import SwiftUI

private let stationFilters = ["Todas", "Disponibles", "Mesa de uñas", "Pestañas"]

/// Editorial discovery of the coworking stations.
struct SpacesView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    let onOpenStation: (String) -> Void
    let onOpenMemberships: () -> Void
    let onSignContract: () -> Void

    @Environment(DemoStore.self) private var store
    @State private var filter: String = stationFilters[0]

    private var stations: [Station] {
        DemoData.stations.filter { station in
            switch filter {
            case "Disponibles": station.status == .available
            case "Mesa de uñas": station.kind == "Mesa de uñas"
            case "Pestañas": station.kind == "Pestañas"
            default: true
            }
        }
    }

    var body: some View {
        AuraTabScaffold(
            role: .specialist,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: "Explorar espacios",
                eyebrow: AuraCopy.todayLabel,
                subtitle: "Encuentra tu lugar para crear"
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 16)

                    if !store.hasSignedContract {
                        ContractLockedCard(onSignContract: onSignContract)
                            .padding(.horizontal, 20)
                            .padding(.bottom, 18)
                    }

                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 9) {
                            ForEach(stationFilters, id: \.self) { option in
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
                        ForEach(stations) { station in
                            StationCard(
                                station: station,
                                isLocked: !store.hasSignedContract
                            ) {
                                if store.hasSignedContract {
                                    onOpenStation(station.id)
                                } else {
                                    onSignContract()
                                }
                            }
                        }
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 14)

                    MembershipTeaserCard(onOpenMemberships: onOpenMemberships)
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 28)
                }
            }
        }
    }
}

/// Renting is blocked until the rental agreement is signed.
private struct ContractLockedCard: View {
    let onSignContract: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 14) {
                ZStack {
                    RoundedRectangle(cornerRadius: 13)
                        .fill(AuraPalette.amber.opacity(0.18))
                        .frame(width: 40, height: 40)
                    Image(systemName: "lock.fill")
                        .font(.system(size: 17))
                        .foregroundStyle(AuraPalette.amber)
                }
                VStack(alignment: .leading, spacing: 2) {
                    Text("Falta firmar tu contrato")
                        .font(AuraFont.titleMedium())
                        .fontWeight(.semibold)
                        .foregroundStyle(AuraPalette.amber)
                    Text("Necesario para reservar una estación")
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            .padding(.bottom, 16)

            AuraPrimaryButton(title: "Firmar contrato de renta", action: onSignContract)
        }
        .padding(20)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.amberSoft, in: .rect(cornerRadius: AuraRadius.card))
    }
}

struct StationCard: View {
    let station: Station
    var isLocked: Bool = false
    let action: () -> Void

    private var isAvailable: Bool { station.status == .available && !isLocked }

    private var pillText: String {
        if isLocked { return "Requiere contrato" }
        return isAvailable ? "Disponible ahora" : "Ocupada"
    }

    var body: some View {
        VStack(spacing: 0) {
            AuraPalette.sandSoft
                .frame(height: 168)
                .overlay {
                    AsyncImage(url: URL(string: station.imageURL)) { phase in
                        if case .success(let image) = phase {
                            image
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .opacity(isAvailable ? 1 : 0.55)
                        } else {
                            AuraPalette.sandSoft
                        }
                    }
                    .allowsHitTesting(false)
                }
                .clipped()
                .overlay(alignment: .topLeading) {
                    StatusPill(
                        text: pillText,
                        foreground: isAvailable ? AuraPalette.green : AuraPalette.grey,
                        background: isAvailable ? AuraPalette.greenSoft : AuraPalette.greySoft
                    )
                    .padding(14)
                }

            VStack(alignment: .leading, spacing: 0) {
                Text(station.name)
                    .font(AuraFont.headlineSmall())
                    .foregroundStyle(AuraPalette.navy)
                    .padding(.bottom, 8)

                HStack(spacing: 6) {
                    Image(systemName: "clock")
                        .font(.system(size: 13))
                    Text(station.scheduleLabel)
                        .font(AuraFont.bodyMedium())
                        .fontWeight(.medium)
                }
                .foregroundStyle(isAvailable ? AuraPalette.blue : AuraPalette.grey)

                if !isAvailable, let nextAvailability = station.nextAvailability {
                    Text(nextAvailability)
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .padding(.top, 4)
                }

                EyebrowText(text: station.amenities.joined(separator: " · "))
                    .padding(.top, 10)

                Group {
                    if isLocked {
                        AuraSecondaryButton(
                            title: "Firmar contrato para reservar",
                            symbol: "lock.fill",
                            action: action
                        )
                    } else if isAvailable {
                        AuraPrimaryButton(title: "Reservar espacio", action: action)
                    } else {
                        AuraSecondaryButton(title: "Ver detalles", action: action)
                    }
                }
                .padding(.top, 16)
            }
            .padding(18)
            .frame(maxWidth: .infinity, alignment: .leading)
        }
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
        .clipShape(.rect(cornerRadius: AuraRadius.card))
    }
}

private struct MembershipTeaserCard: View {
    let onOpenMemberships: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 14) {
                ZStack {
                    RoundedRectangle(cornerRadius: 13)
                        .fill(AuraPalette.sand.opacity(0.35))
                        .frame(width: 40, height: 40)
                    Image(systemName: "sparkles")
                        .font(.system(size: 17))
                        .foregroundStyle(AuraPalette.navy)
                }
                VStack(alignment: .leading, spacing: 2) {
                    Text("¿Vienes varias veces al mes?")
                        .font(AuraFont.titleMedium())
                        .fontWeight(.semibold)
                        .foregroundStyle(AuraPalette.ink)
                    Text("Conoce la membresía Residente")
                        .font(AuraFont.bodyMedium())
                        .foregroundStyle(AuraPalette.inkMuted)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            .padding(.bottom, 16)

            AuraSecondaryButton(title: "Ver membresías", action: onOpenMemberships)
        }
        .padding(20)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))
    }
}
