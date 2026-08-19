import SwiftUI

/// Step 1 of the onboarding contract flow: the specialist has to read the whole agreement
/// before the acceptance checkbox unlocks. Also reused read-only from "Mi contrato".
struct ContractReadView: View {
    let onBack: () -> Void
    let onContinue: () -> Void
    var isReadOnly: Bool = false

    @Environment(DemoStore.self) private var store
    @State private var readingProgress: Double = 0
    @State private var reachedEnd = false
    @State private var accepted = false

    private var signedContract: SignedContract? {
        isReadOnly ? store.signedContract : nil
    }

    var body: some View {
        AuraDetailScaffold {
            AuraHeader(
                title: isReadOnly ? "Mi contrato" : "Contrato de renta",
                eyebrow: isReadOnly ? "Documento firmado" : "Paso 1 de 2",
                subtitle: isReadOnly
                    ? "Folio \(signedContract?.folio ?? "")"
                    : "Léelo completo para poder continuar",
                onBack: onBack,
                content: {
                    GeometryReader { proxy in
                        ZStack(alignment: .leading) {
                            Capsule().fill(AuraPalette.white.opacity(0.22))
                            Capsule()
                                .fill(AuraPalette.yellow)
                                .frame(
                                    width: proxy.size.width
                                        * (isReadOnly ? 1 : max(readingProgress, 0.02))
                                )
                        }
                    }
                    .frame(height: 5)
                    .animation(.easeOut(duration: 0.18), value: readingProgress)
                }
            )
        } content: {
            ScrollViewReader { scrollProxy in
                ScrollView {
                    VStack(spacing: 0) {
                        Spacer().frame(height: 18)

                        if let signedContract {
                            HStack(spacing: 12) {
                                Image(systemName: "checkmark.seal.fill")
                                    .font(.system(size: 18))
                                    .foregroundStyle(AuraPalette.green)
                                Text("Firmado por \(signedContract.signerName) el \(signedContract.dateLabel) a las \(signedContract.timeLabel)")
                                    .font(AuraFont.bodySmall())
                                    .foregroundStyle(AuraPalette.green)
                                    .fixedSize(horizontal: false, vertical: true)
                                Spacer(minLength: 0)
                            }
                            .padding(16)
                            .frame(maxWidth: .infinity)
                            .background(
                                AuraPalette.greenSoft,
                                in: .rect(cornerRadius: AuraRadius.card)
                            )
                            .padding(.bottom, 16)
                        }

                        ContractPaper(
                            signerName: store.contractSignerName,
                            signedContract: signedContract
                        )

                        Color.clear
                            .frame(height: 26)
                            .id(bottomAnchor)
                    }
                    .padding(.horizontal, 20)
                }
                .onScrollGeometryChange(for: Double.self) { geometry in
                    let scrollable = geometry.contentSize.height
                        + geometry.contentInsets.top
                        + geometry.contentInsets.bottom
                        - geometry.containerSize.height
                    guard scrollable > 1 else { return 1 }
                    let offset = geometry.contentOffset.y + geometry.contentInsets.top
                    return min(max(offset / scrollable, 0), 1)
                } action: { _, progress in
                    readingProgress = progress
                    if progress >= 0.985 { reachedEnd = true }
                }
                .onChange(of: goToEnd) { _, _ in
                    withAnimation(.easeInOut(duration: 0.6)) {
                        scrollProxy.scrollTo(bottomAnchor, anchor: .bottom)
                    }
                }
            }
        } bottomAction: {
            if isReadOnly {
                AuraPrimaryButton(title: "Cerrar", action: onBack)
            } else {
                VStack(spacing: 0) {
                    if !reachedEnd {
                        Button {
                            AuraHaptics.tap()
                            goToEnd.toggle()
                        } label: {
                            HStack(spacing: 10) {
                                Text("Desliza hasta el final para aceptar")
                                    .font(AuraFont.bodySmall())
                                    .foregroundStyle(AuraPalette.inkMuted)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                ZStack {
                                    Circle()
                                        .fill(AuraPalette.blue)
                                        .frame(width: 28, height: 28)
                                    Image(systemName: "arrow.down")
                                        .font(.system(size: 13, weight: .bold))
                                        .foregroundStyle(AuraPalette.white)
                                }
                            }
                            .padding(.horizontal, 16)
                            .padding(.vertical, 12)
                            .frame(maxWidth: .infinity)
                            .background(AuraPalette.cream, in: .rect(cornerRadius: 18))
                        }
                        .buttonStyle(PressableButtonStyle())
                        .padding(.bottom, 12)
                        .transition(.opacity)
                    }

                    Button {
                        guard reachedEnd else { return }
                        AuraHaptics.tap()
                        accepted.toggle()
                    } label: {
                        HStack(spacing: 12) {
                            ZStack {
                                RoundedRectangle(cornerRadius: 7)
                                    .fill(accepted ? AuraPalette.blue : AuraPalette.white)
                                    .frame(width: 26, height: 26)
                                RoundedRectangle(cornerRadius: 7)
                                    .strokeBorder(
                                        accepted ? AuraPalette.blue : AuraPalette.inkFaint,
                                        lineWidth: 1.6
                                    )
                                    .frame(width: 26, height: 26)
                                if accepted {
                                    Image(systemName: "checkmark")
                                        .font(.system(size: 14, weight: .bold))
                                        .foregroundStyle(AuraPalette.white)
                                }
                            }
                            Text("He leído y acepto los términos de este contrato")
                                .font(AuraFont.bodyMedium())
                                .foregroundStyle(reachedEnd ? AuraPalette.ink : AuraPalette.inkFaint)
                                .multilineTextAlignment(.leading)
                                .fixedSize(horizontal: false, vertical: true)
                            Spacer(minLength: 0)
                        }
                        .frame(maxWidth: .infinity)
                        .contentShape(.rect)
                    }
                    .buttonStyle(.plain)
                    .disabled(!reachedEnd)
                    .padding(.bottom, 12)

                    AuraPrimaryButton(
                        title: "Continuar",
                        isEnabled: accepted,
                        action: onContinue
                    )
                }
                .animation(.easeInOut(duration: 0.25), value: reachedEnd)
                .animation(.easeInOut(duration: 0.2), value: accepted)
            }
        }
    }

