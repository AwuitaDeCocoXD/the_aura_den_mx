import SwiftUI

/// A bottom navigation destination.
nonisolated struct AuraTab: Identifiable, Hashable {
    let route: AuraTabRoute
    let label: String
    let symbol: String

    var id: AuraTabRoute { route }
}

nonisolated enum AuraTabRoute: String, Hashable {
    case specialistHome
    case specialistAgenda
    case specialistSpaces
    case specialistPayments
    case clientAppointment
    case clientExplore
    case clientHistory
    case clientProfile
    case receptionToday
    case receptionCheckIn
    case receptionWalkIn
    case adminLive
    case adminSpecialists
    case adminReports
}

/// Each role only ever sees its own destinations.
nonisolated func tabsForRole(_ role: UserRole) -> [AuraTab] {
    switch role {
    case .specialist:
        [
            AuraTab(route: .specialistHome, label: "Inicio", symbol: "house.fill"),
            AuraTab(route: .specialistAgenda, label: "Agenda", symbol: "calendar"),
            AuraTab(route: .specialistSpaces, label: "Espacios", symbol: "chair.lounge.fill"),
            AuraTab(route: .specialistPayments, label: "Pagos", symbol: "wallet.pass.fill")
        ]
    case .client:
        [
            AuraTab(route: .clientAppointment, label: "Mi cita", symbol: "calendar.badge.checkmark"),
            AuraTab(route: .clientExplore, label: "Explorar", symbol: "magnifyingglass"),
            AuraTab(route: .clientHistory, label: "Mis citas", symbol: "clock.arrow.circlepath"),
            AuraTab(route: .clientProfile, label: "Perfil", symbol: "person.fill")
        ]
    case .reception:
        [
            AuraTab(route: .receptionToday, label: "Hoy", symbol: "storefront.fill"),
            AuraTab(route: .receptionCheckIn, label: "Check-in", symbol: "person.crop.circle.badge.checkmark"),
            AuraTab(route: .receptionWalkIn, label: "Walk-in", symbol: "person.badge.plus")
        ]
    case .admin:
        [
            AuraTab(route: .adminLive, label: "En vivo", symbol: "dot.radiowaves.left.and.right"),
            AuraTab(route: .adminSpecialists, label: "Rentistas", symbol: "person.3.fill"),
            AuraTab(route: .adminReports, label: "Reportes", symbol: "chart.bar")
        ]
    }
}

nonisolated func startRouteForRole(_ role: UserRole) -> AuraTabRoute {
    tabsForRole(role)[0].route
}

/// Bottom navigation shown only on top-level destinations of the active role.
struct AuraBottomBar: View {
    let role: UserRole
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void

    var body: some View {
        HStack(spacing: 0) {
            ForEach(tabsForRole(role)) { tab in
                let selected = tab.route == currentRoute
                Button {
                    guard !selected else { return }
                    AuraHaptics.tap()
                    onTabSelected(tab.route)
                } label: {
                    VStack(spacing: 4) {
                        ZStack {
                            Capsule()
                                .fill(selected ? AuraPalette.yellow : Color.clear)
                                .frame(width: 64, height: 32)
                            Image(systemName: tab.symbol)
                                .font(.system(size: 19, weight: .medium))
                                .foregroundStyle(selected ? AuraPalette.navy : AuraPalette.blue)
                        }
                        Text(tab.label)
                            .font(AuraFont.labelSmall())
                            .tracking(0.6)
                            .foregroundStyle(selected ? AuraPalette.navy : AuraPalette.inkMuted)
                            .lineLimit(1)
                    }
                    .frame(maxWidth: .infinity)
                    .contentShape(.rect)
                }
                .buttonStyle(.plain)
                .accessibilityLabel(tab.label)
                .accessibilityAddTraits(selected ? [.isSelected, .isButton] : .isButton)
            }
        }
        .padding(.top, 12)
        .padding(.bottom, 10)
        .frame(maxWidth: .infinity)
        .background(AuraPalette.white)
        .animation(.easeInOut(duration: 0.2), value: currentRoute)
    }
}

/// Scaffold for top-level destinations: header + content + role bottom bar.
struct AuraTabScaffold<Header: View, Content: View>: View {
    let role: UserRole
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    @ViewBuilder var header: Header
    @ViewBuilder var content: Content

    var body: some View {
        VStack(spacing: 0) {
            header
            content
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AuraPalette.canvas)
        .safeAreaInset(edge: .bottom, spacing: 0) {
            AuraBottomBar(
                role: role,
                currentRoute: currentRoute,
                onTabSelected: onTabSelected
            )
        }
        .ignoresSafeArea(edges: .top)
        .toolbar(.hidden, for: .navigationBar)
    }
}

/// Scaffold for detail, form and full-screen destinations: no bottom navigation, so a bottom
/// call to action owns the bottom edge alone.
struct AuraDetailScaffold<Header: View, Content: View, BottomAction: View>: View {
    @ViewBuilder var header: Header
    @ViewBuilder var content: Content
    @ViewBuilder var bottomAction: BottomAction

    var body: some View {
        VStack(spacing: 0) {
            header
            content
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AuraPalette.canvas)
        .safeAreaInset(edge: .bottom, spacing: 0) {
            bottomAction
                .padding(.horizontal, 20)
                .padding(.vertical, 14)
                .frame(maxWidth: .infinity)
                .background(AuraPalette.white)
                .shadow(color: AuraPalette.navy.opacity(0.12), radius: 12, x: 0, y: -4)
        }
        .ignoresSafeArea(edges: .top)
        .toolbar(.hidden, for: .navigationBar)
    }
}

/// Detail scaffold without a bottom call to action.
struct AuraPlainScaffold<Header: View, Content: View>: View {
    @ViewBuilder var header: Header
    @ViewBuilder var content: Content

    var body: some View {
        VStack(spacing: 0) {
            header
            content
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AuraPalette.canvas)
        .ignoresSafeArea(edges: .top)
        .toolbar(.hidden, for: .navigationBar)
    }
}
