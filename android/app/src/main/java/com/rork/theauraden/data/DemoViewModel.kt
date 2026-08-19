package com.rork.theauraden.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Immutable snapshot of the whole demo. No persistence: state lives while the app runs. */
data class DemoUiState(
    val role: UserRole = UserRole.SPECIALIST,
    val profile: SpecialistProfile = DemoData.currentSpecialist,
    val appointments: List<Appointment> = DemoData.appointments,
    val checkIns: List<CheckIn> = DemoData.checkIns,
    val payments: List<PaymentRecord> = DemoData.paymentHistory,
    val selectedDayId: String = "tue",
    val activePlanId: String = "residente",
    val clientAppointment: Appointment? = DemoData.appointments.first(),
    val clientHistory: List<Appointment> = DemoData.clientHistory,
    val lastPaymentId: String? = null,
    val lastReservation: String? = null,
    /** Null while the specialist has not signed her rental agreement yet. */
    val signedContract: SignedContract? = DemoData.signedContract,
    val contractSignerName: String = AuraCopy.CURRENT_USER,
    /** Name of the guest using the app. Changes when a brand new guest signs up. */
    val guestName: String = AuraCopy.CLIENT_USER,
    /** True right after a guest signs up: she has no appointments and no history yet. */
    val isNewGuest: Boolean = false,
    val readNotificationIds: Set<String> = emptySet(),
    /** Appointment ids the guest already rated. */
    val reviewedAppointmentIds: Set<String> = emptySet()
) {
    val hasSignedContract: Boolean get() = signedContract != null

    val guestInitials: String
        get() = guestName.trim().split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifBlank { "AD" }

    val notifications: List<AppNotification>
        get() = DemoData.notificationsForRole(role).map {
            if (it.id in readNotificationIds) it.copy(unread = false) else it
        }

    val unreadNotifications: Int get() = notifications.count { it.unread }

    /** A completed visit the guest has not rated yet, offered right after her service. */
    val reviewableAppointment: Appointment?
        get() = clientHistory.firstOrNull {
            it.status == AppointmentStatus.COMPLETED && it.id !in reviewedAppointmentIds
        }

    val activePlan: MembershipPlan get() = DemoData.planById(activePlanId)

    val agendaForSelectedDay: List<Appointment>
        get() = appointments.filter { it.dayId == selectedDayId && it.status != AppointmentStatus.CANCELLED }
            .sortedBy { DemoData.timeSlots.indexOf(it.time) }

    val selectedDay: DaySlot
        get() = DemoData.weekDays.first { it.id == selectedDayId }

    val nextAppointment: Appointment?
        get() = appointments.firstOrNull {
            it.dayId == "tue" && it.status == AppointmentStatus.CONFIRMED
        }

    val pendingCheckIns: Int
        get() = checkIns.count { it.status != CheckInStatus.ATTENDED }

    val clientUpcoming: List<Appointment>
        get() = listOfNotNull(clientAppointment?.takeIf { it.status == AppointmentStatus.CONFIRMED })
}

class DemoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DemoUiState())
    val uiState: StateFlow<DemoUiState> = _uiState.asStateFlow()

    private var folioCounter = 3400

    fun setRole(role: UserRole) {
        _uiState.update { it.copy(role = role) }
    }

    /** A brand new guest account: no upcoming appointment and no past visits. */
    fun startGuestAccount(name: String) {
        _uiState.update {
            it.copy(
                role = UserRole.CLIENT,
                guestName = name.ifBlank { it.guestName },
                isNewGuest = true,
                clientAppointment = null,
                clientHistory = emptyList()
            )
        }
    }

    /** The guest books her own appointment from Explorar. */
    fun bookGuestAppointment(
        specialist: SpecialistProfile,
        service: ClientService,
        dayId: String,
        time: String
    ): Appointment {
        val day = DemoData.weekDays.first { it.id == dayId }
        val station = DemoData.stations.firstOrNull {
            if (service.name.contains("Pestañas")) it.kind == "Pestañas" else it.kind != "Pestañas"
        } ?: DemoData.stations.first()
        val appointment = Appointment(
            id = "guest-${System.currentTimeMillis()}",
            clientName = _uiState.value.guestName,
            specialistName = specialist.name,
            service = service.name,
            dayId = dayId,
            dateLabel = day.fullLabel,
            time = time,
            stationName = station.name,
            status = AppointmentStatus.CONFIRMED,
            notes = null,
            price = service.price
        )
        _uiState.update {
            it.copy(
                clientAppointment = appointment,
                isNewGuest = false,
                appointments = it.appointments + appointment
            )
        }
        return appointment
    }

    fun markNotificationsRead() {
        _uiState.update { state ->
            state.copy(
                readNotificationIds = state.readNotificationIds +
                    DemoData.notificationsForRole(state.role).map { it.id }
            )
        }
    }

    /** Stores the guest's rating so the same visit is not offered again. */
    fun submitReview(appointmentId: String) {
        _uiState.update { it.copy(reviewedAppointmentIds = it.reviewedAppointmentIds + appointmentId) }
    }

    /** Puts every screen back to its opening state so the demo can be shown again. */
    fun resetDemo() {
        folioCounter = 3400
        _uiState.value = DemoUiState()
    }

    fun selectDay(dayId: String) {
        _uiState.update { it.copy(selectedDayId = dayId) }
    }

    fun appointmentById(id: String): Appointment? =
        _uiState.value.appointments.firstOrNull { it.id == id }
            ?: _uiState.value.clientHistory.firstOrNull { it.id == id }
            ?: _uiState.value.clientAppointment?.takeIf { it.id == id }

    fun cancelAppointment(id: String) {
        _uiState.update { state ->
            state.copy(
                appointments = state.appointments.map {
                    if (it.id == id) it.copy(status = AppointmentStatus.CANCELLED) else it
                },
                clientAppointment = state.clientAppointment?.let {
                    if (it.id == id) it.copy(status = AppointmentStatus.CANCELLED) else it
                },
                clientHistory = if (state.clientAppointment?.id == id) {
                    listOf(
                        state.clientAppointment.copy(status = AppointmentStatus.CANCELLED)
                    ) + state.clientHistory
                } else {
                    state.clientHistory
                }
            )
        }
    }

    fun rescheduleAppointment(id: String, time: String, dayId: String) {
        val day = DemoData.weekDays.first { it.id == dayId }
        _uiState.update { state ->
            state.copy(
                appointments = state.appointments.map {
                    if (it.id == id) {
                        it.copy(time = time, dayId = dayId, dateLabel = day.fullLabel)
                    } else {
                        it
                    }
                },
                selectedDayId = dayId
            )
        }
    }

    fun addAppointment(
        clientName: String,
        service: String,
        dayId: String,
        time: String,
        stationName: String,
        notes: String?
    ) {
        val day = DemoData.weekDays.first { it.id == dayId }
        val appointment = Appointment(
            id = "new-${System.currentTimeMillis()}",
            clientName = clientName.ifBlank { "Nueva clienta" },
            specialistName = _uiState.value.profile.name,
            service = service,
            dayId = dayId,
            dateLabel = day.fullLabel,
            time = time,
            stationName = stationName,
            status = AppointmentStatus.CONFIRMED,
            notes = notes?.takeIf { it.isNotBlank() },
            price = DemoData.serviceByName(service).price
        )
        _uiState.update {
            it.copy(appointments = it.appointments + appointment, selectedDayId = dayId)
        }
    }

    fun reserveStation(stationName: String, dayId: String, time: String) {
        val day = DemoData.weekDays.first { it.id == dayId }
        _uiState.update {
            it.copy(lastReservation = "$stationName · ${day.fullLabel} · $time")
        }
    }

    fun markArrival(id: String) {
        _uiState.update { state ->
            state.copy(
                checkIns = state.checkIns.map {
                    if (it.id == id) it.copy(status = CheckInStatus.ATTENDED) else it
                }
            )
        }
    }

    fun registerWalkIn(clientName: String, service: String, specialist: String, station: String) {
        val walkIn = CheckIn(
            id = "walk-${System.currentTimeMillis()}",
            clientName = clientName.ifBlank { "Clienta sin cita" },
            specialistName = specialist,
            time = "Ahora",
            service = service,
            status = CheckInStatus.WAITING
        )
        _uiState.update { it.copy(checkIns = listOf(walkIn) + it.checkIns) }
    }

    /** Membership charge: specialist pays the coworking. Visual only. */
    fun payMembership(planId: String, method: PaymentMethodOption, invoice: Boolean): String {
        val plan = DemoData.planById(planId)
        val record = PaymentRecord(
            id = "pay-${System.currentTimeMillis()}",
            folio = nextFolio(),
            concept = "Membresía ${plan.name} · septiembre",
            dateLabel = "18 de agosto",
            amount = plan.price,
            status = if (method.settlesImmediately) PaymentStatus.PAID else PaymentStatus.PENDING,
            method = method.name,
            kind = PaymentKind.MEMBERSHIP,
            invoiceRequested = invoice,
            payerName = _uiState.value.profile.name
        )
        _uiState.update {
            it.copy(
                payments = listOf(record) + it.payments,
                activePlanId = planId,
                lastPaymentId = record.id
            )
        }
        return record.id
    }

    /** Service charge: the client pays her specialist through the app. Visual only. */
    fun payService(
        service: ClientService,
        specialist: SpecialistProfile,
        method: PaymentMethodOption,
        invoice: Boolean
    ): String {
        val record = PaymentRecord(
            id = "svc-${System.currentTimeMillis()}",
            folio = nextFolio(),
            concept = "${service.name} · ${specialist.name}",
            dateLabel = "18 de agosto",
            amount = service.price,
            status = if (method.settlesImmediately) PaymentStatus.PAID else PaymentStatus.PENDING,
            method = method.name,
            kind = PaymentKind.SERVICE,
            invoiceRequested = invoice,
            payerName = "Lucía Gómez"
        )
        _uiState.update {
            it.copy(payments = listOf(record) + it.payments, lastPaymentId = record.id)
        }
        return record.id
    }

    fun paymentById(id: String?): PaymentRecord? =
        _uiState.value.payments.firstOrNull { it.id == id }

    /** A brand new account starts without a contract: she must sign before renting. */
    fun startContractFlow(name: String) {
        _uiState.update { state ->
            state.copy(
                signedContract = null,
                contractSignerName = name.ifBlank { state.contractSignerName }
            )
        }
    }

    /** Records the signature. Visual only: nothing is stored or sent anywhere. */
    fun signContract(name: String) {
        _uiState.update { state ->
            val signer = name.ifBlank { state.contractSignerName }
            state.copy(
                signedContract = SignedContract(
                    folio = DemoData.NEW_CONTRACT_FOLIO,
                    signerName = signer,
                    dateLabel = AuraCopy.CONTRACT_DATE,
                    timeLabel = currentTimeLabel()
                ),
                contractSignerName = signer,
                profile = state.profile.copy(
                    contractSigned = true,
                    contractFolio = DemoData.NEW_CONTRACT_FOLIO,
                    contractDateLabel = AuraCopy.CONTRACT_DATE
                )
            )
        }
    }

    fun updateProfile(name: String, phone: String, email: String, specialty: String) {
        _uiState.update {
            it.copy(
                profile = it.profile.copy(
                    name = name.ifBlank { it.profile.name },
                    phone = phone.ifBlank { it.profile.phone },
                    email = email.ifBlank { it.profile.email },
                    specialty = specialty
                )
            )
        }
    }

    private fun nextFolio(): String {
        folioCounter += 7
        return "AD-2025-$folioCounter"
    }

    /** Real clock time, so the signature receipt reads like a live confirmation. */
    private fun currentTimeLabel(): String {
        val formatter = java.text.SimpleDateFormat("HH:mm", java.util.Locale("es", "MX"))
        return "${formatter.format(java.util.Date())} h"
    }
}
