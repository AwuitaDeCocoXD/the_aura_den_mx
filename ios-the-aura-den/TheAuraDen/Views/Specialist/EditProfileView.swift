import SwiftUI

/// Edit the specialist's public data.
struct EditProfileView: View {
    let onBack: () -> Void

    @Environment(DemoStore.self) private var store

    @State private var name: String = ""
    @State private var phone: String = ""
    @State private var email: String = ""
    @State private var specialty: String = ""

    var body: some View {
        AuraDetailScaffold {
            AuraHeader(
                title: "Editar perfil",
                eyebrow: "Tu presencia en el estudio",
                onBack: onBack
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 22)

                    VStack(spacing: 0) {
                        AuraAvatar(
                            imageURL: store.profile.imageURL,
                            size: 104,
                            ringColor: .clear
                        )
                        Button("Cambiar foto") {}
                            .font(AuraFont.labelLarge())
                            .foregroundStyle(AuraPalette.blue)
                            .padding(.vertical, 10)
                    }
                    .frame(maxWidth: .infinity)

                    Spacer().frame(height: 14)

                    AuraTextField(title: "Nombre completo", text: $name)

                    Spacer().frame(height: 18)

                    AuraTextField(title: "Número de celular", text: $phone, keyboard: .phonePad)

                    Spacer().frame(height: 18)

                    AuraTextField(title: "Correo electrónico", text: $email, keyboard: .emailAddress)

                    Spacer().frame(height: 18)

                    AuraDropdownField(
                        title: "Especialidad",
                        selection: $specialty,
                        options: DemoData.specialties
                    )

                    Spacer().frame(height: 26)
                }
                .padding(.horizontal, 20)
            }
        } bottomAction: {
            AuraPrimaryButton(title: "Guardar cambios") {
                store.updateProfile(
                    name: name,
                    phone: phone,
                    email: email,
                    specialty: specialty
                )
                onBack()
            }
        }
        .onAppear {
            if name.isEmpty {
                name = store.profile.name
                phone = store.profile.phone
                email = store.profile.email
                specialty = store.profile.specialty
            }
        }
    }
}
