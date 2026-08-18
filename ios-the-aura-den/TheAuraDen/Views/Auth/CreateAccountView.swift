import SwiftUI

/// Specialist sign up. Data is prefilled so the demo reads like a real account.
struct CreateAccountView: View {
    let onBack: () -> Void
    let onRegistered: (String) -> Void
    let onSignIn: () -> Void
    let onOpenLegal: () -> Void

    @State private var name: String = "Juanita Cruz"
    @State private var email: String = "juanita@correo.com"
    @State private var phone: String = "+52 55 1234 5678"
    @State private var specialty: String = DemoData.specialties[0]

    var body: some View {
        AuraDetailScaffold {
            AuraHeader(
                title: "Crear cuenta",
                eyebrow: "Especialista",
                subtitle: "Al terminar firmarás tu contrato de renta",
                onBack: onBack,
                content: {
                    AuraLogo(size: .compact)
                        .padding(.bottom, 2)
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 24)

                    AuraTextField(
                        title: "Nombre completo",
                        placeholder: "Juanita Cruz",
                        text: $name
                    )

                    Spacer().frame(height: 18)

                    AuraTextField(
                        title: "Correo electrónico",
                        placeholder: "juanita@correo.com",
                        text: $email,
                        keyboard: .emailAddress
                    )

                    Spacer().frame(height: 18)

                    AuraTextField(
                        title: "Número de celular",
                        placeholder: "+52 55 1234 5678",
                        text: $phone,
                        keyboard: .phonePad
                    )

                    Spacer().frame(height: 18)

                    AuraDropdownField(
                        title: "Especialidad",
                        selection: $specialty,
                        options: DemoData.specialties
                    )

                    Spacer().frame(height: 22)

                    VStack(spacing: 0) {
                        Text("Al registrarte aceptas nuestros")
                            .font(AuraFont.bodySmall())
                            .foregroundStyle(AuraPalette.inkMuted)
                            .multilineTextAlignment(.center)
                        Button("Términos y condiciones y Aviso de privacidad") {
                            AuraHaptics.tap()
                            onOpenLegal()
                        }
                        .font(AuraFont.labelLarge())
                        .foregroundStyle(AuraPalette.blue)
                        .multilineTextAlignment(.center)
                        .padding(.vertical, 10)
                    }
                    .frame(maxWidth: .infinity)

                    Spacer().frame(height: 10)

                    VStack(spacing: 0) {
                        Text("¿Ya tienes cuenta?")
                            .font(AuraFont.bodyMedium())
                            .foregroundStyle(AuraPalette.inkMuted)
                        Button("Inicia sesión", action: onSignIn)
                            .font(AuraFont.labelLarge())
                            .foregroundStyle(AuraPalette.blue)
                            .padding(.vertical, 10)
                    }
                    .frame(maxWidth: .infinity)

                    Spacer().frame(height: 16)
                }
                .padding(.horizontal, 20)
            }
        } bottomAction: {
            AuraPrimaryButton(title: "Registrarme") {
                onRegistered(name.trimmingCharacters(in: .whitespacesAndNewlines))
            }
        }
    }
}
