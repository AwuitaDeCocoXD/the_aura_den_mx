import SwiftUI

/// Entry point: brand splash, unauthenticated flow, then the role shell.
struct RootView: View {
    @State private var store = DemoStore()
    @State private var showRoleSheet = false
    @State private var showSplash = true

    var body: some View {
        Group {
            if showSplash {
                SplashView { showSplash = false }
                    .transition(.opacity)
            } else if store.isSignedIn {
                RoleShellView(showRoleSheet: $showRoleSheet)
                    .transition(.opacity)
            } else {
                AuthFlowView()
                    .transition(.opacity)
            }
        }
        .environment(store)
        .animation(.easeInOut(duration: 0.32), value: showSplash)
        .animation(.easeInOut(duration: 0.28), value: store.isSignedIn)
        .sheet(isPresented: $showRoleSheet) {
            RoleSwitcherSheet(
                currentRole: store.role,
                onSelect: { role in store.role = role },
                onResetDemo: {
                    store.resetDemo()
                }
            )
        }
        .tint(AuraPalette.blue)
    }
}

// MARK: - Auth

private enum AuthRoute: Hashable {
    case login
    case createAccount(SignUpMode)
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
                onCreateAccount: { path.append(.createAccount(.specialist)) },
                onCreateGuestAccount: { path.append(.createAccount(.guest)) },
                onSignIn: { path.append(.login) }
            )
            .toolbar(.hidden, for: .navigationBar)
            .navigationDestination(for: AuthRoute.self) { route in
                switch route {
                case .login:
                    LoginView(
                        onBack: { path.removeLast() },
                        onSignedIn: { signIn(as: .specialist) },
                        onCreateAccount: { path.append(.createAccount(.specialist)) },
                        onCreateGuestAccount: { path.append(.createAccount(.guest)) }
                    )

                case .createAccount(let mode):
                    CreateAccountView(
                        initialMode: mode,
                        onBack: { path.removeLast() },
                        onRegistered: { name in
                            store.startContractFlow(name: name)
                            path.append(.contractRead)
                        },
                        onRegisteredGuest: { name in
                            store.startGuestAccount(name: name)
                            signIn(as: .client)
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
                        ContractSignedView(contract: contract) { signIn(as: .specialist) }
                    }
                }
            }
        }
    }

    private func signIn(as role: UserRole) {
        AuraHaptics.success()
        store.role = role
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
        .onAppear { currentRoute = startRouteForRole(store.role) }
        .onChange(of: store.role) { _, role in
            path.removeAll()
            currentRoute = startRouteForRole(role)
        }
    }

    private func openTab(_ route: AuraTabRoute) {
        path.removeAll()
        currentRoute = route
    }

    /// Back arrow of a secondary tab: always returns to the home of the active role.
    private func backToRoleHome() {
        openTab(startRouteForRole(store.role))
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
                onOpenNotifications: { path.append(.notifications) },
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
                onBack: backToRoleHome,
                onOpenAppointment: { path.append(.appointmentDetail(id: $0)) },
                onScheduleClient: { path.append(.scheduleClient) }
            )

        case .specialistSpaces:
            SpacesView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onBack: backToRoleHome,
                onOpenStation: { path.append(.reserveSpace(stationID: $0)) },
                onOpenMemberships: { path.append(.memberships) },
                onSignContract: { path.append(.contractRead) }
            )

        case .specialistPayments:
            PaymentsView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onBack: backToRoleHome,
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
                onOpenNotifications: { path.append(.notifications) },
                onOpenRoleSwitcher: { showRoleSheet = true }
            )

        case .clientExplore:
            ClientExploreView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onBack: backToRoleHome,
                onBookWith: { path.append(.booking(specialistID: $0)) }
            )

        case .clientHistory:
            ClientHistoryView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onBack: backToRoleHome,
                onReview: { path.append(.review(appointmentID: $0)) },
                onExplore: { openTab(.clientExplore) }
            )

        case .clientProfile:
            ClientProfileView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onBack: backToRoleHome,
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
                onOpenRoleSwitcher: { showRoleSheet = true },
                onOpenNotifications: { path.append(.notifications) }
            )

        case .receptionCheckIn:
            ReceptionCheckInView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onBack: backToRoleHome,
                onOpenWalkIn: { openTab(.receptionWalkIn) }
            )

        case .receptionWalkIn:
            ReceptionWalkInView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onBack: backToRoleHome,
                onRegistered: { openTab(.receptionCheckIn) }
            )

        case .adminLive:
            AdminLiveView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onOpenSpecialists: { openTab(.adminSpecialists) },
                onOpenRoleSwitcher: { showRoleSheet = true },
                onOpenNotifications: { path.append(.notifications) }
            )

        case .adminSpecialists:
            AdminSpecialistsView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onBack: backToRoleHome
            )

        case .adminReports:
            AdminReportsView(
                currentRoute: currentRoute,
                onTabSelected: openTab,
                onBack: backToRoleHome
            )
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

        case .notifications:
            NotificationsView(onBack: { path.removeLast() })

        case .booking(let specialistID):
            BookingView(
                specialistID: specialistID,
                onBack: { path.removeLast() },
                onConfirm: { service, dayID, time in
                    store.bookGuestAppointment(
                        specialist: DemoData.specialist(id: specialistID),
                        service: service,
                        dayID: dayID,
                        time: time
                    )
                    path = [.success(.booking)]
                }
            )

        case .review(let appointmentID):
            ReviewView(
                appointmentID: appointmentID,
                onBack: { path.removeLast() },
                onSubmit: {
                    store.submitReview(appointmentID: appointmentID)
                    path = [.success(.review)]
                }
            )

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
                    switch kind {
                    case .appointment: currentRoute = .specialistAgenda
                    case .booking, .review: currentRoute = .clientExplore
                    default: currentRoute = .specialistSpaces
                    }
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
