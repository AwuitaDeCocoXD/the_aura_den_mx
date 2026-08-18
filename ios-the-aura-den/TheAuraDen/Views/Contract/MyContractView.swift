import SwiftUI

/// Summary of the signed agreement, reachable from the specialist profile.
struct MyContractView: View {
    let onBack: () -> Void
    let onReadContract: () -> Void
    let onSignContract: () -> Void

    @Environment(DemoStore.self) private var store
    @State private var pdfRequested = false

    var body: some View {
        let contract = store.signedContract

        AuraDetailScaffold {
            AuraHeader(
                title: "Mi contrato",
                eyebrow: "Renta de espacio",
                subtitle: contract != nil
                    ? "Vigente · Membresía \(store.activePlan.name)"
                    : "Aún no has firmado tu contrato",
                onBack: onBack,
                content: {
                    StatusPill(
                        text: contract != nil ? "Contrato firmado" : "Pendiente de firma",
                        foreground: AuraPalette.navy,
                        background: contract != nil ? AuraPalette.yellow : AuraPalette.white,
                        symbol: contract != nil ? "checkmark.seal.fill" : "signature"
                    )
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 20)

                    if let contract {
                        ContractHeroCard(contract: contract)

                        Spacer().frame(height: 16)

                        VStack(spacing: 0) {
                            InfoRow(
                                label: "Folio del contrato",
                                value: contract.folio,
                                symbol: "number"
                            )
                            AuraDivider().padding(.horizontal, 18)
                            InfoRow(
                                label: "Fecha de firma",
                                value: "\(contract.dateLabel) · \(contract.timeLabel)",
                                symbol: "calendar"
                            )
                            AuraDivider().padding(.horizontal, 18)
                            InfoRow(
                                label: "Firmado por",
                                value: contract.signerName,
                                symbol: "signature"
                            )
                            AuraDivider().padding(.horizontal, 18)
                            InfoRow(
                                label: "Documento",
                                value: ContractCopy.title,
                                symbol: "doc.text.fill",
                                showsChevron: true,
                                action: onReadContract
                            )
                        }
                        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))

                        Spacer().frame(height: 16)

                        Button {
                            AuraHaptics.tap()
                            withAnimation(.easeInOut(duration: 0.25)) { pdfRequested = true }
                        } label: {
                            HStack(spacing: 14) {
                                ZStack {
                                    RoundedRectangle(cornerRadius: 14)
                                        .fill(AuraPalette.sand.opacity(0.35))
                                        .frame(width: 42, height: 42)
                                    Image(systemName: "arrow.down.circle.fill")
                                        .font(.system(size: 19))
                                        .foregroundStyle(AuraPalette.navy)
                                }
                                VStack(alignment: .leading, spacing: 2) {
                                    Text("Descargar PDF")
                                        .font(AuraFont.titleMedium())
                                        .fontWeight(.semibold)
                                        .foregroundStyle(AuraPalette.ink)
                                    Text("Copia del contrato con tu firma")
                                        .font(AuraFont.bodySmall())
                                        .foregroundStyle(AuraPalette.inkMuted)
                                }
                                Spacer(minLength: 0)
                            }
                            .padding(18)
                            .frame(maxWidth: .infinity)
                            .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))
                        }
                        .buttonStyle(PressableButtonStyle())

                        if pdfRequested {
                            HStack(spacing: 10) {
                                Image(systemName: "info.circle.fill")
                                    .font(.system(size: 14))
                                    .foregroundStyle(AuraPalette.green)
                                Text("La descarga en PDF estará disponible en la versión final de la app.")
                                    .font(AuraFont.bodySmall())
                                    .foregroundStyle(AuraPalette.green)
                                    .fixedSize(horizontal: false, vertical: true)
                                Spacer(minLength: 0)
                            }
                            .padding(.horizontal, 14)
                            .padding(.vertical, 12)
                            .frame(maxWidth: .infinity)
                            .background(AuraPalette.greenSoft, in: .rect(cornerRadius: 16))
                            .padding(.top, 10)
                            .transition(.opacity)
                        }

                        Spacer().frame(height: 20)

                        Text("Este contrato es una simulación visual sin validez legal. \(AuraCopy.brandName) · \(AuraCopy.addressLine1), \(AuraCopy.addressLine2)")
                            .font(AuraFont.bodySmall())
                            .foregroundStyle(AuraPalette.inkMuted)
                            .fixedSize(horizontal: false, vertical: true)
                            .frame(maxWidth: .infinity, alignment: .leading)
                    } else {
                        VStack(alignment: .leading, spacing: 0) {
                            HStack(spacing: 10) {
                                Image(systemName: "exclamationmark.circle.fill")
                                    .font(.system(size: 18))
                                    .foregroundStyle(AuraPalette.amber)
                                Text("Contrato pendiente")
                                    .font(AuraFont.titleMedium())
                                    .fontWeight(.semibold)
                                    .foregroundStyle(AuraPalette.amber)
                            }
                            .padding(.bottom, 8)

                            Text("Para poder reservar una estación necesitas leer y firmar el contrato de renta de espacio. Te toma menos de dos minutos.")
                                .font(AuraFont.bodyMedium())
                                .foregroundStyle(AuraPalette.ink)
                                .fixedSize(horizontal: false, vertical: true)
                        }
                        .padding(20)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .background(AuraPalette.amberSoft, in: .rect(cornerRadius: AuraRadius.card))
                    }

                    Spacer().frame(height: 26)
                }
                .padding(.horizontal, 20)
            }
        } bottomAction: {
            if contract != nil {
                AuraSecondaryButton(
                    title: "Ver contrato completo",
                    symbol: "doc.text.fill",
                    action: onReadContract
                )
            } else {
                AuraPrimaryButton(
                    title: "Firmar contrato",
                    symbol: "signature",
                    action: onSignContract
                )
            }
        }
    }
}

private struct ContractHeroCard: View {
    let contract: SignedContract

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 14) {
                ZStack {
                    Circle()
                        .fill(AuraPalette.greenSoft)
                        .frame(width: 46, height: 46)
                    Image(systemName: "checkmark.seal.fill")
                        .font(.system(size: 21))
                        .foregroundStyle(AuraPalette.green)
                }
                VStack(alignment: .leading, spacing: 3) {
                    EyebrowText(text: "Estado")
                    Text("Contrato firmado")
                        .font(AuraFont.headlineSmall())
                        .foregroundStyle(AuraPalette.navy)
                }
                Spacer(minLength: 0)
            }
            .padding(.bottom, 18)

            VStack(alignment: .leading, spacing: 0) {
                EyebrowText(text: "Firmado por", color: AuraPalette.sand)
                    .padding(.bottom, 6)
                Text(contract.signerName)
                    .font(AuraFont.displaySmall())
                    .foregroundStyle(AuraPalette.navy)
                    .padding(.bottom, 6)
                Text("\(contract.dateLabel) · \(contract.timeLabel)")
                    .font(AuraFont.bodyMedium())
                    .foregroundStyle(AuraPalette.inkMuted)
            }
            .padding(.horizontal, 18)
            .padding(.vertical, 16)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(
                AuraPalette.sandSoft.opacity(0.5),
                in: .rect(cornerRadius: 18)
            )
            .padding(.bottom, 14)

            Text("Folio \(contract.folio)")
                .font(AuraFont.labelLarge())
                .foregroundStyle(AuraPalette.blue)
        }
        .padding(20)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
    }
}
