import Foundation

/// Push destinations shared by every role stack.
nonisolated enum AuraRoute: Hashable {
    case reserveSpace(stationID: String)
    case memberships
    case scheduleClient
    case appointmentDetail(id: String)
    case profile
    case editProfile
    case legal
    case contractRead
    case contractSign
    case contractSigned
    case contractReview
    case myContract
    case notifications
    case booking(specialistID: String)
    case review(appointmentID: String)
    case success(SuccessKind)
    case membershipCheckout(planID: String)
    case serviceCheckout(specialistID: String, serviceID: String)
    case receipt(paymentID: String)
}

nonisolated enum SuccessKind: Hashable {
    case reservation
    case appointment
    case notify
    case booking
    case review

    var title: String {
        switch self {
        case .notify: "Te avisamos"
        case .booking: "¡Tu cita quedó lista!"
        case .review: "¡Gracias!"
        default: "¡Listo!"
        }
    }

    var message: String {
        switch self {
        case .reservation:
            "Tu espacio quedó reservado. Te esperamos en The Aura Den."
        case .appointment:
            "La cita quedó agendada y tu clienta ya recibió su confirmación."
        case .notify:
            "En cuanto la estación se libere recibirás un aviso para reservarla."
        case .booking:
            "Te esperamos en The Aura Den. Recibirás un recordatorio un día antes."
        case .review:
            "Tu reseña ayuda a que otras invitadas encuentren a la especialista indicada."
        }
    }

    /// Guest-facing outcomes use the client role wording and destinations.
    var isGuestFlow: Bool {
        self == .booking || self == .review
    }
}
