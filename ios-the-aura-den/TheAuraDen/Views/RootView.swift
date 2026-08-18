import SwiftUI

/// Entry point: unauthenticated flow, then the role shell with the brand bottom bar.
struct RootView: View {
    @State private var store = DemoStore()
    @State private var showRoleSheet = false

    var body: some View {
        Group {
            if store.isSignedIn {
                RoleShellView(showRoleSheet: $showRoleSheet)
                    .transition(.opacity)
            } else {
                AuthFlowView()
                    .transition(.opacity)
            }
        }
        .environment(store)
        .animation(.easeInOut(duration: 0.28), value: store.isSignedIn)
        .sheet(isPresented: $showRoleSheet) {
            RoleSwitcherSheet(currentRole: store.role) { role in
                store.role = role
            }
        }
        .tint(AuraPalette.blue)
    }
}

// MARK: - Auth

private enum AuthRoute: Hashable {
    case login
    case createAccount
    case legal
    case contractRead
    case contractSign
    case contractSigned
}

private struct AuthFlowView: View {
    @Environment(DemoStore.self) private var store
    @State private var path: [AuthRoute] = []

    var body: some View {
        NavigationStack(path: $path) {
            WelcomeView(
                onCreateAccount: { path.append(.createAccount) },
                onSignIn: { path.append(.login) }
            )
            .toolbar(.hidden, for: .navigationBar)
            .navigationDestination(for: AuthRoute.self) { route in
                switch route {
                case .login:
                    LoginView(
                        onBack: { path.removeLast() },
                        onSignedIn: { signIn() },
                        onCreateAccount: { path.append(.createAccount) }
                    )
                case .createAccount:
                    CreateAccountView(
                        onBack: { path.removeLast() },
                        onRegistered: { name in
                            store.startContractFlow(name: name)
                            path.append(.contractRead)
                        },
                        onSignIn: { path.append(.login) },
                        onOpenLegal: { path.append(.legal) }
                    )
                case .legal:
                    LegalView(onBack: { path.removeLast() })
                case .contractRead:
                    ContractReadView(
                        onBack: { path.removeLast() },
                        onContinue: { path.append(.contractSign) }
                    )
                case .contractSign:
                    ContractSignView(
                        onBack: { path.removeLast() },
                        onSign: { name in
                            store.signContract(name: name)
                            path.append(.contractSigned)
                        }
                    )
                case .contractSigned:
                    if let contract = store.signedContract {
                        ContractSignedView(contract: contract) { signIn() }
                    }
                }
            }
        }
    }

    private func signIn() {
        AuraHaptics.success()
        store.role = .specialist
        store.isSignedIn = true
        path.removeAll()
    }
}

// MARK: - Role shell

private struct RoleShellView: View {
    @Environment(DemoStore.self) private var store
    @Binding var showRoleSheet: Bool

    @State private var currentRoute: AuraTabRoute = .specialistHome
    @State private var path: [AuraRoute] = []

    var body: some View {
        NavigationStack(path: $path) {
            tabRoot
                .toolbar(.hidden, for: .navigationBar)
                .navigationDestination(for: AuraRoute.self, destination: destination)
        }
        .onChange(of: store.role) { _, role in
            path.removeAll()
            currentRoute = startRouteForRole(role)
        }
    }

    private func openTab(_ route: AuraTabRoute) {
        path.removeAll()
        currentRoute = route
    }

    private func goToRoleStart() {
        path.removeAll()
        currentRoute = startRouteForRole(store.role)
    }

    private func signOut() {
        path.removeAll()
        store.signOut()
    }

    // MARK: Tab roots

    @ViewBuilder
    private var tabRoot: some View {
        switch currentRoute {
        case .specialistHome:
            SpecialistHomeView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onOpenProfile: { path.append(.profile) },
                onOpenRoleSwitcher: { showRoleSheet = true },
                onOpenAgenda: { openTab(.specialistAgenda) },
                onOpenSpaces: { openTab(.specialistSpaces) },
                onScheduleClient: { path.append(.scheduleClient) },
                onOpenPayments: { openTab(.specialistPayments) },
                onOpenAppointment: { path.append(.appointmentDetail(id: $0)) }
            )

        case .specialistAgenda:
            AgendaView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onOpenAppointment: { path.append(.appointmentDetail(id: $0)) },
                onScheduleClient: { path.append(.scheduleClient) }
            )