    @State private var goToEnd = false
    private let bottomAnchor = "contract-bottom"
}

/// The agreement itself, laid out like a printed document.
private struct ContractPaper: View {
    let signerName: String
    let signedContract: SignedContract?

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            AuraCardMark()
                .frame(maxWidth: .infinity)
                .padding(.bottom, 18)

            Text(ContractCopy.title)
                .font(AuraFont.headlineSmall())
                .foregroundStyle(AuraPalette.navy)
                .multilineTextAlignment(.center)
                .frame(maxWidth: .infinity)
                .padding(.bottom, 6)

            Text(ContractCopy.subtitle)
                .font(AuraFont.bodySmall())
                .foregroundStyle(AuraPalette.inkMuted)
                .frame(maxWidth: .infinity)
                .padding(.bottom, 18)

            Rectangle()
                .fill(AuraPalette.sandSoft)
                .frame(height: 1)
                .padding(.bottom, 18)

            Text(ContractCopy.parties)
                .font(AuraFont.bodyMedium())
                .foregroundStyle(AuraPalette.ink)
                .fixedSize(horizontal: false, vertical: true)

            ForEach(ContractCopy.sections) { section in
                HStack(spacing: 10) {
                    ZStack {
                        RoundedRectangle(cornerRadius: 9)
                            .fill(AuraPalette.sandSoft)
                            .frame(width: 26, height: 26)
                        Text(section.number)
                            .font(AuraFont.labelMedium())
                            .fontWeight(.bold)
                            .foregroundStyle(AuraPalette.navy)
                    }
                    Text(section.title)
                        .font(AuraFont.titleMedium())
                        .fontWeight(.semibold)
                        .foregroundStyle(AuraPalette.navy)
                        .fixedSize(horizontal: false, vertical: true)
                    Spacer(minLength: 0)
                }
                .padding(.top, 22)
                .padding(.bottom, 9)

                Text(section.body)
                    .font(AuraFont.bodyMedium())
                    .foregroundStyle(AuraPalette.ink)
                    .fixedSize(horizontal: false, vertical: true)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }

            Rectangle()
                .fill(AuraPalette.sandSoft)
                .frame(height: 1)
                .padding(.top, 24)
                .padding(.bottom, 18)

            Text(ContractCopy.closing)
                .font(AuraFont.bodyMedium())
                .foregroundStyle(AuraPalette.ink)
                .fixedSize(horizontal: false, vertical: true)

            EyebrowText(text: "Por la especialista")
                .padding(.top, 26)
                .padding(.bottom, 10)

            Rectangle()
                .fill(AuraPalette.inkFaint)
                .frame(height: 1)
                .padding(.bottom, 8)

            Text(signedContract?.signerName ?? signerName)
                .font(AuraFont.titleMedium())
                .foregroundStyle(AuraPalette.navy)

            Text(
                signedContract.map { "Folio \($0.folio) · \($0.dateLabel), \($0.timeLabel)" }
                    ?? "Pendiente de firma · \(AuraCopy.addressLine2)"
            )
            .font(AuraFont.bodySmall())
            .foregroundStyle(AuraPalette.inkMuted)
        }
        .padding(.horizontal, 22)
        .padding(.vertical, 26)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
    }
}
