import Foundation

/// Domain models for the navigable demo. Everything is in-memory and hardcoded —
/// there is no database, no network and no real payment processing.

nonisolated enum UserRole: String, CaseIterable, Identifiable, Hashable {
    case specialist
    case client
    case reception
    case admin

    var id: String { rawValue }

    var label: String {
        switch self {
        case .specialist: "Especialista"
        case .client: "Clienta"
        case .reception: "Recepcionista"
        case .admin: "Administrador"
        }
    }

    var summary: String {
        switch self {
        case .specialist: "Renta espacio y atiende a sus clientas"
        case .client: "Recibe el servicio"
        case .reception: "Hace check-in y apoya en piso"
        case .admin: "Ve todo el negocio"
        }
    }

    var symbol: String {
        switch self {
        case .specialist: "hands.sparkles"
        case .client: "heart"
        case .reception: "storefront"
        case .admin: "chart.bar"
        }
    }
}

nonisolated enum StationStatus: Hashable {
    case available
    case occupied
}

nonisolated struct Station: Identifiable, Hashable {
    let id: String
    let name: String
    let kind: String
    let status: StationStatus
    let scheduleLabel: String
    let amenities: [String]
    let description: String
    let imageURL: String
    var occupiedBy: String?
    var nextAvailability: String?
    let hourlyRate: Int
}

nonisolated enum AppointmentStatus: Hashable {
    case confirmed
    case completed
    case cancelled

    var label: String {
        switch self {
        case .confirmed: "Confirmada"
        case .completed: "Completada"
        case .cancelled: "Cancelada"
        }
    }
}

nonisolated struct Appointment: Identifiable, Hashable {
    let id: String
    let clientName: String
    let specialistName: String
    let service: String
    var dayID: String
    var dateLabel: String
    var time: String
    let stationName: String
    var status: AppointmentStatus
    var notes: String?
    let price: Int
}

nonisolated enum CheckInStatus: Hashable {
    case pending
    case waiting
    case attended

    var label: String {
        switch self {
        case .pending: "Por llegar"
        case .waiting: "En espera"
        case .attended: "Atendida"
        }
    }
}

nonisolated struct CheckIn: Identifiable, Hashable {
    let id: String
    let clientName: String
    let specialistName: String
    let time: String
    let service: String
    var status: CheckInStatus
}

nonisolated struct MembershipPlan: Identifiable, Hashable {
    let id: String
    let name: String
    let price: Int
    let hours: Int
    let locker: String
    let perks: [String]
    var recommended: Bool = false
}

nonisolated enum PaymentStatus: Hashable {
    case paid
    case pending
    case failed

    var label: String {
        switch self {
        case .paid: "Pagado"
        case .pending: "Pendiente"
        case .failed: "Fallido"
        }
    }
}

nonisolated enum PaymentKind: Hashable {
    case membership
    case service
}

nonisolated struct PaymentRecord: Identifiable, Hashable {
    let id: String
    let folio: String
    let concept: String
    let dateLabel: String
    let amount: Int
    let status: PaymentStatus
    let method: String
    let kind: PaymentKind
    var invoiceRequested: Bool = false
    var payerName: String?
}

nonisolated struct PaymentMethodOption: Identifiable, Hashable {
    let id: String
    let name: String
    let detail: String
    /// SPEI settles later, so it produces a pending receipt in the demo.
    let settlesImmediately: Bool
}

nonisolated struct DaySlot: Identifiable, Hashable {
    let id: String
    let weekdayShort: String
    let dayNumber: String
    let fullLabel: String
}

nonisolated struct Review: Identifiable, Hashable {
    let id: String
    let author: String
    let rating: Int
    let comment: String
    let timeAgo: String
}

nonisolated struct SpecialistProfile: Identifiable, Hashable {
    let id: String
    var name: String
    var specialty: String
    var phone: String
    var email: String
    let since: String
    let rating: Double
    let reviewCount: Int
    let imageURL: String
    let planID: String
    let hoursUsed: Int
    let paymentStatus: PaymentStatus
    var nextServiceLabel: String?
    /// A renter cannot book a station until her rental agreement is signed.
    var contractSigned: Bool = true
    var contractFolio: String?
    var contractDateLabel: String?
}

/// One numbered clause of the rental agreement.
nonisolated struct ContractSection: Identifiable, Hashable {
    var id: String { number }
    let number: String
    let title: String
    let body: String
}

/// Signed rental agreement. Visual only: no PDF, no legal validity.
nonisolated struct SignedContract: Hashable {
    let folio: String
    let signerName: String
    let dateLabel: String
    let timeLabel: String
}

nonisolated struct ClientService: Identifiable, Hashable {
    let id: String
    let name: String
    let durationLabel: String
    let price: Int
}

nonisolated struct ReportMetric: Identifiable, Hashable {
    var id: String { label }
    let label: String
    let value: String
    let delta: String
    let positive: Bool
}

nonisolated struct ServiceDemand: Identifiable, Hashable {
    var id: String { service }
    let service: String
    let count: Int
    let share: Double
}

nonisolated struct MonthRevenue: Identifiable, Hashable {
    var id: String { month }
    let month: String
    let amount: Int
    let share: Double
}

/// Recurring rent contributed by each membership tier.
nonisolated struct PlanRevenue: Identifiable, Hashable {
    var id: String { planName }
    let planName: String
    let specialists: Int
    let amount: Int
    let share: Double
}

/// Ranking of the specialists who booked the most appointments this month.
nonisolated struct TopSpecialist: Identifiable, Hashable {
    var id: Int { position }
    let position: Int
    let name: String
    let specialty: String
    let imageURL: String
    let appointments: Int
    let revenue: Int
    let share: Double
}

/// A membership charge the coworking still has to collect.
nonisolated struct PendingCharge: Identifiable, Hashable {
    let id: String
    let specialistName: String
    let planName: String
    let amount: Int
    let dueLabel: String
    /// Past due charges are shown in red and counted apart.
    let overdue: Bool
}

nonisolated struct KpiTile: Identifiable, Hashable {
    var id: String { label }
    let value: String
    let label: String
}

/// Formats money the way the demo copy does: `$3,200`.
nonisolated enum Money {
    static func format(_ amount: Int) -> String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.groupingSeparator = ","
        formatter.maximumFractionDigits = 0
        let number = formatter.string(from: NSNumber(value: amount)) ?? "\(amount)"
        return "$\(number)"
    }
}
