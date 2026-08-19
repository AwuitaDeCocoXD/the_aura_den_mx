import SwiftUI

/// Shared confirmation moment after a reservation or a new appointment.
struct SuccessView: View {
    let kind: SuccessKind
    let onPrimary: () -> Void
    let onSecondary: () -> Void

    @Environment(DemoStore.self) private var store
    @State private var visible = false

    private var detail: String? {
        switch kind {
        case .reservation, .notify:
            store.lastReservation
        case .appointment:
            store.agendaForSelectedDay.last.map {
                "\($0.clientName) · \($0.time) · \($0.stationName)"
            }
        case .booking:
            store.clientAppointment.map {
                "\($0.specialistName) · \($0.dateLabel) · \($0.time)"
            }
        case .review:
            nil
        }
    }

    private var primaryLabel: String {
        kind.isGuestFlow ? "Ver mi cita" : "Volver al inicio"
    }

    private var secondaryLabel: String {
        switch kind {
        case .appointment: "Ver mi agenda"
        case .booking, .review: "Explorar más especialistas"
        default: "Ver mis espacios"
        }
    }

    var body: some View {
        ZStack {
            AuraPalette.blue.ignoresSafeArea()

            VStack(spacing: 0) {
                Spacer(minLength: 0)

                ZStack {
                    Circle()
                        .fill(AuraPalette.yellow)
                        .frame(width: 116, height: 116)
                    Image(systemName: "checkmark")
                        .font(.system(size: 52, weight: .medium))
                        .foregroundStyle(AuraPalette.navyDeep)
                }
                .scaleEffect(visible ? 1 : 0.6)

                Spacer().frame(height: 26)

                AuraArc(color: AuraPalette.yellow.opacity(0.7), lineWidth: 2)
                    .frame(width: 64, height: 24)

                Spacer().frame(height: 16)

                Text(kind.title)
                    .font(AuraFont.displayMedium())
                    .foregroundStyle(AuraPalette.white)
                    .multilineTextAlignment(.center)

                Spacer().frame(height: 10)

                Text(kind.message)
                    .font(AuraFont.bodyLarge())
                    .foregroundStyle(AuraPalette.white.opacity(0.85))
                    .multilineTextAlignment(.center)
                    .fixedSize(horizontal: false, vertical: true)

                if let detail {
                    Spacer().frame(height: 20)
                    Text(detail)
                        .font(AuraFont.titleSmall())
                        .foregroundStyle(AuraPalette.yellow)
                        .multilineTextAlignment(.center)
                        .padding(.horizontal, 20)
                        .padding(.vertical, 14)
                        .frame(maxWidth: .infinity)
                        .background(AuraPalette.navy.opacity(0.45), in: .capsule)
                }

                Spacer(minLength: 0)

                VStack(spacing: 10) {
                    AuraPrimaryButton(
                        title: primaryLabel,
                        tint: AuraPalette.white,
                        foreground: AuraPalette.blue,
                        action: onPrimary
                    )
                    AuraPrimaryButton(
                        title: secondaryLabel,
                        tint: AuraPalette.white.opacity(0.14),
                        foreground: AuraPalette.white,
                        action: onSecondary
                    )
                }
                .padding(.bottom, 24)
            }
            .padding(.horizontal, 28)
        }
        .toolbar(.hidden, for: .navigationBar)
        .onAppear {
            AuraHaptics.success()
            withAnimation(.spring(response: 0.5, dampingFraction: 0.55)) {
                visible = true
            }
        }
    }
}
