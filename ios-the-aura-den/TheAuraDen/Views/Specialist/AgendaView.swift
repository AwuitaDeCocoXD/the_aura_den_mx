import SwiftUI

/// Weekly agenda with the selected day's appointments.
struct AgendaView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    let onBack: () -> Void
    let onOpenAppointment: (String) -> Void
    let onScheduleClient: () -> Void

    @Environment(DemoStore.self) private var store

    var body: some View {
        let appointments = store.agendaForSelectedDay
        let nextFreeSlot = DemoData.timeSlots.first { slot in
            !appointments.contains { $0.time == slot }
        }

        AuraTabScaffold(
            role: .specialist,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: "Mi agenda",
                eyebrow: store.profile.name,
                subtitle: store.selectedDay.fullLabel,
                onBack: onBack,
                trailing: {
                    ZStack {
                        RoundedRectangle(cornerRadius: 14)
                            .fill(AuraPalette.white.opacity(0.14))
                            .frame(width: 42, height: 42)
                        Image(systemName: "calendar")
                            .font(.system(size: 18))
                            .foregroundStyle(AuraPalette.yellow)
                    }
                }
            )
        } content: {
            ZStack(alignment: .bottomTrailing) {
                ScrollView {
                    VStack(alignment: .leading, spacing: 0) {
                        Spacer().frame(height: 16)

                        ScrollView(.horizontal, showsIndicators: false) {
                            HStack(spacing: 10) {
                                ForEach(DemoData.weekDays) { day in
                                    let hasAppointments = store.appointments.contains {
                                        $0.dayID == day.id && $0.status == .confirmed
                                    }
                                    DayPill(
                                        weekday: day.weekdayShort,
                                        day: day.dayNumber,
                                        isSelected: day.id == store.selectedDayID,
                                        hasDot: hasAppointments,
                                        action: { store.selectDay(day.id) }
                                    )
                                }
                            }
                            .padding(.horizontal, 20)
                        }

                        Spacer().frame(height: 22)

                        VStack(alignment: .leading, spacing: 4) {
                            Text(appointments.isEmpty ? "Sin citas" : "\(appointments.count) citas hoy")
                                .font(AuraFont.titleLarge())
                                .foregroundStyle(AuraPalette.navy)
                            if !appointments.isEmpty, let nextFreeSlot {
                                Text("Tu siguiente espacio libre es a las \(nextFreeSlot)")
                                    .font(AuraFont.bodyMedium())
                                    .foregroundStyle(AuraPalette.inkMuted)
                            }
                        }
                        .padding(.horizontal, 20)

                        Spacer().frame(height: 14)

                        if appointments.isEmpty {
                            AuraEmptyState(
                                title: "No tienes citas hoy",
                                message: "Aprovecha para agendar una clienta o rentar tu espacio en otro horario.",
                                symbol: "chair.lounge.fill",
                                actionLabel: "Agendar clienta",
                                action: onScheduleClient
                            )
                        } else {
                            VStack(spacing: 12) {
                                ForEach(appointments) { appointment in
                                    AgendaAppointmentCard(appointment: appointment) {
                                        onOpenAppointment(appointment.id)
                                    }
                                }
                            }
                            .padding(.horizontal, 20)
                        }

                        Spacer().frame(height: 96)
                    }
                }

                Button {
                    AuraHaptics.tap()
                    onScheduleClient()
                } label: {
                    HStack(spacing: 10) {
                        Image(systemName: "person.badge.plus")
                            .font(.system(size: 17, weight: .medium))
                        Text("Agendar clienta")
                            .font(AuraFont.labelLarge())
                    }
                    .foregroundStyle(AuraPalette.white)
                    .padding(.horizontal, 20)
                    .frame(height: 56)
                    .background(AuraPalette.blue, in: .rect(cornerRadius: 18))
                    .shadow(color: AuraPalette.navy.opacity(0.25), radius: 10, x: 0, y: 5)
                }
                .buttonStyle(PressableButtonStyle())
                .padding(.trailing, 20)
                .padding(.bottom, 18)
            }
        }
    }
}

private struct AgendaAppointmentCard: View {
    let appointment: Appointment
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            HStack(spacing: 0) {
                VStack(spacing: 0) {
                    Text(appointment.time.components(separatedBy: " ").first ?? appointment.time)
                        .font(AuraFont.titleLarge())
                        .foregroundStyle(AuraPalette.navy)
                    Text(appointment.time.components(separatedBy: " ").dropFirst().joined(separator: " "))
                        .font(AuraFont.labelSmall())
                        .tracking(0.6)
                        .foregroundStyle(AuraPalette.blue)
                }
                .padding(.vertical, 14)
                .frame(width: 76)
                .background(AuraPalette.blueSoft, in: .rect(cornerRadius: 16))
                .padding(.trailing, 14)

                VStack(alignment: .leading, spacing: 0) {
                    Text(appointment.clientName)
                        .font(AuraFont.titleMedium())
                        .fontWeight(.semibold)
                        .foregroundStyle(AuraPalette.ink)
                        .padding(.bottom, 3)
                    Text(appointment.service)
                        .font(AuraFont.bodyMedium())
                        .foregroundStyle(AuraPalette.blue)
                        .padding(.bottom, 6)
                    EyebrowText(text: appointment.stationName)
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                Image(systemName: "chevron.right")
                    .font(.system(size: 15, weight: .medium))
                    .foregroundStyle(AuraPalette.inkMuted)
            }
            .padding(16)
            .frame(maxWidth: .infinity)
            .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
        }
        .buttonStyle(PressableButtonStyle())
    }
}
