import SwiftUI

/// The two kinds of account the studio hands out.
nonisolated enum SignUpMode: Hashable {
    case specialist
    case guest
}

/// Sign up for both audiences. The specialist path continues into the rental
/// agreement; the guest path only collects contact data and lets her book.
struct CreateAccountView: View {
    let initialMode: SignUpMode
    let onBack: () -> Void
    let onRegistered: (String) -> Void
    let onRegisteredGuest: (String) -> Void
    let onSignIn: () -> Void
    let onOpenLegal: () -> Void

    @State private var mode: SignUpMode
    @State private var specialistName: String = "Juanita Cruz"
    @State private var specialistEmail: String = "juanita@correo.com"
    @State private var specialistPhone: String = "+52 55 1234 5678"
    @State private var specialty: String = DemoData.specialties[0]
    @State private var guestName: String = ""
    @State private var guestEmail: String = ""
    @State private var guestPhone: String = ""

    init(
        initialMode: SignUpMode,
        onBack: @escaping () -> Void,
        onRegistered: @escaping (String) -> Void,
        onRegisteredGuest: @escaping (String) -> Void,
        onSignIn: @escaping () -> Void,
        onOpenLegal: @escaping () -> Void
    ) {
        self.initialMode = initialMode
        self.onBack = onBack
        self.onRegistered = onRegistered
        self.onRegisteredGuest = onRegisteredGuest
        self.onSignIn = onSignIn
        self.onOpenLegal = onOpenLegal
        _mode = State(initialValue: initialMode)
    }

    private var isGuest: Bool { mode == .guest }

    private var canSubmit: Bool {
        let value = isGuest ? guestName : specialistName
        return !value.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
    }

    var body: some View {
        AuraDetailScaffold {
            AuraHeader(
                title: isGuest ? "Crear cuenta de invitada" : "Crear cuenta",
                eyebrow: isGuest ? "Invitada" : "Especialista",
                subtitle: isGuest
                    ? "En un minuto ya puedes agendar tu primera cita"
                    : "Al terminar firmarás tu contrato de renta",
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
                    Spacer().frame(height: 20)

                    HStack(spacing: 12) {
                        ModeCard(
                            label: "Soy especialista",
                            detail: "Rento un espacio",
                            symbol: "scissors",
                            isSelected: !isGuest
                        ) {
                            withAnimation(.easeInOut(duration: 0.22)) { mode = .specialist }
                        }
                        ModeCard(
                            label: "Soy invitada",
                            detail: "Vengo por un servicio",
                            symbol: "calendar.badge.checkmark",
                            isSelected: isGuest
                        ) {
                            withAnimation(.easeInOut(duration: 0.22)) { mode = .guest }
                        }
                    }

                    Spacer().frame(height: 24)

                    if isGuest {
                        guestForm
                    } else {
                        specialistForm
                    }

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
            AuraPrimaryButton(
                title: isGuest ? "Crear mi cuenta" : "Registrarme",
                isEnabled: canSubmit
            ) {
                if isGuest {
                    onRegisteredGuest(guestName.trimmingCharacters(in: .whitespacesAndNewlines))
                } else {
                    onRegistered(specialistName.trimmingCharacters(in: .whitespacesAndNewlines))
                }
            }
        }
    }

    private var guestForm: some View {
        VStack(spacing: 0) {
            AuraTextField(
                title: "Nombre completo",
                placeholder: "Ana Sofía Herrera",
                text: $guestName
            )

            Spacer().frame(height: 18)

            AuraTextField(
                title: "Correo electrónico",
                placeholder: "ana@correo.com",
                text: $guestEmail,
                keyboard: .emailAddress
            )

            Spacer().frame(height: 18)

            AuraTextField(
                title: "Número de celular",
                placeholder: "+52 55 0000 0000",
                text: $guestPhone,
                keyboard: .phonePad
            )

            Spacer().frame(height: 16)

            Text(
                "Las invitadas no firman contrato: con estos datos ya puedes agendar con "
                    + "cualquiera de nuestras especialistas."
            )
            .font(AuraFont.bodySmall())
            .foregroundStyle(AuraPalette.inkMuted)
            .fixedSize(horizontal: false, vertical: true)
            .frame(maxWidth: .infinity, alignment: .leading)
        }
    }

    private var specialistForm: some View {
        VStack(spacing: 0) {
            AuraTextField(
                title: "Nombre completo",
                placeholder: "Juanita Cruz",
                text: $specialistName
            )

            Spacer().frame(height: 18)

            AuraTextField(
                title: "Correo electrónico",
                placeholder: "juanita@correo.com",
                text: $specialistEmail,
                keyboard: .emailAddress
            )

            Spacer().frame(height: 18)

            AuraTextField(
                title: "Número de celular",
                placeholder: "+52 55 1234 5678",
                text: $specialistPhone,
                keyboard: .phonePad
            )

            Spacer().frame(height: 18)

            AuraDropdownField(
                title: "Especialidad",
                selection: $specialty,
                options: DemoData.specialties
            )
        }
    }
}

/// One of the two account paths, shown as a large tappable tile.
private struct ModeCard: View {
    let label: String
    let detail: String
    let symbol: String
    let isSelected: Bool
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            VStack(alignment: .leading, spacing: 0) {
                ZStack {
                    RoundedRectangle(cornerRadius: 13)
                        .fill(isSelected ? AuraPalette.yellow : AuraPalette.blue.opacity(0.09))
                        .frame(width: 38, height: 38)
                    Image(systemName: symbol)
                        .font(.system(size: 17, weight: .medium))
                        .foregroundStyle(isSelected ? AuraPalette.navy : AuraPalette.blue)
                }
                .padding(.bottom, 12)

                Text(label)
                    .font(AuraFont.titleMedium())
                    .foregroundStyle(isSelected ? AuraPalette.white : AuraPalette.navy)
                    .fixedSize(horizontal: false, vertical: true)
                    .padding(.bottom, 2)

                Text(detail)
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(
                        isSelected ? AuraPalette.white.opacity(0.8) : AuraPalette.ink.opacity(0.6)
                    )
                    .fixedSize(horizontal: false, vertical: true)
            }
            .padding(.horizontal, 14)
            .padding(.vertical, 16)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(
                isSelected ? AuraPalette.blue : AuraPalette.white,
                in: .rect(cornerRadius: 20)
            )
            .overlay {
                RoundedRectangle(cornerRadius: 20)
                    .stroke(
                        isSelected ? AuraPalette.blue : AuraPalette.inkMuted.opacity(0.25),
                        lineWidth: 1
                    )
            }
            .contentShape(.rect)
        }
        .buttonStyle(.plain)
    }
}
