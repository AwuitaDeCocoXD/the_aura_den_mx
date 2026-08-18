package com.rork.theauraden.data

/**
 * Domain models for the navigable demo. Everything is in-memory and hardcoded —
 * there is no database, no network and no real payment processing.
 */

enum class UserRole(val label: String, val description: String) {
    SPECIALIST("Especialista", "Renta espacio y atiende a sus clientas"),
    CLIENT("Clienta", "Recibe el servicio"),
    RECEPTION("Recepcionista", "Hace check-in y apoya en piso"),
    ADMIN("Administrador", "Ve todo el negocio")
}

enum class StationStatus { AVAILABLE, OCCUPIED }

data class Station(
    val id: String,
    val name: String,
    val kind: String,
    val status: StationStatus,
    val scheduleLabel: String,
    val amenities: List<String>,
    val description: String,
    val imageUrl: String,
    val occupiedBy: String? = null,
    val nextAvailability: String? = null,
    val hourlyRate: Int
)

enum class AppointmentStatus(val label: String) {
    CONFIRMED("Confirmada"),
    COMPLETED("Completada"),
    CANCELLED("Cancelada")
}

data class Appointment(
    val id: String,
    val clientName: String,
    val specialistName: String,
    val service: String,
    val dayId: String,
    val dateLabel: String,
    val time: String,
    val stationName: String,
    val status: AppointmentStatus,
    val notes: String? = null,
    val price: Int
)

enum class CheckInStatus(val label: String) {
    PENDING("Por llegar"),
    WAITING("En espera"),
    ATTENDED("Atendida")
}

data class CheckIn(
    val id: String,
    val clientName: String,
    val specialistName: String,
    val time: String,
    val service: String,
    val status: CheckInStatus
)

data class MembershipPlan(
    val id: String,
    val name: String,
    val price: Int,
    val hours: Int,
    val locker: String,
    val perks: List<String>,
    val recommended: Boolean = false
)

enum class PaymentStatus(val label: String) {
    PAID("Pagado"),
    PENDING("Pendiente"),
    FAILED("Fallido")
}

enum class PaymentKind { MEMBERSHIP, SERVICE }

data class PaymentRecord(
    val id: String,
    val folio: String,
    val concept: String,
    val dateLabel: String,
    val amount: Int,
    val status: PaymentStatus,
    val method: String,
    val kind: PaymentKind,
    val invoiceRequested: Boolean = false,
    val payerName: String? = null
)

data class PaymentMethodOption(
    val id: String,
    val name: String,
    val detail: String,
    /** SPEI settles later, so it produces a pending receipt in the demo. */
    val settlesImmediately: Boolean
)

data class DaySlot(
    val id: String,
    val weekdayShort: String,
    val dayNumber: String,
    val fullLabel: String
)

data class Review(
    val id: String,
    val author: String,
    val rating: Int,
    val comment: String,
    val timeAgo: String
)

data class SpecialistProfile(
    val id: String,
    val name: String,
    val specialty: String,
    val phone: String,
    val email: String,
    val since: String,
    val rating: Double,
    val reviewCount: Int,
    val imageUrl: String,
    val planId: String,
    val hoursUsed: Int,
    val paymentStatus: PaymentStatus,
    val nextServiceLabel: String? = null,
    /** A renter cannot book a station until her rental agreement is signed. */
    val contractSigned: Boolean = true,
    val contractFolio: String? = null,
    val contractDateLabel: String? = null
)

/** One numbered clause of the rental agreement. */
data class ContractSection(
    val number: String,
    val title: String,
    val body: String
)

/** Signed rental agreement. Visual only: no PDF, no legal validity. */
data class SignedContract(
    val folio: String,
    val signerName: String,
    val dateLabel: String,
    val timeLabel: String
)

data class ClientService(
    val id: String,
    val name: String,
    val durationLabel: String,
    val price: Int
)

data class ReportMetric(
    val label: String,
    val value: String,
    val delta: String,
    val positive: Boolean
)

data class ServiceDemand(
    val service: String,
    val count: Int,
    val share: Float
)

data class MonthRevenue(
    val month: String,
    val amount: Int,
    val share: Float
)

/** Recurring rent contributed by each membership tier. */
data class PlanRevenue(
    val planName: String,
    val specialists: Int,
    val amount: Int,
    val share: Float
)

/** Ranking of the specialists who booked the most appointments this month. */
data class TopSpecialist(
    val position: Int,
    val name: String,
    val specialty: String,
    val imageUrl: String,
    val appointments: Int,
    val revenue: Int,
    val share: Float
)

/** A membership charge the coworking still has to collect. */
data class PendingCharge(
    val id: String,
    val specialistName: String,
    val planName: String,
    val amount: Int,
    val dueLabel: String,
    /** Past due charges are shown in red and counted apart. */
    val overdue: Boolean
)
