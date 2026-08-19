import SwiftUI

/// Floor view for the front desk: who is working where, right now.
struct ReceptionTodayView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    let onOpenCheckIn: () -> Void
    let onOpenWalkIn: () -> Void
    let onOpenRoleSwitcher: () -> Void
    let onOpenNotifications: () -> Void

    @Environment(DemoStore.self) private var store

    var body: some View {
        let waiting = store.checkIns.count { $0.status == .waiting }
        let pending = store.checkIns.count { $0.status == .pending }

        AuraTabScaffold(
            role: .reception,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: "Piso hoy",
                eyebrow: "Recepción",
                subtitle: "\(AuraCopy.todayLabel) · 11:42 am",
                trailing: {
                    HStack(spacing: 4) {
                        NotificationBell(
                            unread: store.unreadNotifications,
                            action: onOpenNotifications
                        )
                        Button {
                            AuraHaptics.tap()
                            onOpenRoleSwitcher()
                        } label: {
                            ZStack {
                                Circle()
                                    .fill(AuraPalette.white.opacity(0.14))
                                    .frame(width: 42, height: 42)
                                Text("RC")
                                    .font(AuraFont.titleSmall())
                                    .foregroundStyle(AuraPalette.yellow)
                            }
                        }
                        .buttonStyle(.plain)
                        .accessibilityLabel("Cambiar de vista")
                    }
                }
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 18)

                    HStack(alignment: .top, spacing: 12) {
                        MetricTile(
                            value: "\(waiting)",
                            label: "En espera",
                            background: AuraPalette.yellow
                        )
                        MetricTile(value: "\(pending)", label: "Por llegar")
                        MetricTile(value: "6", label: "Especialistas")
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 24)

                    SectionHeading(text: "Estaciones")
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 10)

                    VStack(spacing: 10) {
                        ForEach(DemoData.stations) { station in
                            StationRow(station: station)
                        }
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 22)

                    VStack(spacing: 10) {
                        AuraSecondaryButton(
                            title: "Ir al check-in",
                            symbol: "person.crop.circle.badge.checkmark",
                            action: onOpenCheckIn
                        )
                        AuraSecondaryButton(
                            title: "Registrar walk-in",
                            symbol: "person.badge.plus",
                            action: onOpenWalkIn
                        )
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 26)
                }
            }
        }
    }
}

private struct StationRow: View {
    let station: Station

    private var isAvailable: Bool { station.status == .available }

    var body: some View {
        HStack(spacing: 14) {
            ZStack {
                RoundedRectangle(cornerRadius: 15)
                    .fill(isAvailable ? AuraPalette.greenSoft : AuraPalette.sandSoft)
                    .frame(width: 46, height: 46)
                Image(systemName: "chair.lounge.fill")
                    .font(.system(size: 19))
                    .foregroundStyle(isAvailable ? AuraPalette.green : AuraPalette.navy)
            }

            VStack(alignment: .leading, spacing: 3) {
                Text(station.name)
                    .font(AuraFont.titleMedium())
                    .fontWeight(.semibold)
                    .foregroundStyle(AuraPalette.ink)
                Text(
                    isAvailable
                        ? station.scheduleLabel
                        : "\(station.occupiedBy ?? "") · \(station.scheduleLabel.lowercased())"
                )
                .font(AuraFont.bodySmall())
                .foregroundStyle(AuraPalette.inkMuted)
                .fixedSize(horizontal: false, vertical: true)
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            StatusPill(
                text: isAvailable ? "Libre" : "En uso",
                foreground: isAvailable ? AuraPalette.green : AuraPalette.grey,
                background: isAvailable ? AuraPalette.greenSoft : AuraPalette.greySoft
            )
        }
        .padding(16)
        .frame(maxWidth: .infinity)
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
    }
}
