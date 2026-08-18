import SwiftUI

private let allFilter = "Todas"
private let pendingFilter = "Pendientes"
private let attendedFilter = "Atendidas"
private let checkInFilters = [allFilter, pendingFilter, attendedFilter]

/// Arrival control for the front desk.
struct ReceptionCheckInView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    let onOpenWalkIn: () -> Void

    @Environment(DemoStore.self) private var store
    @State private var filter: String = allFilter

    var body: some View {
        let visible = store.checkIns.filter { checkIn in
            switch filter {
            case pendingFilter: checkIn.status != .attended
            case attendedFilter: checkIn.status == .attended
            default: true
            }
        }
        let nextArrival = store.checkIns.first { $0.status == .pending }

        AuraTabScaffold(
            role: .reception,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: "Check-in de hoy",
                eyebrow: "Recepción",
                subtitle: AuraCopy.todayLabel
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 18)

                    HStack(spacing: 12) {
                        VStack(alignment: .leading, spacing: 5) {
                            Text("\(store.pendingCheckIns) citas restantes hoy")
                                .font(AuraFont.headlineSmall())
                                .foregroundStyle(AuraPalette.navy)
                                .fixedSize(horizontal: false, vertical: true)
                            if let nextArrival {
                                HStack(spacing: 6) {
                                    Image(systemName: "clock")
                                        .font(.system(size: 12))
                                    Text("Próxima llegada · \(nextArrival.clientName) · \(nextArrival.time)")
                                        .font(AuraFont.bodyMedium())
                                        .fixedSize(horizontal: false, vertical: true)
                                }
                                .foregroundStyle(AuraPalette.blue)
                            }
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)

                        ZStack {
                            Circle()
                                .fill(AuraPalette.white)
                                .frame(width: 46, height: 46)
                            Image(systemName: "person.crop.circle.badge.checkmark")
                                .font(.system(size: 20))
                                .foregroundStyle(AuraPalette.blue)
                        }
                    }
                    .padding(18)
                    .frame(maxWidth: .infinity)
                    .background(AuraPalette.blueSoft, in: .rect(cornerRadius: AuraRadius.card))
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 18)

                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 9) {
                            ForEach(checkInFilters, id: \.self) { option in
                                AuraFilterChip(
                                    title: option,
                                    isSelected: option == filter,
                                    action: { filter = option }
                                )
                            }
                        }
                        .padding(.horizontal, 20)
                    }

                    Spacer().frame(height: 16)

                    if visible.isEmpty {
                        AuraEmptyState(
                            title: filter == pendingFilter
                                ? "No tienes citas pendientes"
                                : "No tienes citas hoy",
                            message: "Todas las clientas del día ya fueron registradas. Puedes dar de alta a una clienta sin cita.",
                            symbol: "checkmark.circle.fill",
                            actionLabel: "Registrar walk-in",
                            action: onOpenWalkIn
                        )
                    } else {
                        VStack(spacing: 12) {
                            ForEach(visible) { checkIn in
                                CheckInCard(checkIn: checkIn) {
                                    store.markArrival(id: checkIn.id)
                                }
                            }
                        }
                        .padding(.horizontal, 20)
                    }

                    Spacer().frame(height: 18)

                    HStack(spacing: 8) {
                        CheckInStatusPill(status: .pending)
                        CheckInStatusPill(status: .waiting)
                        CheckInStatusPill(status: .attended)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 26)
                }
            }
        }
    }
}

private struct CheckInCard: View {
    let checkIn: CheckIn
    let onMarkArrival: () -> Void

    private var isAttended: Bool { checkIn.status == .attended }

    private var initials: String {
        checkIn.clientName
            .split(separator: " ")
            .compactMap(\.first)
            .map(String.init)
            .joined()
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 14) {
                ZStack {
                    RoundedRectangle(cornerRadius: 16)
                        .fill(isAttended ? AuraPalette.greenSoft : AuraPalette.sandSoft)
                        .frame(width: 52, height: 52)
                    Text(initials)
                        .font(AuraFont.titleMedium())
                        .foregroundStyle(isAttended ? AuraPalette.green : AuraPalette.navy)
                }

                VStack(alignment: .leading, spacing: 0) {
                    Text(checkIn.clientName)
                        .font(AuraFont.titleMedium())
                        .fontWeight(.semibold)
                        .foregroundStyle(AuraPalette.ink)
                        .padding(.bottom, 3)
                    Text("\(checkIn.service) · \(checkIn.specialistName)")
                        .font(AuraFont.bodyMedium())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .fixedSize(horizontal: false, vertical: true)
                        .padding(.bottom, 6)
                    EyebrowText(text: checkIn.time)
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                CheckInStatusPill(status: checkIn.status)
            }
            .padding(.bottom, 14)

            if isAttended {
                HStack(spacing: 7) {
                    Image(systemName: "checkmark.circle.fill")
                        .font(.system(size: 14))
                    Text("Llegada registrada")
                        .font(AuraFont.labelLarge())
                }
                .foregroundStyle(AuraPalette.green)
            } else {
                AuraSecondaryButton(title: "Marcar llegada", action: onMarkArrival)
            }
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
    }
}
