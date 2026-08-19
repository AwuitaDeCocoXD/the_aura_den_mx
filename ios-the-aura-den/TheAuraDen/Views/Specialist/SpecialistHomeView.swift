import SwiftUI

/// Specialist dashboard: next appointment first, then the four quick actions.
struct SpecialistHomeView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    let onOpenProfile: () -> Void
    let onOpenRoleSwitcher: () -> Void
    let onOpenNotifications: () -> Void
    let onOpenAgenda: () -> Void
    let onOpenSpaces: () -> Void
    let onScheduleClient: () -> Void
    let onOpenPayments: () -> Void
    let onOpenAppointment: (String) -> Void

    @Environment(DemoStore.self) private var store

    private var todayCount: Int {
        store.appointments.count { $0.dayID == "tue" && $0.status == .confirmed }
    }

    var body: some View {
        let profile = store.profile
        let plan = store.activePlan
        let next = store.nextAppointment

        AuraTabScaffold(
            role: .specialist,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: profile.name,
                eyebrow: "Bienvenida de vuelta"
            ) {
                HStack(spacing: 4) {
                    NotificationBell(
                        unread: store.unreadNotifications,
                        action: onOpenNotifications
                    )
                    AuraAvatar(imageURL: profile.imageURL, size: 48, ringColor: .clear)
                        .frame(width: 52, height: 52)
                        .contentShape(.circle)
                        .onTapGesture { onOpenProfile() }
                        .onLongPressGesture(minimumDuration: 0.45) {
                            AuraHaptics.tap()
                            onOpenRoleSwitcher()
                        }
                        .accessibilityLabel("Mi perfil")
                }
            } content: {
                StatusPill(
                    text: "Especialista · \(profile.specialty)",
                    foreground: AuraPalette.yellow,
                    background: AuraPalette.white.opacity(0.14)
                )
                .frame(maxWidth: .infinity, alignment: .leading)
            }
        } content: {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    Spacer().frame(height: 18)

                    if let next {
                        NextAppointmentCard(
                            service: next.service,
                            clientName: next.clientName,
                            time: next.time,
                            station: next.stationName,
                            action: { onOpenAppointment(next.id) }
                        )
                    } else {
                        AuraCard(padding: 20) {
                            VStack(alignment: .leading, spacing: 0) {
                                EyebrowText(text: "Próxima cita")
                                    .padding(.bottom, 8)
                                Text("Hoy no tienes citas agendadas")
                                    .font(AuraFont.headlineSmall())
                                    .foregroundStyle(AuraPalette.navy)
                                    .padding(.bottom, 4)
                                Text("Aprovecha para agendar a una clienta o rentar tu espacio.")
                                    .font(AuraFont.bodyMedium())
                                    .foregroundStyle(AuraPalette.inkMuted)
                                    .fixedSize(horizontal: false, vertical: true)
                            }
                        }
                    }

                    Spacer().frame(height: 24)

                    EyebrowText(text: "Accesos rápidos")
                        .padding(.bottom, 12)

                    HStack(alignment: .top, spacing: 14) {
                        QuickActionTile(
                            title: "Mi agenda",
                            subtitle: "\(todayCount) citas hoy",
                            symbol: "calendar",
                            action: onOpenAgenda
                        )
                        QuickActionTile(
                            title: "Rentar espacio",
                            subtitle: "Disponible ahora",
                            symbol: "chair.lounge.fill",
                            action: onOpenSpaces
                        )
                    }

                    Spacer().frame(height: 14)

                    HStack(alignment: .top, spacing: 14) {
                        QuickActionTile(
                            title: "Agendar clienta",
                            subtitle: "Dentro de tu horario",
                            symbol: "person.badge.plus",
                            action: onScheduleClient
                        )
                        QuickActionTile(
                            title: "Mis pagos",
                            subtitle: "Al corriente",
                            symbol: "wallet.pass.fill",
                            action: onOpenPayments
                        )
                    }

                    Spacer().frame(height: 24)

                    MembershipSummaryCard(
                        planName: plan.name,
                        hoursUsed: profile.hoursUsed,
                        totalHours: plan.hours,
                        action: onOpenPayments
                    )

                    Spacer().frame(height: 28)
                }
                .padding(.horizontal, 20)
            }
        }
    }
}

private struct NextAppointmentCard: View {
    let service: String
    let clientName: String
    let time: String
    let station: String
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            VStack(alignment: .leading, spacing: 0) {
                HStack(spacing: 8) {
                    Text("Próxima cita")
                        .font(AuraFont.titleMedium())
                        .foregroundStyle(AuraPalette.white)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    ZStack {
                        Circle()
                            .fill(AuraPalette.white.opacity(0.12))
                            .frame(width: 38, height: 38)
                        Image(systemName: "calendar")
                            .font(.system(size: 17))
                            .foregroundStyle(AuraPalette.yellow)
                    }
                }
                .padding(.bottom, 14)

                StatusPill(
                    text: "HOY",
                    foreground: AuraPalette.navy,
                    background: AuraPalette.yellow
                )
                .padding(.bottom, 12)

                Text("\(service) · \(clientName)")
                    .font(AuraFont.headlineMedium())
                    .foregroundStyle(AuraPalette.white)
                    .multilineTextAlignment(.leading)
                    .fixedSize(horizontal: false, vertical: true)
                    .padding(.bottom, 12)

                HStack(spacing: 7) {
                    Image(systemName: "clock")
                        .font(.system(size: 14))
                        .foregroundStyle(AuraPalette.yellow)
                    Text("\(time) · \(station)")
                        .font(AuraFont.titleSmall())
                        .foregroundStyle(AuraPalette.yellow)
                    Spacer(minLength: 8)
                    Image(systemName: "chevron.right")
                        .font(.system(size: 15, weight: .medium))
                        .foregroundStyle(AuraPalette.white.opacity(0.7))
                }
            }
            .padding(20)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(AuraPalette.blue, in: .rect(cornerRadius: AuraRadius.card))
        }
        .buttonStyle(PressableButtonStyle())
    }
}

private struct MembershipSummaryCard: View {
    let planName: String
    let hoursUsed: Int
    let totalHours: Int
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            HStack(alignment: .top, spacing: 10) {
                VStack(alignment: .leading, spacing: 0) {
                    Text("Membresía \(planName)")
                        .font(AuraFont.titleMedium())
                        .fontWeight(.semibold)
                        .foregroundStyle(AuraPalette.sand)
                        .padding(.bottom, 10)

                    Text("\(totalHours - hoursUsed) de \(totalHours) horas disponibles")
                        .font(AuraFont.titleLarge())
                        .foregroundStyle(AuraPalette.navy)
                        .multilineTextAlignment(.leading)
                        .fixedSize(horizontal: false, vertical: true)
                        .padding(.bottom, 12)

                    AuraProgressBar(
                        progress: totalHours == 0 ? 0 : Double(hoursUsed) / Double(totalHours)
                    )
                    .padding(.bottom, 12)

                    HStack(spacing: 6) {
                        Image(systemName: "calendar")
                            .font(.system(size: 13))
                            .foregroundStyle(AuraPalette.sand)
                        Text("Renueva el 15 de septiembre")
                            .font(AuraFont.bodyMedium())
                            .foregroundStyle(AuraPalette.navy)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                AuraCardMark()
                    .frame(width: 56)
            }
            .padding(20)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))
        }
        .buttonStyle(PressableButtonStyle())
    }
}
