import SwiftUI

/// Receipt for both money flows, covering paid, pending and failed states.
struct ReceiptView: View {
    let paymentID: String
    let onBack: () -> Void
    let onPrimary: (PaymentKind) -> Void
    let onRetry: (PaymentKind) -> Void

    @Environment(DemoStore.self) private var store

    private var payment: PaymentRecord? { store.payment(id: paymentID) }

    var body: some View {
        if let payment {
            content(for: payment)
        } else {
            AuraPlainScaffold {
                AuraHeader(title: "Comprobante", onBack: onBack)
            } content: {
                AuraEmptyState(
                    title: "Comprobante no disponible",
                    message: "No encontramos este movimiento.",
                    symbol: "doc.text"
                )
            }
        }
    }

    @ViewBuilder
    private func content(for payment: PaymentRecord) -> some View {
        let isMembership = payment.kind == .membership

        AuraDetailScaffold {
            AuraHeader(
                title: "Comprobante",
                eyebrow: isMembership ? "Renta del espacio" : "Servicio",
                subtitle: "Folio \(payment.folio)",
                onBack: onBack
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 24)

                    ZStack {
                        Circle()
                            .fill(payment.status.tone.background)
                            .frame(width: 92, height: 92)
                        Image(systemName: statusSymbol(payment.status))
                            .font(.system(size: 40, weight: .medium))
                            .foregroundStyle(payment.status.tone.foreground)
                    }

                    Spacer().frame(height: 18)

                    Text(headline(payment.status))
                        .font(AuraFont.headlineLarge())
                        .foregroundStyle(AuraPalette.navy)
                        .multilineTextAlignment(.center)

                    Spacer().frame(height: 8)

                    Text(message(payment.status, isMembership: isMembership))
                        .font(AuraFont.bodyLarge())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .multilineTextAlignment(.center)
                        .fixedSize(horizontal: false, vertical: true)

                    Spacer().frame(height: 14)

                    PaymentStatusPill(status: payment.status)

                    Spacer().frame(height: 24)

                    VStack(alignment: .leading, spacing: 0) {
                        HStack(alignment: .top, spacing: 12) {
                            VStack(alignment: .leading, spacing: 4) {
                                EyebrowText(text: "Concepto", color: AuraPalette.sand)
                                Text(payment.concept)
                                    .font(AuraFont.titleLarge())
                                    .foregroundStyle(AuraPalette.navy)
                                    .multilineTextAlignment(.leading)
                                    .fixedSize(horizontal: false, vertical: true)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)

                            AuraCardMark()
                                .frame(width: 52)
                        }
                        .padding(.bottom, 18)

                        ReceiptRow(label: "Folio", value: payment.folio)
                            .padding(.bottom, 10)
                        ReceiptRow(label: "Fecha", value: payment.dateLabel)
                            .padding(.bottom, 10)
                        ReceiptRow(label: "Método", value: payment.method)
                            .padding(.bottom, 10)
                        if let payerName = payment.payerName {
                            ReceiptRow(label: "Pagado por", value: payerName)
                                .padding(.bottom, 10)
                        }
                        ReceiptRow(
                            label: "Factura CFDI",
                            value: payment.invoiceRequested ? "Solicitada" : "No solicitada"
                        )
                        .padding(.bottom, 16)

                        AuraDivider()
                            .padding(.bottom, 16)

                        HStack {
                            Text("Total")
                                .font(AuraFont.titleLarge())
                                .foregroundStyle(AuraPalette.navy)
                                .frame(maxWidth: .infinity, alignment: .leading)
                            Text("\(Money.format(payment.amount)) MXN")
                                .font(AuraFont.headlineSmall())
                                .foregroundStyle(AuraPalette.blue)
                        }
                    }
                    .padding(20)
                    .frame(maxWidth: .infinity)
                    .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))

                    Spacer().frame(height: 16)

                    HStack(alignment: .top, spacing: 12) {
                        Image(systemName: "doc.text")
                            .font(.system(size: 18))
                            .foregroundStyle(AuraPalette.sand)
                        VStack(alignment: .leading, spacing: 2) {
                            Text(
                                isMembership
                                    ? "Cobro de renta · The Aura Den"
                                    : "Cobro de servicio · procesado por la app"
                            )
                            .font(AuraFont.titleSmall())
                            .foregroundStyle(AuraPalette.ink)
                            Text("\(AuraCopy.addressLine1), \(AuraCopy.addressLine2)")
                                .font(AuraFont.bodySmall())
                                .foregroundStyle(AuraPalette.inkMuted)
                                .fixedSize(horizontal: false, vertical: true)
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                    }
                    .padding(16)
                    .frame(maxWidth: .infinity)
                    .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))

                    Spacer().frame(height: 26)
                }
                .padding(.horizontal, 20)
            }
        } bottomAction: {
            VStack(spacing: 8) {
                AuraPrimaryButton(
                    title: payment.status == .failed ? "Intentar de nuevo" : "Listo"
                ) {
                    if payment.status == .failed {
                        onRetry(payment.kind)
                    } else {
                        onPrimary(payment.kind)
                    }
                }

                if payment.status != .failed {
                    AuraSecondaryButton(title: "Descargar comprobante") {}
                }
            }
        }
        .onAppear { AuraHaptics.success() }
    }

    private func statusSymbol(_ status: PaymentStatus) -> String {
        switch status {
        case .paid: "checkmark"
        case .pending: "hourglass"
        case .failed: "xmark"
        }
    }

    private func headline(_ status: PaymentStatus) -> String {
        switch status {
        case .paid: "Pago confirmado"
        case .pending: "Pago en proceso"
        case .failed: "El pago no se completó"
        }
    }

    private func message(_ status: PaymentStatus, isMembership: Bool) -> String {
        switch status {
        case .paid:
            isMembership
                ? "Tu membresía queda activa y tus horas ya están disponibles."
                : "Tu servicio quedó cubierto. Te esperamos en el estudio."
        case .pending:
            "Tu transferencia SPEI se confirma en un día hábil. Te avisamos en cuanto se acredite."
        case .failed:
            "El banco rechazó el cargo. Intenta con otro método de pago."
        }
    }
}

private struct ReceiptRow: View {
    let label: String
    let value: String

    var body: some View {
        HStack(spacing: 12) {
            Text(label)
                .font(AuraFont.bodyMedium())
                .foregroundStyle(AuraPalette.inkMuted)
                .frame(maxWidth: .infinity, alignment: .leading)
            Text(value)
                .font(AuraFont.titleSmall())
                .foregroundStyle(AuraPalette.ink)
                .multilineTextAlignment(.trailing)
        }
    }
}
