import Foundation
import Observation

/// Holds the whole demo in memory. No persistence: state lives while the app runs.
@Observable
final class DemoStore {
    var role: UserRole = .specialist
    var isSignedIn: Bool = false
    var profile: SpecialistProfile = DemoData.currentSpecialist
    var appointments: [Appointment] = DemoData.appointments
    var checkIns: [CheckIn] = DemoData.checkIns
    var payments: [PaymentRecord] = DemoData.paymentHistory
    var selectedDayID: String = "tue"
    var activePlanID: String = "residente"
    var clientAppointment: Appointment? = DemoData.appointments.first
    var clientHistory: [Appointment] = DemoData.clientHistory
    var lastReservation: String?
    /// Nil while the specialist has not signed her rental agreement yet.
    var signedContract: SignedContract? = DemoData.signedContract
    var contractSignerName: String = AuraCopy.currentUser

    private var folioCounter = 3400

    // MARK: - Derived state

    var activePlan: MembershipPlan { DemoData.plan(id: activePlanID) }

    var hasSignedContract: Bool { signedContract != nil }

    var selectedDay: DaySlot { DemoData.day(id: selectedDayID) }

    var agendaForSelectedDay: [Appointment] {
        appointments
            .filter { $0.dayID == selectedDayID && $0.status != .cancelled }
            .sorted { lhs, rhs in
                let left = DemoData.timeSlots.firstIndex(of: lhs.time) ?? Int.max
                let right = DemoData.timeSlots.firstIndex(of: rhs.time) ?? Int.max
                return left == right ? lhs.time < rhs.time : left < right
            }
    }

    var nextAppointment: Appointment? {
        appointments.first { $0.dayID == "tue" && $0.status == .confirmed }
    }

    var pendingCheckIns: Int {
        checkIns.count { $0.status != .attended }
    }

    var clientUpcoming: [Appointment] {
        guard let appointment = clientAppointment, appointment.status == .confirmed else {
            return []
        }
        return [appointment]
    }

    var hoursProgress: Double {
        let plan = activePlan
        guard plan.hours > 0 else { return 0 }
        return min(Double(profile.hoursUsed) / Double(plan.hours), 1)
    }

    // MARK: - Lookups

    func appointment(id: String) -> Appointment? {
        appointments.first { $0.id == id }
            ?? clientHistory.first { $0.id == id }
            ?? clientAppointment.flatMap { $0.id == id ? $0 : nil }
    }

    func payment(id: String) -> PaymentRecord? {
        payments.first { $0.id == id }
    }

    // MARK: - Agenda actions

    func selectDay(_ dayID: String) {
        selectedDayID = dayID
    }

    func cancelAppointment(id: String) {
        if let index = appointments.firstIndex(where: { $0.id == id }) {
            appointments[index].status = .cancelled
        }
        if var appointment = clientAppointment, appointment.id == id {
            appointment.status = .cancelled
            clientAppointment = appointment
            clientHistory.insert(appointment, at: 0)
        }
    }

    func rescheduleAppointment(id: String, time: String, dayID: String) {
        let day = DemoData.day(id: dayID)
        guard let index = appointments.firstIndex(where: { $0.id == id }) else { return }
        appointments[index].time = time
        appointments[index].dayID = dayID
        appointments[index].dateLabel = day.fullLabel
        selectedDayID = dayID
    }

    func addAppointment(
        clientName: String,
        service: String,
        dayID: String,
        time: String,
        stationName: String,
        notes: String?
    ) {
        let day = DemoData.day(id: dayID)
        let trimmedName = clientName.trimmingCharacters(in: .whitespacesAndNewlines)
        let trimmedNotes = notes?.trimmingCharacters(in: .whitespacesAndNewlines)
        let appointment = Appointment(
            id: "new-\(UUID().uuidString)",
            clientName: trimmedName.isEmpty ? "Nueva clienta" : trimmedName,
            specialistName: profile.name,
            service: service,
            dayID: dayID,
            dateLabel: day.fullLabel,
            time: time,
            stationName: stationName,
            status: .confirmed,
            notes: (trimmedNotes?.isEmpty ?? true) ? nil : trimmedNotes,
            price: DemoData.service(named: service).price
        )
        appointments.append(appointment)
        selectedDayID = dayID
    }

