import SwiftUI

/// Specialist profile with contact data, reviews and the discreet demo role shortcut.
struct ProfileView: View {
    let onBack: () -> Void
    let onEditProfile: () -> Void
    let onOpenContract: () -> Void
    let onOpenLegal: () -> Void
    let onOpenRoleSwitcher: () -> Void
    let onSignOut: () -> Void
    let onDeleteAccount: () -> Void

    @Environment(DemoStore.self) private var store
    @State private var showDeleteSheet = false

    var body: some View {
        let profile = store.profile

        ScrollView {
            VStack(spacing: 0) {
                VStack(spacing: 0) {
                    HStack(spacing: 0) {
                        Button {
                            AuraHaptics.tap()
                            onBack()
                        } label: {
                            Image(systemName: "arrow.left")
                                .font(.system(size: 20, weight: .medium))
                                .foregroundStyle(AuraPalette.white)
                                .frame(width: 48, height: 48)
                                .contentShape(.rect)
                        }
                        .buttonStyle(.plain)
                        .accessibilityLabel("Regresar")

                        Text("Perfil")
                            .font(AuraFont.headlineSmall())
                            .foregroundStyle(AuraPalette.white)
                            .frame(maxWidth: .infinity, alignment: .leading)

                        Button {
                            AuraHaptics.tap()
                            onOpenRoleSwitcher()
                        } label: {
                            Image(systemName: "ellipsis")
                                .font(.system(size: 20, weight: .medium))
                                .foregroundStyle(AuraPalette.white)
                                .frame(width: 48, height: 48)
                                .contentShape(.rect)
                        }
                        .buttonStyle(.plain)
                        .accessibilityLabel("Más opciones")
                    }
                    .padding(.horizontal, 8)
                    .padding(.vertical, 4)

                    Spacer().frame(height: 6)

                    Circle()
                        .fill(AuraPalette.white)
                        .frame(width: 136, height: 136)
                        .overlay {
                            AuraAvatar(imageURL: profile.imageURL, size: 128, ringColor: .clear)
                        }

                    Spacer().frame(height: 14)

                    Text(profile.name)
                        .font(AuraFont.displaySmall())
                        .foregroundStyle(AuraPalette.white)

                    Spacer().frame(height: 10)

                    StatusPill(
                        text: profile.specialty,
                        foreground: AuraPalette.navy,
                        background: AuraPalette.yellow,
                        symbol: "star.fill"
                    )

                    Spacer().frame(height: 10)

                    Text(profile.since)
                        .font(AuraFont.bodyMedium())
                        .foregroundStyle(AuraPalette.white.opacity(0.85))
                }
                .padding(.top, safeAreaTop)
                .padding(.bottom, 26)
                .frame(maxWidth: .infinity)
                .background(
                    AuraPalette.blue,
                    in: .rect(bottomLeadingRadius: 30, bottomTrailingRadius: 30)
                )

                VStack(spacing: 0) {
                    Spacer().frame(height: 20)

                    VStack(spacing: 0) {
                        InfoRow(
                            label: "Celular",
                            value: profile.phone,
                            symbol: "phone.fill",
                            showsChevron: true,
                            action: onEditProfile
                        )
                        AuraDivider().padding(.horizontal, 18)
                        InfoRow(
                            label: "Correo",
                            value: profile.email,
                            symbol: "envelope.fill",
                            showsChevron: true,
                            action: onEditProfile
                        )
                    }
                    .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))

                    Spacer().frame(height: 16)

                    VStack(alignment: .leading, spacing: 0) {
                        HStack {
                            EyebrowText(text: "Mis reseñas", color: AuraPalette.sand)
                            Spacer()
                            Image(systemName: "chevron.right")
                                .font(.system(size: 15, weight: .medium))
                                .foregroundStyle(AuraPalette.sand)
                        }
                        .padding(.bottom, 8)

                        Text(String(format: "%.1f", profile.rating))
                            .font(AuraFont.displayMedium())
                            .foregroundStyle(AuraPalette.navy)
                            .padding(.bottom, 8)

                        HStack {
                            RatingStars(rating: profile.rating, starSize: 20)
                            Spacer()
                            Text("\(profile.reviewCount) reseñas")
                                .font(AuraFont.bodyMedium())
                                .foregroundStyle(AuraPalette.inkMuted)
                        }
                    }
                    .padding(20)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))

                    Spacer().frame(height: 12)

                    ForEach(DemoData.reviews.prefix(2)) { review in
                        ReviewCard(review: review)
                            .padding(.bottom, 10)
                    }

                    Spacer().frame(height: 10)

