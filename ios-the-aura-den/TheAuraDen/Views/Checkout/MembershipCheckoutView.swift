import SwiftUI

/// Membership charge: the specialist pays the coworking. Visual only, nothing is processed.
struct MembershipCheckoutView: View {
    let planID: String
    let onBack: () -> Void
    let onPaid: (String) -> Void

    @Environment(DemoStore.self) private var store

    @State private var methodID: String = DemoData.paymentMethods[0].id
    @State private var invoice = false

    private var plan: MembershipPlan { DemoData.plan(id: planID) }
    private var method: PaymentMethodOption {
        DemoData.paymentMethods.first { $0.id == methodID } ?? DemoData.paymentMethods[0]
    }

    var body: some View {
        AuraDetailScaffold {
            AuraHeader(
                title: "Pagar membresía",
                eyebrow: "Renta del espacio",
                subtitle: "Plan \(plan.name) · mensual",
                onBack: onBack
            )
        } content: {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    Spacer().frame(height: 18)

                    HStack(alignment: .top, spacing: 12) {
                        VStack(alignment: .leading, spacing: 0) {
                            EyebrowText(text: "Resumen del plan", color: AuraPalette.sand)
                                .padding(.bottom, 6)
                            Text(plan.name)
                                .font(AuraFont.displaySmall())
                                .foregroundStyle(AuraPalette.navy)
                                .padding(.bottom, 8)
                            ForEach(plan.perks, id: \.self) { perk in
                                Text("· \(perk)")
                                    .font(AuraFont.bodyMedium())
                                    .foregroundStyle(AuraPalette.ink)
                                    .fixedSize(horizontal: false, vertical: true)
                            }
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)

                        AuraCardMark()
                            .frame(width: 54)
                    }
                    .padding(20)
                    .frame(maxWidth: .infinity)
                    .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))

                    Spacer().frame(height: 24)

                    SectionHeading(text: "Método de pago")
                        .padding(.bottom, 10)

                    ForEach(DemoData.paymentMethods) { option in
                        PaymentMethodRow(
                            option: option,
                            isSelected: option.id == methodID,
                            onSelect: { methodID = option.id }
                        )
                        .padding(.bottom, 10)
                    }

                    Spacer().frame(height: 8)

                    InvoiceToggleRow(isOn: $invoice)

                    Spacer().frame(height: 22)

                    VStack(spacing: 0) {
                        TotalRow(label: "Membresía \(plan.name)", value: Money.format(plan.price))
                            .padding(.bottom, 8)
                        TotalRow(label: "Comisión de servicio", value: "$0")
                            .padding(.bottom, 12)
                        AuraDivider()
                            .padding(.bottom, 12)
                        HStack {
                            Text("Total")
                                .font(AuraFont.titleLarge())
                                .foregroundStyle(AuraPalette.navy)
                                .frame(maxWidth: .infinity, alignment: .leading)
                            Text("\(Money.format(plan.price)) MXN")
                                .font(AuraFont.headlineSmall())
                                .foregroundStyle(AuraPalette.blue)
                        }
                    }
                    .padding(18)
                    .frame(maxWidth: .infinity)
                    .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))

                    Spacer().frame(height: 26)
                }
                .padding(.horizontal, 20)
            }
        } bottomAction: {
            VStack(alignment: .leading, spacing: 10) {
                HStack(spacing: 6) {
                    Image(systemName: "lock.fill")
                        .font(.system(size: 12))
                    Text("Demo sin cobro real")
                        .font(AuraFont.bodySmall())
                }
                .foregroundStyle(AuraPalette.inkMuted)

                AuraPrimaryButton(title: "Pagar \(Money.format(plan.price))") {
                    let paymentID = store.payMembership(
                        planID: planID,
                        method: method,
                        invoice: invoice
                    )
                    onPaid(paymentID)
                }
            }
        }
    }
}

struct PaymentMethodRow: View {
    let option: PaymentMethodOption
    let isSelected: Bool
    let onSelect: () -> Void

    private var symbol: String {
        switch option.id {
        case "spei": "building.columns.fill"
        case "mercadopago": "wallet.pass.fill"
        default: "creditcard.fill"
        }
    }

    var body: some View {
        Button {
            AuraHaptics.tap()
            onSelect()
        } label: {
            HStack(spacing: 13) {
                ZStack {
                    RoundedRectangle(cornerRadius: 13)
                        .fill(isSelected ? AuraPalette.blue.opacity(0.1) : AuraPalette.cream)
                        .frame(width: 42, height: 42)
                    Image(systemName: symbol)
                        .font(.system(size: 18))
                        .foregroundStyle(isSelected ? AuraPalette.blue : AuraPalette.sand)
                }

                VStack(alignment: .leading, spacing: 2) {
                    Text(option.name)
                        .font(AuraFont.titleMedium())
                        .fontWeight(.medium)
                        .foregroundStyle(AuraPalette.ink)
                    Text(option.detail)
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .multilineTextAlignment(.leading)
                        .fixedSize(horizontal: false, vertical: true)
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                Image(systemName: isSelected ? "largecircle.fill.circle" : "circle")
                    .font(.system(size: 21))
                    .foregroundStyle(isSelected ? AuraPalette.blue : AuraPalette.divider)
            }
            .padding(14)
            .frame(maxWidth: .infinity)
            .background(AuraPalette.white, in: .rect(cornerRadius: 18))
            .overlay {
                RoundedRectangle(cornerRadius: 18)
                    .strokeBorder(isSelected ? AuraPalette.blue : AuraPalette.divider, lineWidth: 1)
            }
        }
        .buttonStyle(PressableButtonStyle())
    }
}

struct InvoiceToggleRow: View {
    @Binding var isOn: Bool

    var body: some View {
        Button {
            AuraHaptics.tap()
            isOn.toggle()
        } label: {
            HStack(spacing: 12) {
                Image(systemName: isOn ? "checkmark.square.fill" : "square")
                    .font(.system(size: 22))
                    .foregroundStyle(isOn ? AuraPalette.blue : AuraPalette.divider)

                VStack(alignment: .leading, spacing: 2) {
                    Text("Solicitar factura (CFDI)")
                        .font(AuraFont.titleMedium())
                        .foregroundStyle(AuraPalette.ink)
                    Text("Se emite con tus datos fiscales registrados")
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .multilineTextAlignment(.leading)
                        .fixedSize(horizontal: false, vertical: true)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 16)
            .frame(maxWidth: .infinity)
            .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
        }
        .buttonStyle(PressableButtonStyle())
    }
}

struct TotalRow: View {
    let label: String
    let value: String

    var body: some View {
        HStack {
            Text(label)
                .font(AuraFont.bodyLarge())
                .foregroundStyle(AuraPalette.inkMuted)
                .frame(maxWidth: .infinity, alignment: .leading)
            Text(value)
                .font(AuraFont.titleMedium())
                .foregroundStyle(AuraPalette.ink)
        }
    }
}
