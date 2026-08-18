import SwiftUI

/// Service charge: the client pays her specialist through the app. Separate money flow from
/// the membership checkout, even though both share the brand's checkout language.
struct ServiceCheckoutView: View {
    let specialistID: String
    let serviceID: String
    let onBack: () -> Void
    let onPaid: (String) -> Void

    @Environment(DemoStore.self) private var store

    @State private var methodID: String = DemoData.paymentMethods[0].id
    @State private var invoice = false

    private let serviceFee = 0

    private var specialist: SpecialistProfile { DemoData.specialist(id: specialistID) }
    private var service: ClientService {
        DemoData.services.first { $0.id == serviceID } ?? DemoData.services[0]
    }
    private var method: PaymentMethodOption {
        DemoData.paymentMethods.first { $0.id == methodID } ?? DemoData.paymentMethods[0]
    }

    var body: some View {
        AuraDetailScaffold {
            AuraHeader(
                title: "Pagar servicio",
                eyebrow: "Tu cita",
                subtitle: "\(service.name) con \(specialist.name)",
                onBack: onBack
            )
        } content: {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    Spacer().frame(height: 18)

                    HStack(spacing: 14) {
                        AuraAvatar(imageURL: specialist.imageURL, size: 62, ringColor: .clear)
                        VStack(alignment: .leading, spacing: 0) {
                            EyebrowText(text: "Con", color: AuraPalette.sand)
                                .padding(.bottom, 2)
                            Text(specialist.name)
                                .font(AuraFont.headlineSmall())
                                .foregroundStyle(AuraPalette.navy)
                                .padding(.bottom, 6)
                            HStack(spacing: 6) {
                                RatingStars(rating: specialist.rating, starSize: 13)
                                Text(String(format: "%.1f", specialist.rating))
                                    .font(AuraFont.bodySmall())
                                    .foregroundStyle(AuraPalette.inkMuted)
                            }
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                    }
                    .padding(18)
                    .frame(maxWidth: .infinity)
                    .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))

                    Spacer().frame(height: 16)

                    VStack(alignment: .leading, spacing: 0) {
                        EyebrowText(text: "Servicio")
                            .padding(.bottom, 6)
                        Text(service.name)
                            .font(AuraFont.headlineSmall())
                            .foregroundStyle(AuraPalette.navy)
                            .padding(.bottom, 10)
                        HStack(spacing: 7) {
                            Image(systemName: "clock")
                                .font(.system(size: 13))
                            Text("\(service.durationLabel) · Martes 18 de agosto, 10:00 am")
                                .font(AuraFont.bodyMedium())
                                .fixedSize(horizontal: false, vertical: true)
                        }
                        .foregroundStyle(AuraPalette.blue)
                    }
                    .padding(18)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))

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

                    VStack(alignment: .leading, spacing: 0) {
                        TotalRow(label: service.name, value: Money.format(service.price))
                            .padding(.bottom, 8)
                        TotalRow(label: "Cargo por reservar", value: "$\(serviceFee)")
                            .padding(.bottom, 12)
                        AuraDivider()
                            .padding(.bottom, 12)
                        HStack {
                            Text("Total")
                                .font(AuraFont.titleLarge())
                                .foregroundStyle(AuraPalette.navy)
                                .frame(maxWidth: .infinity, alignment: .leading)
                            Text("\(Money.format(service.price + serviceFee)) MXN")
                                .font(AuraFont.headlineSmall())
                                .foregroundStyle(AuraPalette.blue)
                        }
                        Text("El pago va directo a \(specialist.name); The Aura Den solo procesa el cobro.")
                            .font(AuraFont.bodySmall())
                            .foregroundStyle(AuraPalette.inkMuted)
                            .fixedSize(horizontal: false, vertical: true)
                            .padding(.top, 10)
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

                AuraPrimaryButton(title: "Pagar \(Money.format(service.price))") {
                    let paymentID = store.payService(
                        service: service,
                        specialist: specialist,
                        method: method,
                        invoice: invoice
                    )
                    onPaid(paymentID)
                }
            }
        }
    }
}