                    Button {
                        AuraHaptics.tap()
                        onOpenRoleSwitcher()
                    } label: {
                        HStack(spacing: 12) {
                            Image(systemName: "arrow.left.arrow.right")
                                .font(.system(size: 18))
                                .foregroundStyle(AuraPalette.blue)
                            VStack(alignment: .leading, spacing: 2) {
                                Text("Cambiar de vista")
                                    .font(AuraFont.titleMedium())
                                    .foregroundStyle(AuraPalette.ink)
                                Text("Modo demo · Especialista, Clienta, Recepción, Admin")
                                    .font(AuraFont.bodySmall())
                                    .foregroundStyle(AuraPalette.inkMuted)
                                    .multilineTextAlignment(.leading)
                                    .fixedSize(horizontal: false, vertical: true)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                            StatusPill(
                                text: "Demo",
                                foreground: AuraPalette.navy,
                                background: AuraPalette.yellow
                            )
                        }
                        .padding(16)
                        .frame(maxWidth: .infinity)
                        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
                    }
                    .buttonStyle(PressableButtonStyle())

                    Spacer().frame(height: 12)

                    ContractRow(contract: store.signedContract, action: onOpenContract)

                    Spacer().frame(height: 12)

                    InfoRow(
                        label: "Legal",
                        value: "Términos y aviso de privacidad",
                        symbol: "doc.text.fill",
                        showsChevron: true,
                        action: onOpenLegal
                    )
                    .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))

                    Spacer().frame(height: 22)

                    AuraPrimaryButton(title: "Editar perfil", action: onEditProfile)

                    Button("Cerrar sesión") {
                        AuraHaptics.tap()
                        onSignOut()
                    }
                    .font(AuraFont.labelLarge())
                    .foregroundStyle(AuraPalette.red)
                    .frame(maxWidth: .infinity)
                    .frame(height: 48)
                    .padding(.top, 6)

                    Button("Eliminar mi cuenta") {
                        AuraHaptics.tap()
                        showDeleteSheet = true
                    }
                    .font(AuraFont.labelLarge())
                    .foregroundStyle(AuraPalette.red)
                    .frame(maxWidth: .infinity)
                    .frame(height: 44)

                    Text("Al eliminar tu cuenta pierdes tu perfil, tus reseñas, tu historial de citas y tu membresía activa.")
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .multilineTextAlignment(.center)
                        .fixedSize(horizontal: false, vertical: true)
                        .padding(.top, 4)

                    Spacer().frame(height: 24)
                }
                .padding(.horizontal, 20)
            }
        }
        .background(AuraPalette.canvas)
        .ignoresSafeArea(edges: .top)
        .toolbar(.hidden, for: .navigationBar)
        .sheet(isPresented: $showDeleteSheet) {
            DeleteAccountSheet(accountName: store.profile.name, onConfirmed: onDeleteAccount)
        }
    }

    private var safeAreaTop: CGFloat {
        #if canImport(UIKit)
        let scenes = UIApplication.shared.connectedScenes
        let window = scenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap(\.windows)
            .first { $0.isKeyWindow }
        return window?.safeAreaInsets.top ?? 47
        #else
        return 47
        #endif
    }
}

/// Entry point to the signed rental agreement, or a nudge to sign it.
private struct ContractRow: View {
    let contract: SignedContract?
    let action: () -> Void

    private var isSigned: Bool { contract != nil }

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            HStack(spacing: 14) {
                ZStack {
                    RoundedRectangle(cornerRadius: 13)
                        .fill(isSigned ? AuraPalette.greenSoft : AuraPalette.amber.opacity(0.16))
                        .frame(width: 42, height: 42)
                    Image(systemName: isSigned ? "checkmark.seal.fill" : "signature")
                        .font(.system(size: 18))
                        .foregroundStyle(isSigned ? AuraPalette.green : AuraPalette.amber)
                }
                VStack(alignment: .leading, spacing: 2) {
                    Text(isSigned ? "Ver mi contrato firmado" : "Firmar mi contrato")
                        .font(AuraFont.titleMedium())
                        .fontWeight(.semibold)
                        .foregroundStyle(AuraPalette.ink)
                        .multilineTextAlignment(.leading)
                    Text(
                        contract.map { "Folio \($0.folio) · \($0.dateLabel)" }
                            ?? "Pendiente de firma"
                    )
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(AuraPalette.inkMuted)
                    .multilineTextAlignment(.leading)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                StatusPill(
                    text: isSigned ? "Firmado" : "Pendiente",
                    foreground: isSigned ? AuraPalette.green : AuraPalette.amber,
                    background: isSigned ? AuraPalette.greenSoft : AuraPalette.amber.opacity(0.16)
                )
            }
            .padding(16)
            .frame(maxWidth: .infinity)
            .background(
                isSigned ? AuraPalette.white : AuraPalette.amberSoft,
                in: .rect(cornerRadius: AuraRadius.card)
            )
        }
        .buttonStyle(PressableButtonStyle())
    }
}

private struct ReviewCard: View {
    let review: Review

    private var initials: String {
        review.author
            .split(separator: " ")
            .compactMap(\.first)
            .map(String.init)
            .joined()
    }

    var body: some View {
        HStack(alignment: .top, spacing: 13) {
            ZStack {
                Circle()
                    .fill(AuraPalette.sandSoft)
                    .frame(width: 44, height: 44)
                Text(initials)
                    .font(AuraFont.titleMedium())
                    .foregroundStyle(AuraPalette.navy)
            }

            VStack(alignment: .leading, spacing: 0) {
                Text(review.author)
                    .font(AuraFont.titleMedium())
                    .fontWeight(.semibold)
                    .foregroundStyle(AuraPalette.ink)
                    .padding(.bottom, 3)
                RatingStars(rating: Double(review.rating), starSize: 14)
                    .padding(.bottom, 6)
                Text("“\(review.comment)”")
                    .font(AuraFont.bodyMedium())
                    .foregroundStyle(AuraPalette.ink)
                    .fixedSize(horizontal: false, vertical: true)
                    .padding(.bottom, 6)
                Text(review.timeAgo)
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(AuraPalette.inkMuted)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
        }
        .padding(16)
        .frame(maxWidth: .infinity)
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
    }
}
