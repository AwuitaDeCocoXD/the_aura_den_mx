import SwiftUI

private let liveFilters = ["Todas", "Disponibles", "En uso"]

/// Owner's live floor: station status plus the day's key numbers.
struct AdminLiveView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    let onOpenSpecialists: () -> Void
    let onOpenRoleSwitcher: () -> Void

    @State private var filter: String = liveFilters[0]

    private var occupiedCount: Int {
        DemoData.stations.count { $0.status == .occupied }
    }

    private var stations: [Station] {
        DemoData.stations.filter { station in
            switch filter {
            case "Disponibles": station.status == .available
            case "En uso": station.status == .occupied
            default: true
            }
        }
    }

    var body: some View {
        let stationRows = stride(from: 0, to: stations.count, by: 2).map { index in
            Array(stations[index..<min(index + 2, stations.count)])
        }
        let kpiRows = stride(from: 0, to: DemoData.todayKpis.count, by: 2).map { index in
            Array(DemoData.todayKpis[index..<min(index + 2, DemoData.todayKpis.count)])
        }

        AuraTabScaffold(
            role: .admin,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: "Vista en vivo",
                eyebrow: "Modo administrador",
                subtitle: "\(AuraCopy.todayLabel) · 11:42 am",
                trailing: {
                    Button {
                        AuraHaptics.tap()
                        onOpenRoleSwitcher()
                    } label: {
                        ZStack {
                            Circle()
                                .fill(AuraPalette.white.opacity(0.14))
                                .frame(width: 42, height: 42)
                            Text("AD")
                                .font(AuraFont.titleSmall())
                                .foregroundStyle(AuraPalette.yellow)
                        }
                    }
                    .buttonStyle(.plain)
                    .accessibilityLabel("Cambiar de vista")
                }
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 16)

                    PendingPaymentsBanner(
                        overdueCount: DemoData.pendingCharges.count(where: \.overdue),
                        total: DemoData.pendingTotal,
                        action: onOpenSpecialists
                    )
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 20)

                    SectionHeading(text: "Piso en vivo")
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 4)

                    Text("\(occupiedCount) de \(DemoData.stations.count) estaciones ocupadas ahora mismo")
                        .font(AuraFont.bodyMedium())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .fixedSize(horizontal: false, vertical: true)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 14)

                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 9) {
                            ForEach(liveFilters, id: \.self) { option in
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

                    VStack(spacing: 12) {
                        ForEach(Array(stationRows.enumerated()), id: \.offset) { _, row in
                            HStack(alignment: .top, spacing: 12) {
                                ForEach(row) { station in
                                    LiveStationTile(station: station)
                                }
                                if row.count == 1 {
                                    Spacer().frame(maxWidth: .infinity)
                                }
                            }
                        }
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 20)

                    SectionHeading(text: "Resumen de hoy")
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 12)

                    VStack(spacing: 12) {
                        ForEach(Array(kpiRows.enumerated()), id: \.offset) { _, row in
                            HStack(alignment: .top, spacing: 12) {
                                ForEach(row) { kpi in
                                    MetricTile(
                                        value: kpi.value,
                                        label: kpi.label,
                                        background: kpi.label == "Ingresos del día"
                                            ? AuraPalette.cream
                                            : AuraPalette.white
                                    )
                                }
                                if row.count == 1 {
                                    Spacer().frame(maxWidth: .infinity)
                                }
                            }
                        }
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 16)

                    HStack(spacing: 8) {
                        Circle()
                            .fill(AuraPalette.yellow)
                            .frame(width: 7, height: 7)
                        Text("Datos actualizados hace 1 minuto")
                            .font(AuraFont.bodySmall())
                            .foregroundStyle(AuraPalette.inkMuted)
                        Spacer()
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 26)
                }
            }
        }
    }
}

/// Red alert that pulls the owner straight into the renters screen.
private struct PendingPaymentsBanner: View {
    let overdueCount: Int
    let total: Int
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            HStack(spacing: 14) {
                ZStack {
                    RoundedRectangle(cornerRadius: 14)
                        .fill(AuraPalette.red)
                        .frame(width: 44, height: 44)
                    Image(systemName: "exclamationmark")
                        .font(.system(size: 20, weight: .bold))
                        .foregroundStyle(AuraPalette.white)
                }

                VStack(alignment: .leading, spacing: 2) {
                    Text("\(overdueCount) pagos vencidos")
                        .font(AuraFont.titleMedium())
                        .fontWeight(.semibold)
                        .foregroundStyle(AuraPalette.red)
                    Text("\(Money.format(total)) por cobrar en total")
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                Image(systemName: "chevron.right")
                    .font(.system(size: 15, weight: .semibold))
                    .foregroundStyle(AuraPalette.red)
            }
            .padding(16)
            .frame(maxWidth: .infinity)
            .background(AuraPalette.redSoft, in: .rect(cornerRadius: AuraRadius.card))
        }
        .buttonStyle(PressableButtonStyle())
    }
}

private struct LiveStationTile: View {
    let station: Station

    private var isAvailable: Bool { station.status == .available }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack {
                ZStack {
                    RoundedRectangle(cornerRadius: 12)
                        .fill(isAvailable ? AuraPalette.greenSoft : AuraPalette.sandSoft)
                        .frame(width: 38, height: 38)
                    Image(systemName: "chair.lounge.fill")
                        .font(.system(size: 16))
                        .foregroundStyle(isAvailable ? AuraPalette.green : AuraPalette.navy)
                }
                Spacer(minLength: 4)
                StatusPill(
                    text: isAvailable ? "Libre" : "Ocupada",
                    foreground: isAvailable ? AuraPalette.green : AuraPalette.grey,
                    background: isAvailable ? AuraPalette.greenSoft : AuraPalette.greySoft
                )
            }
            .padding(.bottom, 12)

            Text(station.name)
                .font(AuraFont.titleMedium())
                .fontWeight(.semibold)
                .foregroundStyle(AuraPalette.ink)
                .fixedSize(horizontal: false, vertical: true)
                .padding(.bottom, 6)

            Group {
                if isAvailable {
                    EyebrowText(text: "Sin reserva", color: AuraPalette.inkMuted)
                } else {
                    Text(station.occupiedBy ?? "")
                        .font(AuraFont.bodyMedium())
                        .foregroundStyle(AuraPalette.blue)
                }
            }
            .padding(.bottom, 8)

            HStack(spacing: 5) {
                Image(systemName: "clock")
                    .font(.system(size: 11))
                Text(isAvailable ? station.scheduleLabel : (station.nextAvailability ?? station.scheduleLabel))
                    .font(AuraFont.bodySmall())
                    .fixedSize(horizontal: false, vertical: true)
            }
            .foregroundStyle(AuraPalette.inkMuted)
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
    }
}
