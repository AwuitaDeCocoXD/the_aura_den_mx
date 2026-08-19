import SwiftUI

/// Client account screen: her data, her legal documents and account deletion.
struct ClientProfileView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    let onBack: () -> Void
    let onOpenLegal: () -> Void
    let onSignOut: () -> Void
    let onDeleteAccount: () -> Void
    let onOpenRoleSwitcher: () -> Void

    @Environment(DemoStore.self) private var store
    @State private var showDeleteSheet = false

    var body: some View {
        let completed = store.clientHistory.count { $0.status == .completed }

        AuraTabScaffold(
            role: .client,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: store.guestName,
                eyebrow: "Mi cuenta",
                subtitle: "Invitada de \(AuraCopy.brandName)",
                onBack: onBack,
                trailing: {
                    ZStack {
                        Circle()
                            .fill(AuraPalette.white.opacity(0.16))
                            .frame(width: 52, height: 52)
                        Text(store.guestInitials)
                            .font(AuraFont.titleMedium())
                            .foregroundStyle(AuraPalette.yellow)
                    }
                }
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 18)

                    HStack(alignment: .top, spacing: 12) {
                        MetricTile(
                            value: "\(completed)",
                            label: "Servicios recibidos",
                            background: AuraPalette.cream
                        )
                        MetricTile(
                            value: "\(store.clientUpcoming.count)",
                            label: "Citas próximas"
                        )
                    }

                    Spacer().frame(height: 16)

                    VStack(spacing: 0) {
                        InfoRow(
                            label: "Celular",
                            value: "+52 55 8899 3311",
                            symbol: "phone.fill"
                        )
                        AuraDivider().padding(.horizontal, 18)
                        InfoRow(
                            label: "Correo",
                            value: "lucia@correo.com",
                            symbol: "envelope.fill"
                        )
                    }
                    .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))

                    Spacer().frame(height: 16)

                    HStack(spacing: 14) {
                        Image(systemName: "heart.fill")
                            .font(.system(size: 20))
                            .foregroundStyle(AuraPalette.sand)
                        VStack(alignment: .leading, spacing: 5) {
                            EyebrowText(text: "Mi especialista favorita", color: AuraPalette.sand)
                            Text("Juanita Cruz")
                                .font(AuraFont.titleMedium())
                                .foregroundStyle(AuraPalette.navy)
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                        StatusPill(
                            text: "4 visitas",
                            foreground: AuraPalette.navy,
                            background: AuraPalette.yellow
                        )
                    }
                    .padding(20)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))

                    Spacer().frame(height: 16)

                    VStack(spacing: 0) {
                        InfoRow(
                            label: "Legal",
                            value: "Términos y aviso de privacidad",
                            symbol: "doc.text.fill",
                            showsChevron: true,
                            action: onOpenLegal
                        )
                        AuraDivider().padding(.horizontal, 18)
                        InfoRow(
                            label: "Modo demo",
                            value: "Cambiar de vista",
                            symbol: "arrow.left.arrow.right",
                            showsChevron: true,
                            action: onOpenRoleSwitcher
                        )
                    }
                    .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))

                    Spacer().frame(height: 22)

                    AuraSecondaryButton(title: "Cerrar sesión", action: onSignOut)

                    Button("Eliminar mi cuenta") {
                        AuraHaptics.tap()
                        showDeleteSheet = true
                    }
                    .font(AuraFont.labelLarge())
                    .foregroundStyle(AuraPalette.red)
                    .frame(maxWidth: .infinity)
                    .frame(height: 48)
                    .padding(.top, 6)

                    Text("Al eliminar tu cuenta se borra tu historial de citas y tus datos personales de The Aura Den.")
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .multilineTextAlignment(.center)
                        .fixedSize(horizontal: false, vertical: true)

                    Text(AuraCopy.brandName)
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.ink.opacity(0.35))
                        .padding(.top, 12)

                    Spacer().frame(height: 26)
                }
                .padding(.horizontal, 20)
            }
        }
        .sheet(isPresented: $showDeleteSheet) {
            DeleteAccountSheet(accountName: AuraCopy.clientUser, onConfirmed: onDeleteAccount)
        }
    }
}
