import SwiftUI

/// Account status: current membership, hours used and payment history.
struct PaymentsView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    let onChangePlan: () -> Void
    let onOpenReceipt: (String) -> Void

    @Environment(DemoStore.self) private var store

    var body: some View {
        let plan = store.activePlan
        let membershipPayments = store.payments.filter { $0.kind == .membership }
        let allPaid = !membershipPayments.contains { $0.status == .failed }

        AuraTabScaffold(
            role: .specialist,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: "Mis pagos",
                eyebrow: store.profile.name,
                content: {
                    StatusPill(
                        text: allPaid ? "Todo al corriente" : "Revisa un cargo",
                        foreground: AuraPalette.green,
                        background: AuraPalette.greenSoft,
                        symbol: "checkmark.circle.fill"
                    )
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 18)

                    CurrentMembershipCard(
                        planName: plan.name,
                        price: plan.price,
                        hoursUsed: store.profile.hoursUsed,
                        totalHours: plan.hours
                    )
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 26)

                    SectionHeading(text: "Historial de pagos")
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 10)

                    VStack(spacing: 10) {
                        ForEach(membershipPayments) { payment in
                            PaymentRow(payment: payment) { onOpenReceipt(payment.id) }
                        }
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 22)

                    AuraPrimaryButton(title: "Cambiar de plan", action: onChangePlan)
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 28)
                }
            }
        }
    }
}

private struct CurrentMembershipCard: View {
    let planName: String
    let price: Int
    let hoursUsed: Int
    let totalHours: Int

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(alignment: .top, spacing: 12) {
                VStack(alignment: .leading, spacing: 0) {
                    EyebrowText(text: "Membresía", color: AuraPalette.sand)
                        .padding(.bottom, 4)
                    Text(planName)
                        .font(AuraFont.displaySmall())
                        .foregroundStyle(AuraPalette.navy)
                        .padding(.bottom, 4)
                    HStack(alignment: .lastTextBaseline, spacing: 5) {
                        Text(Money.format(price))
                            .font(AuraFont.headlineLarge())
                            .foregroundStyle(AuraPalette.blue)
                        Text("/ mes")
                            .font(AuraFont.bodyMedium())
                            .foregroundStyle(AuraPalette.inkMuted)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                AuraCardMark()
                    .frame(width: 58)
            }
            .padding(.bottom, 20)

            Text("\(hoursUsed) de \(totalHours) horas usadas")
                .font(AuraFont.titleMedium())
                .foregroundStyle(AuraPalette.navy)
                .padding(.bottom, 10)

            AuraProgressBar(
                progress: totalHours == 0 ? 0 : Double(hoursUsed) / Double(totalHours)
            )
            .padding(.bottom, 16)

            HStack(spacing: 7) {
                Image(systemName: "calendar")
                    .font(.system(size: 14))
                    .foregroundStyle(AuraPalette.sand)
                Text("Próximo pago: 15 de septiembre")
                    .font(AuraFont.bodyLarge())
                    .foregroundStyle(AuraPalette.navy)
            }
        }
        .padding(20)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))
    }
}

struct PaymentRow: View {
    let payment: PaymentRecord
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            HStack(spacing: 0) {
                ZStack {
                    RoundedRectangle(cornerRadius: 14)
                        .fill(AuraPalette.sandSoft)
                        .frame(width: 44, height: 44)
                    Image(systemName: "calendar")
                        .font(.system(size: 18))
                        .foregroundStyle(AuraPalette.sand)
                }
                .padding(.trailing, 13)

                VStack(alignment: .leading, spacing: 2) {
                    Text(payment.dateLabel)
                        .font(AuraFont.titleMedium())
                        .fontWeight(.medium)
                        .foregroundStyle(AuraPalette.ink)
                    Text(Money.format(payment.amount))
                        .font(AuraFont.bodyMedium())
                        .foregroundStyle(AuraPalette.inkMuted)
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                PaymentStatusPill(status: payment.status)

                Image(systemName: "chevron.right")
                    .font(.system(size: 15, weight: .medium))
                    .foregroundStyle(AuraPalette.inkMuted)
                    .padding(.leading, 4)
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 14)
            .frame(maxWidth: .infinity)
            .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
        }
        .buttonStyle(PressableButtonStyle())
    }
}
