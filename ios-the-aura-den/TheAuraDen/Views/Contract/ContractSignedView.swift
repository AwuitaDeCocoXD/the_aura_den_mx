import SwiftUI

/// Step 3: confirmation with the assigned folio and the timestamp of the signature.
struct ContractSignedView: View {
    let contract: SignedContract
    let onContinue: () -> Void

    @State private var visible = false

    var body: some View {
        ZStack {
            AuraPalette.blue.ignoresSafeArea()

            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 48)

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

                    Text("¡Contrato firmado!")
                        .font(AuraFont.displayMedium())
                        .foregroundStyle(AuraPalette.white)
                        .multilineTextAlignment(.center)

                    Spacer().frame(height: 10)

                    Text("Ya puedes reservar tu espacio en The Aura Den. Guardamos una copia en tu perfil.")
                        .font(AuraFont.bodyLarge())
                        .foregroundStyle(AuraPalette.white.opacity(0.85))
                        .multilineTextAlignment(.center)
                        .fixedSize(horizontal: false, vertical: true)

                    Spacer().frame(height: 26)

                    VStack(spacing: 14) {
                        ContractFactRow(
                            symbol: "number",
                            label: "Folio del contrato",
                            value: contract.folio
                        )
                        Rectangle()
                            .fill(AuraPalette.white.opacity(0.18))
                            .frame(height: 1)
                        ContractFactRow(
                            symbol: "clock.fill",
                            label: "Fecha y hora de firma",
                            value: "\(contract.dateLabel) · \(contract.timeLabel)"
                        )
                    }
                    .padding(.horizontal, 20)
                    .padding(.vertical, 18)
                    .frame(maxWidth: .infinity)
                    .background(AuraPalette.navy.opacity(0.45), in: .rect(cornerRadius: 24))

                    Spacer().frame(height: 14)

                    Text("Firmado electrónicamente por \(contract.signerName)")
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.white.opacity(0.7))
                        .multilineTextAlignment(.center)

                    Spacer().frame(height: 36)

                    AuraPrimaryButton(
                        title: "Continuar a mi cuenta",
                        tint: AuraPalette.white,
                        foreground: AuraPalette.blue,
                        action: onContinue
                    )

                    Spacer().frame(height: 24)
                }
                .padding(.horizontal, 28)
            }
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

private struct ContractFactRow: View {
    let symbol: String
    let label: String
    let value: String

    var body: some View {
        HStack(spacing: 14) {
            ZStack {
                RoundedRectangle(cornerRadius: 13)
                    .fill(AuraPalette.white.opacity(0.12))
                    .frame(width: 38, height: 38)
                Image(systemName: symbol)
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundStyle(AuraPalette.yellow)
            }
            VStack(alignment: .leading, spacing: 2) {
                Text(label)
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(AuraPalette.white.opacity(0.7))
                Text(value)
                    .font(AuraFont.titleMedium())
                    .foregroundStyle(AuraPalette.yellow)
                    .fixedSize(horizontal: false, vertical: true)
            }
            Spacer(minLength: 0)
        }
    }
}
