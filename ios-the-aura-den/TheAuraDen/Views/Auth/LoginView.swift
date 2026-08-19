import SwiftUI

/// Simple sign in that drops the specialist straight into her dashboard.
struct LoginView: View {
    let onBack: () -> Void
    let onSignedIn: () -> Void
    let onCreateAccount: () -> Void
    let onCreateGuestAccount: () -> Void

    @State private var identifier: String = "juanita@correo.com"
    @State private var password: String = "auraden2025"

    var body: some View {
        AuraDetailScaffold {
            AuraHeader(
                title: "Iniciar sesión",
                eyebrow: "Bienvenida de vuelta",
                subtitle: "Entra para ver tu agenda y tu espacio",
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
                    Spacer().frame(height: 26)

                    AuraTextField(
                        title: "Correo o celular",
                        placeholder: "juanita@correo.com",
                        text: $identifier,
                        keyboard: .emailAddress
                    )

                    Spacer().frame(height: 18)

                    AuraTextField(
                        title: "Contraseña",
                        placeholder: "Tu contraseña",
                        text: $password,
                        isSecure: true
                    )

                    Spacer().frame(height: 6)

                    Button("¿Olvidaste tu contraseña?") {}
                        .font(AuraFont.labelMedium())
                        .foregroundStyle(AuraPalette.blue)
                        .frame(maxWidth: .infinity, alignment: .trailing)
                        .padding(.vertical, 10)

                    Spacer().frame(height: 14)

                    Text("¿Aún no tienes cuenta?")
                        .font(AuraFont.bodyMedium())
                        .foregroundStyle(AuraPalette.inkMuted)

                    Button("Crear cuenta de especialista", action: onCreateAccount)
                        .font(AuraFont.labelLarge())
                        .foregroundStyle(AuraPalette.blue)
                        .padding(.vertical, 10)

                    Button("Crear cuenta de invitada", action: onCreateGuestAccount)
                        .font(AuraFont.labelLarge())
                        .foregroundStyle(AuraPalette.blue)
                        .padding(.vertical, 10)

                    Spacer().frame(height: 20)
                }
                .padding(.horizontal, 20)
            }
        } bottomAction: {
            AuraPrimaryButton(title: "Entrar", action: onSignedIn)
        }
    }
}
