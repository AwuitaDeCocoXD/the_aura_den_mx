import SwiftUI

private let upcomingFilter = "Próximas"
private let pastFilter = "Pasadas"
private let historyFilters = [upcomingFilter, pastFilter]

/// The client's own appointment history: upcoming and past visits.
struct ClientHistoryView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    let onExplore: () -> Void

    @Environment(DemoStore.self) private var store
    @State private var filter: String = upcomingFilter

    var body: some View {
        let visible = filter == upcomingFilter ? store.clientUpcoming : store.clientHistory

        AuraTabScaffold(
            role: .client,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: "Mis citas",
                eyebrow: "Lucía Gómez",
                subtitle: "Tu historial en The Aura Den"
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 16)

                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 9) {
                            ForEach(historyFilters, id: \.self) { option in
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

                    if visible.isEmpty {
                        AuraEmptyState(
                            title: "Aún no tienes citas",
                            message: filter == upcomingFilter
                                ? "Cuando agendes tu próximo servicio aparecerá aquí con todos los datos."
                                : "Tus visitas anteriores aparecerán en esta lista.",
                            symbol: "calendar.badge.checkmark",
                            actionLabel: "Explorar especialistas",
                            action: onExplore
                        )
                    } else {
                        VStack(spacing: 12) {
                            ForEach(visible) { appointment in
                                ClientAppointmentRow(appointment: appointment)
                            }
                        }
                        .padding(.horizontal, 20)
                    }

                    Spacer().frame(height: 26)
                }
            }
        }
    }
}

private struct ClientAppointmentRow: View {
    let appointment: Appointment

    var body: some View {
        HStack(spacing: 14) {
            ZStack {
                RoundedRectangle(cornerRadius: 15)
                    .fill(AuraPalette.sandSoft)
                    .frame(width: 48, height: 48)
                Image(systemName: "sparkles")
                    .font(.system(size: 19))
                    .foregroundStyle(AuraPalette.blue)
            }

            VStack(alignment: .leading, spacing: 0) {
                Text(appointment.service)
                    .font(AuraFont.titleMedium())
                    .fontWeight(.semibold)
                    .foregroundStyle(AuraPalette.ink)
                    .padding(.bottom, 3)
                Text("Con \(appointment.specialistName)")
                    .font(AuraFont.bodyMedium())
                    .foregroundStyle(AuraPalette.inkMuted)
                    .padding(.bottom, 6)
                EyebrowText(text: "\(appointment.dateLabel) · \(appointment.time)")
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            AppointmentStatusPill(status: appointment.status)
        }
        .padding(16)
        .frame(maxWidth: .infinity)
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
    }
}