    func reserveStation(stationName: String, dayID: String, time: String) {
        let day = DemoData.day(id: dayID)
        lastReservation = "\(stationName) · \(day.fullLabel) · \(time)"
    }

    // MARK: - Reception actions

    func markArrival(id: String) {
        guard let index = checkIns.firstIndex(where: { $0.id == id }) else { return }
        checkIns[index].status = .attended
    }

    func registerWalkIn(clientName: String, service: String, specialist: String, station: String) {
        let trimmed = clientName.trimmingCharacters(in: .whitespacesAndNewlines)
        let walkIn = CheckIn(
            id: "walk-\(UUID().uuidString)",
            clientName: trimmed.isEmpty ? "Clienta sin cita" : trimmed,
            specialistName: specialist,
            time: "Ahora",
            service: service,
            status: .waiting
        )
        checkIns.insert(walkIn, at: 0)
        _ = station
    }

    // MARK: - Payments (visual only)

    /// Membership charge: the specialist pays the coworking.
    func payMembership(planID: String, method: PaymentMethodOption, invoice: Bool) -> String {
        let plan = DemoData.plan(id: planID)
        let record = PaymentRecord(
            id: "pay-\(UUID().uuidString)",
            folio: nextFolio(),
            concept: "Membresía \(plan.name) · septiembre",
            dateLabel: "18 de agosto",
            amount: plan.price,
            status: method.settlesImmediately ? .paid : .pending,
            method: method.name,
            kind: .membership,
            invoiceRequested: invoice,
            payerName: profile.name
        )
        payments.insert(record, at: 0)
        activePlanID = planID
        return record.id
    }

    /// Service charge: the client pays her specialist through the app.
    func payService(
        service: ClientService,
        specialist: SpecialistProfile,
        method: PaymentMethodOption,
        invoice: Bool
    ) -> String {
        let record = PaymentRecord(
            id: "svc-\(UUID().uuidString)",
            folio: nextFolio(),
            concept: "\(service.name) · \(specialist.name)",
            dateLabel: "18 de agosto",
            amount: service.price,
            status: method.settlesImmediately ? .paid : .pending,
            method: method.name,
            kind: .service,
            invoiceRequested: invoice,
            payerName: "Lucía Gómez"
        )
        payments.insert(record, at: 0)
        return record.id
    }

    // MARK: - Contract (visual only)

    /// A brand new account starts without a contract: she must sign before renting.
    func startContractFlow(name: String) {
        let trimmed = name.trimmingCharacters(in: .whitespacesAndNewlines)
        signedContract = nil
        if !trimmed.isEmpty { contractSignerName = trimmed }
    }

    /// Records the signature. Nothing is stored or sent anywhere.
    func signContract(name: String) {
        let trimmed = name.trimmingCharacters(in: .whitespacesAndNewlines)
        let signer = trimmed.isEmpty ? contractSignerName : trimmed
        contractSignerName = signer
        signedContract = SignedContract(
            folio: DemoData.newContractFolio,
            signerName: signer,
            dateLabel: AuraCopy.contractDate,
            timeLabel: Self.currentTimeLabel()
        )
        profile.contractSigned = true
        profile.contractFolio = DemoData.newContractFolio
        profile.contractDateLabel = AuraCopy.contractDate
    }

    /// Real clock time, so the signature receipt reads like a live confirmation.
    private static func currentTimeLabel() -> String {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "es_MX")
        formatter.dateFormat = "HH:mm"
        return "\(formatter.string(from: Date())) h"
    }

    // MARK: - Profile

    func updateProfile(name: String, phone: String, email: String, specialty: String) {
        let trimmedName = name.trimmingCharacters(in: .whitespacesAndNewlines)
        let trimmedPhone = phone.trimmingCharacters(in: .whitespacesAndNewlines)
        let trimmedEmail = email.trimmingCharacters(in: .whitespacesAndNewlines)
        if !trimmedName.isEmpty { profile.name = trimmedName }
        if !trimmedPhone.isEmpty { profile.phone = trimmedPhone }
        if !trimmedEmail.isEmpty { profile.email = trimmedEmail }
        profile.specialty = specialty
    }

    func signOut() {
        isSignedIn = false
        role = .specialist
    }

    private func nextFolio() -> String {
        folioCounter += 7
        return "AD-2025-\(folioCounter)"
    }
}
