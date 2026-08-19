import SwiftUI

private let tabTerms = "Términos y condiciones"
private let tabPrivacy = "Aviso de privacidad"

/// Single legal screen with both documents behind a segmented switch.
struct LegalView: View {
    let onBack: () -> Void
    var startsOnPrivacy: Bool = false

    @State private var tab: String = tabTerms

    private var sections: [LegalSection] {
        tab == tabPrivacy ? LegalCopy.privacy : LegalCopy.terms
    }

    var body: some View {
        AuraPlainScaffold {
            AuraHeader(
                title: "Avisos legales",
                eyebrow: AuraCopy.brandName,
                subtitle: AuraCopy.legalUpdated,
                onBack: onBack
            )
        } content: {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    Spacer().frame(height: 18)

                    HStack(spacing: 9) {
                        AuraFilterChip(
                            title: "Términos",
                            isSelected: tab == tabTerms,
                            action: { tab = tabTerms }
                        )
                        AuraFilterChip(
                            title: "Privacidad",
                            isSelected: tab == tabPrivacy,
                            action: { tab = tabPrivacy }
                        )
                        Spacer(minLength: 0)
                    }

                    Spacer().frame(height: 22)

                    Text(tab)
                        .font(AuraFont.headlineSmall())
                        .foregroundStyle(AuraPalette.navy)

                    ForEach(sections) { section in
                        Spacer().frame(height: 20)
                        Text(section.title)
                            .font(AuraFont.titleMedium())
                            .foregroundStyle(AuraPalette.navy)
                            .fixedSize(horizontal: false, vertical: true)
                            .padding(.bottom, 7)
                        Text(section.body)
                            .font(AuraFont.bodyMedium())
                            .foregroundStyle(AuraPalette.ink)
                            .fixedSize(horizontal: false, vertical: true)
                    }

                    Spacer().frame(height: 28)

                    Text("The Aura Den · \(AuraCopy.addressLine1), \(AuraCopy.addressLine2)")
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)

                    Spacer().frame(height: 30)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal, 20)
            }
        }
        .onAppear {
            if startsOnPrivacy { tab = tabPrivacy }
        }
    }
}