        case .specialistSpaces:
            SpacesView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onOpenStation: { path.append(.reserveSpace(stationID: $0)) },
                onOpenMemberships: { path.append(.memberships) },
                onSignContract: { path.append(.contractRead) }
            )

        case .specialistPayments:
            PaymentsView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onChangePlan: { path.append(.memberships) },
                onOpenReceipt: { path.append(.receipt(paymentID: $0)) }
            )

        case .clientAppointment:
            ClientAppointmentView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onExplore: { openTab(.clientExplore) },
                onPayService: { specialistID, serviceID in
                    path.append(.serviceCheckout(specialistID: specialistID, serviceID: serviceID))
                },
                onOpenRoleSwitcher: { showRoleSheet = true }
            )

        case .clientExplore:
            ClientExploreView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onBookWith: { specialistID, serviceID in
                    path.append(.serviceCheckout(specialistID: specialistID, serviceID: serviceID))
                }
            )

        case .clientHistory:
            ClientHistoryView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onExplore: { openTab(.clientExplore) }
            )

        case .clientProfile:
            ClientProfileView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onOpenLegal: { path.append(.legal) },
                onSignOut: { signOut() },
                onDeleteAccount: { signOut() },
                onOpenRoleSwitcher: { showRoleSheet = true }
            )

        case .receptionToday:
            ReceptionTodayView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onOpenCheckIn: { openTab(.receptionCheckIn) },
                onOpenWalkIn: { openTab(.receptionWalkIn) },
                onOpenRoleSwitcher: { showRoleSheet = true }
            )

        case .receptionCheckIn:
            ReceptionCheckInView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onOpenWalkIn: { openTab(.receptionWalkIn) }
            )

        case .receptionWalkIn:
            ReceptionWalkInView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onRegistered: { openTab(.receptionCheckIn) }
            )

        case .adminLive:
            AdminLiveView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onOpenSpecialists: { openTab(.adminSpecialists) },
                onOpenRoleSwitcher: { showRoleSheet = true }
            )

        case .adminSpecialists:
            AdminSpecialistsView(currentRoute: currentRoute, onTabSelected: openTab)

        case .adminReports:
            AdminReportsView(currentRoute: currentRoute, onTabSelected: openTab)
        }
    }

    // MARK: Push destinations

    @ViewBuilder
    private func destination(for route: AuraRoute) -> some View {
        switch route {
        case .reserveSpace(let stationID):
            ReserveSpaceView(
                stationID: stationID,
                onBack: { path.removeLast() },
                onConfirm: { kind in
                    path.append(.success(kind))
                }
            )

        case .memberships:
            MembershipsView(
                onBack: { path.removeLast() },
                onChoosePlan: { path.append(.membershipCheckout(planID: $0)) }
            )

        case .scheduleClient:
            ScheduleClientView(
                onBack: { path.removeLast() },
                onConfirm: { path.append(.success(.appointment)) }
            )

        case .appointmentDetail(let id):
            AppointmentDetailView(appointmentID: id, onBack: { path.removeLast() })

        case .profile:
            ProfileView(
                onBack: { path.removeLast() },
                onEditProfile: { path.append(.editProfile) },
                onOpenContract: { path.append(.myContract) },
                onOpenLegal: { path.append(.legal) },
                onOpenRoleSwitcher: { showRoleSheet = true },
                onSignOut: { signOut() },
                onDeleteAccount: { signOut() }
            )

        case .editProfile:
            EditProfileView(onBack: { path.removeLast() })

        case .legal:
            LegalView(onBack: { path.removeLast() })

        case .contractRead:
            ContractReadView(
                onBack: { path.removeLast() },
                onContinue: { path.append(.contractSign) }
            )

        case .contractSign:
            ContractSignView(
                onBack: { path.removeLast() },
                onSign: { name in
                    store.signContract(name: name)
                    path.append(.contractSigned)
                }
            )

        case .contractSigned:
            if let contract = store.signedContract {
                ContractSignedView(contract: contract) { path.removeAll() }
            }

        case .contractReview:
            ContractReadView(
                onBack: { path.removeLast() },
                onContinue: { path.removeLast() },
                isReadOnly: true
            )

        case .myContract:
            MyContractView(
                onBack: { path.removeLast() },
                onReadContract: { path.append(.contractReview) },
                onSignContract: { path.append(.contractRead) }
            )

        case .success(let kind):
            SuccessView(
                kind: kind,
                onPrimary: { goToRoleStart() },
                onSecondary: {
                    path.removeAll()
                    currentRoute = kind == .appointment ? .specialistAgenda : .specialistSpaces
                }
            )

        case .membershipCheckout(let planID):
            MembershipCheckoutView(
                planID: planID,
                onBack: { path.removeLast() },
                onPaid: { paymentID in
                    path = [.receipt(paymentID: paymentID)]
                }
            )

        case .serviceCheckout(let specialistID, let serviceID):
            ServiceCheckoutView(
                specialistID: specialistID,
                serviceID: serviceID,
                onBack: { path.removeLast() },
                onPaid: { paymentID in
                    path = [.receipt(paymentID: paymentID)]
                }
            )

        case .receipt(let paymentID):
            ReceiptView(
                paymentID: paymentID,
                onBack: { path.removeLast() },
                onPrimary: { kind in
                    path.removeAll()
                    currentRoute = kind == .membership
                        ? .specialistPayments
                        : startRouteForRole(store.role)
                },
                onRetry: { kind in
                    if kind == .membership {
                        path = [.membershipCheckout(planID: store.activePlanID)]
                    } else {
                        path.removeLast()
                    }
                }
            )
        }
    }
}
