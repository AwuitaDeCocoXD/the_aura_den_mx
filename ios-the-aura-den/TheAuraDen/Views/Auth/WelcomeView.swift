import SwiftUI

/// Brand entrance: full-bleed blue canvas, centered logotype and two large actions.
struct WelcomeView: View {
    let onCreateAccount: () -> Void
    let onSignIn: () -> Void

    var body: some View {
        ZStack {
            LinearGradient(
                stops: [
                    .init(color: AuraPalette.blue, location: 0),
                    .init(color: AuraPalette.blue, location: 0.5),
                    .init(color: AuraPalette.navyDeep, location: 1)
                ],
                startPoint: .top,
                endPoint: .bottom
            )
            .ignoresSafeArea()

            BrandTexture()
                .ignoresSafeArea()

            VStack(spacing: 0) {
                Spacer(minLength: 0)

                AuraLogo(size: .large)

                Spacer().frame(height: 20)

                Text(AuraCopy.tagline)
                    .font(AuraFont.labelMedium())
                    .tracking(0.4)
                    .foregroundStyle(AuraPalette.white.opacity(0.72))
                    .multilineTextAlignment(.center)

                Spacer(minLength: 0)

                AuraPrimaryButton(
                    title: "Crear cuenta",
                    tint: AuraPalette.white,
                    foreground: AuraPalette.blue,
                    action: onCreateAccount
                )

                Spacer().frame(height: 14)

                AuraPrimaryButton(
                    title: "Iniciar sesión",
                    tint: AuraPalette.white.opacity(0.14),
                    foreground: AuraPalette.white,
                    action: onSignIn
                )

                Spacer().frame(height: 26)

                Text("Un espacio para hacer crecer tu talento")
                    .font(AuraFont.bodyMedium())
                    .foregroundStyle(AuraPalette.yellow.opacity(0.9))
                    .multilineTextAlignment(.center)
                    .frame(maxWidth: .infinity)

                Spacer().frame(height: 28)
            }
            .padding(.horizontal, 28)
        }
    }
}
