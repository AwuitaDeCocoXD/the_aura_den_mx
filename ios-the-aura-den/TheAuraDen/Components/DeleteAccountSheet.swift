import SwiftUI

/// Two-step account deletion confirmation. Visual only in the demo: nothing is erased,
/// but the flow mirrors what the store guidelines require.
struct DeleteAccountSheet: View {
    let accountName: String
    let onConfirmed: () -> Void

    @Environment(\.dismiss) private var dismiss
    @State private var isSecondStep = false
    @State private var hasAcknowledged = false

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            ZStack {
                Circle()
                    .fill(AuraPalette.redSoft)
                    .frame(width: 56, height: 56)
                Image(systemName: isSecondStep ? "trash.fill" : "exclamationmark.triangle.fill")
                    .font(.system(size: 24))
                    .foregroundStyle(AuraPalette.red)
            }
            .frame(maxWidth: .infinity)
            .padding(.top, 26)
            .padding(.bottom, 16)

            Text(isSecondStep ? "Confirma una vez más" : "¿Eliminar tu cuenta?")
                .font(AuraFont.headlineSmall())
                .foregroundStyle(AuraPalette.navy)
                .frame(maxWidth: .infinity, alignment: .center)
                .padding(.bottom, 14)

            if isSecondStep {
                secondStep
            } else {
                firstStep
            }

            Spacer(minLength: 18)

            VStack(spacing: 8) {
                if isSecondStep {
                    AuraPrimaryButton(
                        title: "Eliminar definitivamente",
                        tint: AuraPalette.red,
                        isEnabled: hasAcknowledged
                    ) {
                        dismiss()
                        onConfirmed()
                    }
                } else {
                    AuraPrimaryButton(title: "Continuar", tint: AuraPalette.red) {
                        withAnimation(.easeInOut(duration: 0.22)) { isSecondStep = true }
                    }
                }

                Button("Mejor no") {
                    AuraHaptics.tap()
                    dismiss()
                }
                .font(AuraFont.labelLarge())
                .foregroundStyle(AuraPalette.blue)
                .frame(maxWidth: .infinity)
                .frame(height: 48)
            }
        }
        .padding(.horizontal, 22)
        .padding(.bottom, 12)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .background(AuraPalette.white)
        .presentationDetents([.height(isSecondStep ? 430 : 480)])
        .presentationDragIndicator(.hidden)
    }

    private var firstStep: some View {
        VStack(alignment: .leading, spacing: 0) {
            Text("Si eliminas tu cuenta perderás de forma permanente:")
                .font(AuraFont.bodyMedium())
                .foregroundStyle(AuraPalette.inkMuted)
                .fixedSize(horizontal: false, vertical: true)
                .padding(.bottom, 12)

            ForEach(LegalCopy.deletionLosses, id: \.self) { item in
                HStack(alignment: .top, spacing: 10) {
                    Circle()
                        .fill(AuraPalette.red)
                        .frame(width: 5, height: 5)
                        .padding(.top, 7)
                    Text(item)
                        .font(AuraFont.bodyMedium())
                        .foregroundStyle(AuraPalette.ink)
                        .fixedSize(horizontal: false, vertical: true)
                }
                .padding(.vertical, 4)
            }

            Text("Si tienes una membresía vigente, te recomendamos hablar con recepción antes de continuar.")
                .font(AuraFont.bodySmall())
                .foregroundStyle(AuraPalette.inkMuted)
                .fixedSize(horizontal: false, vertical: true)
                .padding(.top, 12)
        }
    }

    private var secondStep: some View {
        VStack(alignment: .leading, spacing: 0) {
            Text("Estás por eliminar la cuenta de \(accountName). Esta acción es permanente y no podemos recuperar la información después.")
                .font(AuraFont.bodyMedium())
                .foregroundStyle(AuraPalette.inkMuted)
                .fixedSize(horizontal: false, vertical: true)
                .padding(.bottom, 16)

            Button {
                AuraHaptics.tap()
                hasAcknowledged.toggle()
            } label: {
                HStack(spacing: 12) {
                    ZStack {
                        RoundedRectangle(cornerRadius: 8)
                            .fill(hasAcknowledged ? AuraPalette.red : AuraPalette.white)
                            .frame(width: 24, height: 24)
                        RoundedRectangle(cornerRadius: 8)
                            .strokeBorder(
                                hasAcknowledged ? AuraPalette.red : AuraPalette.inkFaint,
                                lineWidth: 1.5
                            )
                            .frame(width: 24, height: 24)
                        if hasAcknowledged {
                            Image(systemName: "checkmark")
                                .font(.system(size: 13, weight: .bold))
                                .foregroundStyle(AuraPalette.white)
                        }
                    }
                    Text("Entiendo que esta acción no se puede deshacer")
                        .font(AuraFont.bodyMedium())
                        .foregroundStyle(AuraPalette.ink)
                        .multilineTextAlignment(.leading)
                        .fixedSize(horizontal: false, vertical: true)
                    Spacer(minLength: 0)
                }
                .padding(14)
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(
                    hasAcknowledged ? AuraPalette.redSoft : AuraPalette.white,
                    in: .rect(cornerRadius: 16)
                )
                .overlay {
                    RoundedRectangle(cornerRadius: 16)
                        .strokeBorder(
                            hasAcknowledged ? AuraPalette.red.opacity(0.4) : AuraPalette.divider,
                            lineWidth: 1
                        )
                }
            }
            .buttonStyle(PressableButtonStyle())
        }
    }
}
