import SwiftUI

private let specialtyFilters = ["Todas", "Uñas", "Uñas acrílicas", "Pestañas / cejas"]

/// The client browses specialists and picks who will attend her.
struct ClientExploreView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    let onBack: () -> Void
    let onBookWith: (String) -> Void

    @State private var filter: String = specialtyFilters[0]

    private var specialists: [SpecialistProfile] {
        DemoData.specialists.filter { filter == "Todas" || $0.specialty == filter }
    }

    var body: some View {
        AuraTabScaffold(
            role: .client,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: "Explorar",
                eyebrow: "The Aura Den · \(AuraCopy.neighborhood)",
                subtitle: "Elige con quién quieres consentirte",
                onBack: onBack
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 16)

                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 9) {
                            ForEach(specialtyFilters, id: \.self) { option in
                                AuraFilterChip(
                                    title: option,
                                    isSelected: option == filter,
                                    action: { filter = option }
                                )
                            }
                        }
                        .padding(.horizontal, 20)
                    }

                    Spacer().frame(height: 18)

                    VStack(spacing: 14) {
                        ForEach(specialists) { specialist in
                            SpecialistCard(specialist: specialist) {
                                onBookWith(specialist.id)
                            }
                        }
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 26)
                }
            }
        }
    }
}

private struct SpecialistCard: View {
    let specialist: SpecialistProfile
    let onBook: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 15) {
                AuraAvatar(imageURL: specialist.imageURL, size: 72, ringColor: .clear)
                VStack(alignment: .leading, spacing: 0) {
                    Text(specialist.name)
                        .font(AuraFont.headlineSmall())
                        .foregroundStyle(AuraPalette.navy)
                        .padding(.bottom, 6)
                    StatusPill(
                        text: specialist.specialty,
                        foreground: AuraPalette.navy,
                        background: AuraPalette.yellow
                    )
                    .padding(.bottom, 8)
                    HStack(spacing: 7) {
                        RatingStars(rating: specialist.rating, starSize: 14)
                        Text("\(String(format: "%.1f", specialist.rating)) · \(specialist.reviewCount) reseñas")
                            .font(AuraFont.bodySmall())
                            .foregroundStyle(AuraPalette.inkMuted)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }

            if let nextServiceLabel = specialist.nextServiceLabel {
                HStack(spacing: 7) {
                    Image(systemName: "clock")
                        .font(.system(size: 13))
                    Text(nextServiceLabel)
                        .font(AuraFont.bodyMedium())
                        .fixedSize(horizontal: false, vertical: true)
                }
                .foregroundStyle(AuraPalette.blue)
                .padding(.top, 14)
            }

            EyebrowText(text: "Servicios desde $450 MXN", color: AuraPalette.inkMuted)
                .padding(.top, 12)

            AuraPrimaryButton(title: "Agendar con ella", action: onBook)
                .padding(.top, 14)
        }
        .padding(18)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
    }
}
