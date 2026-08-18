import SwiftUI

/// Step 2: confirm the legal name and draw the signature with a finger.
struct ContractSignView: View {
    let onBack: () -> Void
    let onSign: (String) -> Void

    @Environment(DemoStore.self) private var store
    @State private var name: String = ""
    @State private var strokes: [[CGPoint]] = []

    private var hasSignature: Bool { !strokes.isEmpty }
    private var canSign: Bool {
        hasSignature && !name.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
    }

    var body: some View {
        AuraDetailScaffold {
            AuraHeader(
                title: "Firma tu contrato",
                eyebrow: "Paso 2 de 2",
                subtitle: "Confirma tu nombre y firma con el dedo",
                onBack: onBack
            )
        } content: {
            VStack(spacing: 0) {
                Spacer().frame(height: 20)

                AuraTextField(
                    title: "Nombre completo",
                    placeholder: "Juanita Cruz",
                    text: $name
                )

                Spacer().frame(height: 20)

                VStack(alignment: .leading, spacing: 0) {
                    HStack(spacing: 8) {
                        Image(systemName: "signature")
                            .font(.system(size: 14))
                            .foregroundStyle(AuraPalette.blue)
                        EyebrowText(text: "Tu firma")
                        Spacer()
                        Button("Borrar") {
                            AuraHaptics.tap()
                            strokes = []
                        }
                        .font(AuraFont.labelLarge())
                        .foregroundStyle(hasSignature ? AuraPalette.red : AuraPalette.inkFaint)
                        .disabled(!hasSignature)
                    }
                    .padding(.bottom, 10)

                    ZStack {
                        RoundedRectangle(cornerRadius: 18)
                            .fill(AuraPalette.cream)

                        VStack {
                            Spacer()
                            Rectangle()
                                .fill(AuraPalette.divider)
                                .frame(height: 1)
                                .padding(.horizontal, 24)
                                .padding(.bottom, 44)
                        }

                        if !hasSignature {
                            Text("Firma aquí con el dedo")
                                .font(AuraFont.bodyMedium())
                                .foregroundStyle(AuraPalette.inkFaint)
                        }

                        SignaturePad(strokes: $strokes)
                    }
                    .frame(height: 210)
                    .clipShape(.rect(cornerRadius: 18))

                    Text(
                        hasSignature
                            ? "Si no te gustó cómo quedó, toca Borrar y vuelve a intentarlo."
                            : "Dibuja tu firma sobre la línea. Puedes borrar y repetirla las veces que quieras."
                    )
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(AuraPalette.inkMuted)
                    .fixedSize(horizontal: false, vertical: true)
                    .padding(.top, 10)
                }
                .padding(16)
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))

                Spacer().frame(height: 16)

                Text("Al firmar aceptas el \(ContractCopy.title.lowercased()) de The Aura Den en su versión vigente.")
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(AuraPalette.navy)
                    .fixedSize(horizontal: false, vertical: true)
                    .frame(maxWidth: .infinity, alignment: .leading)

                Spacer(minLength: 0)
            }
            .padding(.horizontal, 20)
        } bottomAction: {
            VStack(spacing: 8) {
                AuraPrimaryButton(
                    title: "Firmar contrato",
                    isEnabled: canSign,
                    action: {
                        onSign(name.trimmingCharacters(in: .whitespacesAndNewlines))
                    }
                )
                HStack(spacing: 6) {
                    Image(systemName: "lock.fill")
                        .font(.system(size: 11))
                        .foregroundStyle(AuraPalette.inkFaint)
                    Text("Tu firma queda asociada al folio del contrato")
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                }
            }
        }
        .onAppear {
            if name.isEmpty { name = store.contractSignerName }
        }
    }
}
