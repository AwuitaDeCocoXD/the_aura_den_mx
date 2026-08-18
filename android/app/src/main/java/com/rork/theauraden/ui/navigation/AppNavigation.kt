package com.rork.theauraden.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rork.theauraden.data.DemoData
import com.rork.theauraden.data.DemoViewModel
import com.rork.theauraden.data.PaymentKind
import com.rork.theauraden.data.StationStatus
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.components.RoleSwitcherSheet
import com.rork.theauraden.ui.screens.LegalScreen
import com.rork.theauraden.ui.screens.SuccessScreen
import com.rork.theauraden.ui.screens.admin.AdminLiveScreen
import com.rork.theauraden.ui.screens.admin.AdminReportsScreen
import com.rork.theauraden.ui.screens.admin.AdminSpecialistsScreen
import com.rork.theauraden.ui.screens.auth.CreateAccountScreen
import com.rork.theauraden.ui.screens.auth.LoginScreen
import com.rork.theauraden.ui.screens.auth.WelcomeScreen
import com.rork.theauraden.ui.screens.checkout.MembershipCheckoutScreen
import com.rork.theauraden.ui.screens.checkout.ReceiptScreen
import com.rork.theauraden.ui.screens.checkout.ServiceCheckoutScreen
import com.rork.theauraden.ui.screens.client.ClientAppointmentScreen
import com.rork.theauraden.ui.screens.client.ClientExploreScreen
import com.rork.theauraden.ui.screens.client.ClientHistoryScreen
import com.rork.theauraden.ui.screens.client.ClientProfileScreen
import com.rork.theauraden.ui.screens.contract.ContractReadScreen
import com.rork.theauraden.ui.screens.contract.ContractSignScreen
import com.rork.theauraden.ui.screens.contract.ContractSignedScreen
import com.rork.theauraden.ui.screens.contract.MyContractScreen
import com.rork.theauraden.ui.screens.reception.ReceptionCheckInScreen
import com.rork.theauraden.ui.screens.reception.ReceptionTodayScreen
import com.rork.theauraden.ui.screens.reception.ReceptionWalkInScreen
import com.rork.theauraden.ui.screens.specialist.AgendaScreen
import com.rork.theauraden.ui.screens.specialist.AppointmentDetailScreen
import com.rork.theauraden.ui.screens.specialist.EditProfileScreen
import com.rork.theauraden.ui.screens.specialist.MembershipsScreen
import com.rork.theauraden.ui.screens.specialist.PaymentsScreen
import com.rork.theauraden.ui.screens.specialist.ProfileScreen
import com.rork.theauraden.ui.screens.specialist.ReserveSpaceScreen
import com.rork.theauraden.ui.screens.specialist.ScheduleClientScreen
import com.rork.theauraden.ui.screens.specialist.SpacesScreen
import com.rork.theauraden.ui.screens.specialist.SpecialistHomeScreen

private const val SUCCESS_RESERVATION = "reservation"
private const val SUCCESS_APPOINTMENT = "appointment"
private const val SUCCESS_NOTIFY = "notify"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: DemoViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route.orEmpty()
    var showRoleSheet by remember { mutableStateOf(false) }

    fun openTab(route: String) {
        navController.navigate(route) {
            popUpTo(startRouteForRole(state.role)) {
                saveState = true
                inclusive = route == startRouteForRole(state.role)
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun signOutToWelcome() {
        navController.navigate(Routes.WELCOME) {
            popUpTo(Routes.WELCOME) { inclusive = true }
        }
    }

    fun goToRoleStart(role: UserRole) {
        navController.navigate(startRouteForRole(role)) {
            popUpTo(Routes.WELCOME)
            launchSingleTop = true
        }
    }

    NavHost(navController = navController, startDestination = Routes.WELCOME) {

        composable(Routes.WELCOME) {
            WelcomeScreen(
                onCreateAccount = { navController.navigate(Routes.CREATE_ACCOUNT) },
                onSignIn = { navController.navigate(Routes.LOGIN) }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onSignedIn = {
                    viewModel.setRole(UserRole.SPECIALIST)
                    goToRoleStart(UserRole.SPECIALIST)
                },
                onCreateAccount = { navController.navigate(Routes.CREATE_ACCOUNT) }
            )
        }

        composable(Routes.CREATE_ACCOUNT) {
            CreateAccountScreen(
                onBack = { navController.popBackStack() },
                onRegistered = { name ->
                    viewModel.setRole(UserRole.SPECIALIST)
                    viewModel.startContractFlow(name)
                    navController.navigate(Routes.CONTRACT_READ)
                },
                onSignIn = { navController.navigate(Routes.LOGIN) },
                onOpenLegal = { navController.navigate(Routes.LEGAL) }
            )
        }

        composable(Routes.CONTRACT_READ) {
            ContractReadScreen(
                signerName = state.contractSignerName,
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate(Routes.CONTRACT_SIGN) }
            )
        }

        composable(Routes.CONTRACT_SIGN) {
            ContractSignScreen(
                defaultName = state.contractSignerName,
                onBack = { navController.popBackStack() },
                onSign = { name ->
                    viewModel.signContract(name)
                    navController.navigate(Routes.CONTRACT_SIGNED) {
                        popUpTo(Routes.CONTRACT_READ) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.CONTRACT_SIGNED) {
            val contract = state.signedContract
            if (contract == null) {
                navController.popBackStack()
            } else {
                ContractSignedScreen(
                    contract = contract,
                    onContinue = {
                        viewModel.setRole(UserRole.SPECIALIST)
                        goToRoleStart(UserRole.SPECIALIST)
                    }
                )
            }
        }

        composable(Routes.CONTRACT_REVIEW) {
            ContractReadScreen(
                signerName = state.contractSignerName,
                onBack = { navController.popBackStack() },
                onContinue = { navController.popBackStack() },
                readOnly = true,
                signedContract = state.signedContract
            )
        }

        composable(Routes.MY_CONTRACT) {
            MyContractScreen(
                contract = state.signedContract,
                planName = state.activePlan.name,
                onBack = { navController.popBackStack() },
                onReadContract = { navController.navigate(Routes.CONTRACT_REVIEW) },
                onSignContract = { navController.navigate(Routes.CONTRACT_READ) }
            )
        }

        composable(Routes.LEGAL) {
            LegalScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.SPECIALIST_HOME) {
            SpecialistHomeScreen(
                state = state,
                currentRoute = currentRoute,
                onTabSelected = ::openTab,
                onOpenProfile = { navController.navigate(Routes.PROFILE) },
                onOpenRoleSwitcher = { showRoleSheet = true },
                onOpenAgenda = { openTab(Routes.SPECIALIST_AGENDA) },
                onOpenSpaces = { openTab(Routes.SPECIALIST_SPACES) },
                onScheduleClient = { navController.navigate(Routes.SCHEDULE_CLIENT) },
                onOpenPayments = { openTab(Routes.SPECIALIST_PAYMENTS) },
                onOpenAppointment = { id ->
                    navController.navigate(Routes.appointmentDetail(id))
                }
            )
        }

        composable(Routes.SPECIALIST_AGENDA) {
            AgendaScreen(
                state = state,
                currentRoute = currentRoute,
                onTabSelected = ::openTab,
                onSelectDay = viewModel::selectDay,
                onOpenAppointment = { id ->
                    navController.navigate(Routes.appointmentDetail(id))
                },
                onScheduleClient = { navController.navigate(Routes.SCHEDULE_CLIENT) }
            )
        }

        composable(Routes.SPECIALIST_SPACES) {
            SpacesScreen(
                currentRoute = currentRoute,
                onTabSelected = ::openTab,
                hasSignedContract = state.hasSignedContract,
                onOpenStation = { id -> navController.navigate(Routes.reserveSpace(id)) },
                onOpenMemberships = { navController.navigate(Routes.MEMBERSHIPS) },
                onSignContract = { navController.navigate(Routes.CONTRACT_READ) }
            )
        }

        composable(Routes.SPECIALIST_PAYMENTS) {
            PaymentsScreen(
                state = state,
                currentRoute = currentRoute,
                onTabSelected = ::openTab,
                onChangePlan = { navController.navigate(Routes.MEMBERSHIPS) },
                onOpenReceipt = { id -> navController.navigate(Routes.receipt(id)) }
            )
        }

        composable("${Routes.RESERVE_SPACE}/{stationId}") { entry ->
            val stationId = entry.arguments?.getString("stationId").orEmpty()
            ReserveSpaceScreen(
                stationId = stationId,
                onBack = { navController.popBackStack() },
                onConfirm = { stationName, dayId, time ->
                    viewModel.reserveStation(stationName, dayId, time)
                    val isAvailable = DemoData.stationById(stationId).status ==
                        StationStatus.AVAILABLE
                    navController.navigate(
                        Routes.success(
                            if (isAvailable) SUCCESS_RESERVATION else SUCCESS_NOTIFY
                        )
                    )
                }
            )
        }

        composable(Routes.MEMBERSHIPS) {
            MembershipsScreen(
                activePlanId = state.activePlanId,
                onBack = { navController.popBackStack() },
                onChoosePlan = { planId ->
                    navController.navigate(Routes.membershipCheckout(planId))
                }
            )
        }

        composable(Routes.SCHEDULE_CLIENT) {
            ScheduleClientScreen(
                specialistName = state.profile.name,
                onBack = { navController.popBackStack() },
                onConfirm = { clientName, service, dayId, time, station, notes ->
                    viewModel.addAppointment(clientName, service, dayId, time, station, notes)
                    navController.navigate(Routes.success(SUCCESS_APPOINTMENT))
                }
            )
        }

        composable("${Routes.APPOINTMENT_DETAIL}/{appointmentId}") { entry ->
            val id = entry.arguments?.getString("appointmentId").orEmpty()
            val appointment = viewModel.appointmentById(id)
            if (appointment == null) {
                navController.popBackStack()
            } else {
                AppointmentDetailScreen(
                    appointment = appointment,
                    onBack = { navController.popBackStack() },
                    onReschedule = { time ->
                        viewModel.rescheduleAppointment(id, time, appointment.dayId)
                        navController.popBackStack()
                    },
                    onCancel = {
                        viewModel.cancelAppointment(id)
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                profile = state.profile,
                onBack = { navController.popBackStack() },
                onEditProfile = { navController.navigate(Routes.EDIT_PROFILE) },
                contract = state.signedContract,
                onOpenContract = { navController.navigate(Routes.MY_CONTRACT) },
                onOpenLegal = { navController.navigate(Routes.LEGAL) },
                onSignOut = ::signOutToWelcome,
                onDeleteAccount = ::signOutToWelcome,
                onOpenRoleSwitcher = { showRoleSheet = true }
            )
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(
                profile = state.profile,
                onBack = { navController.popBackStack() },
                onSave = { name, phone, email, specialty ->
                    viewModel.updateProfile(name, phone, email, specialty)
                    navController.popBackStack()
                }
            )
        }

        composable("${Routes.SUCCESS}/{kind}") { entry ->
            val kind = entry.arguments?.getString("kind").orEmpty()
            val isReservation = kind == SUCCESS_RESERVATION
            val isNotify = kind == SUCCESS_NOTIFY
            SuccessScreen(
                title = if (isNotify) "Te avisamos" else "¡Listo!",
                message = when {
                    isNotify -> "En cuanto la estación se libere recibirás un aviso para " +
                        "reservarla."
                    isReservation -> "Tu espacio quedó reservado. Te esperamos en The Aura Den."
                    else -> "La cita quedó agendada y tu clienta ya recibió su confirmación."
                },
                detail = if (isReservation || isNotify) {
                    state.lastReservation
                } else {
                    state.agendaForSelectedDay.lastOrNull()?.let {
                        "${it.clientName} · ${it.time} · ${it.stationName}"
                    }
                },
                primaryLabel = "Volver al inicio",
                onPrimary = { goToRoleStart(UserRole.SPECIALIST) },
                secondaryLabel = if (isReservation || isNotify) {
                    "Ver mis espacios"
                } else {
                    "Ver mi agenda"
                },
                onSecondary = {
                    navController.navigate(
                        if (isReservation || isNotify) {
                            Routes.SPECIALIST_SPACES
                        } else {
                            Routes.SPECIALIST_AGENDA
                        }
                    ) {
                        popUpTo(Routes.WELCOME)
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("${Routes.MEMBERSHIP_CHECKOUT}/{planId}") { entry ->
            val planId = entry.arguments?.getString("planId").orEmpty()
            MembershipCheckoutScreen(
                planId = planId,
                onBack = { navController.popBackStack() },
                onPay = { method, invoice ->
                    val paymentId = viewModel.payMembership(planId, method, invoice)
                    navController.navigate(Routes.receipt(paymentId)) {
                        popUpTo(Routes.MEMBERSHIPS) { inclusive = true }
                    }
                }
            )
        }

        composable("${Routes.SERVICE_CHECKOUT}/{specialistId}/{serviceId}") { entry ->
            val specialistId = entry.arguments?.getString("specialistId").orEmpty()
            val serviceId = entry.arguments?.getString("serviceId").orEmpty()
            val specialist = DemoData.specialistById(specialistId)
            val service = DemoData.services.firstOrNull { it.id == serviceId }
                ?: DemoData.services.first()
            ServiceCheckoutScreen(
                specialist = specialist,
                service = service,
                onBack = { navController.popBackStack() },
                onPay = { method, invoice ->
                    val paymentId = viewModel.payService(service, specialist, method, invoice)
                    navController.navigate(Routes.receipt(paymentId)) {
                        popUpTo(startRouteForRole(UserRole.CLIENT))
                    }
                }
            )
        }

        composable("${Routes.RECEIPT}/{paymentId}") { entry ->
            val paymentId = entry.arguments?.getString("paymentId").orEmpty()
            val payment = viewModel.paymentById(paymentId)
            if (payment == null) {
                navController.popBackStack()
            } else {
                ReceiptScreen(
                    payment = payment,
                    onBack = { navController.popBackStack() },
                    onPrimary = {
                        if (payment.kind == PaymentKind.MEMBERSHIP) {
                            navController.navigate(Routes.SPECIALIST_PAYMENTS) {
                                popUpTo(Routes.WELCOME)
                                launchSingleTop = true
                            }
                        } else {
                            goToRoleStart(UserRole.CLIENT)
                        }
                    },
                    onRetry = {
                        if (payment.kind == PaymentKind.MEMBERSHIP) {
                            navController.navigate(
                                Routes.membershipCheckout(state.activePlanId)
                            )
                        } else {
                            navController.popBackStack()
                        }
                    }
                )
            }
        }

        composable(Routes.CLIENT_APPOINTMENT) {
            ClientAppointmentScreen(
                appointment = state.clientUpcoming.firstOrNull(),
                currentRoute = currentRoute,
                onTabSelected = ::openTab,
                onCancel = {
                    state.clientAppointment?.let { viewModel.cancelAppointment(it.id) }
                },
                onExplore = { openTab(Routes.CLIENT_EXPLORE) },
                onPayService = {
                    val service = state.clientUpcoming.firstOrNull()?.service
                    val serviceId = DemoData.services
                        .firstOrNull { it.name == service }?.id
                        ?: DemoData.services.first().id
                    navController.navigate(
                        Routes.serviceCheckout(DemoData.currentSpecialist.id, serviceId)
                    )
                },
                onOpenRoleSwitcher = { showRoleSheet = true }
            )
        }

        composable(Routes.CLIENT_EXPLORE) {
            ClientExploreScreen(
                currentRoute = currentRoute,
                onTabSelected = ::openTab,
                onBookWith = { specialistId ->
                    val specialist = DemoData.specialistById(specialistId)
                    val serviceId = DemoData.services
                        .firstOrNull { it.name == specialist.specialty }?.id
                        ?: DemoData.services.first().id
                    navController.navigate(Routes.serviceCheckout(specialistId, serviceId))
                }
            )
        }

        composable(Routes.CLIENT_HISTORY) {
            ClientHistoryScreen(
                upcoming = state.clientUpcoming,
                past = state.clientHistory,
                currentRoute = currentRoute,
                onTabSelected = ::openTab,
                onExplore = { openTab(Routes.CLIENT_EXPLORE) }
            )
        }

        composable(Routes.CLIENT_PROFILE) {
            ClientProfileScreen(
                upcoming = state.clientUpcoming,
                past = state.clientHistory,
                currentRoute = currentRoute,
                onTabSelected = ::openTab,
                onOpenLegal = { navController.navigate(Routes.LEGAL) },
                onSignOut = ::signOutToWelcome,
                onDeleteAccount = ::signOutToWelcome,
                onOpenRoleSwitcher = { showRoleSheet = true }
            )
        }

        composable(Routes.RECEPTION_TODAY) {
            ReceptionTodayScreen(
                state = state,
                currentRoute = currentRoute,
                onTabSelected = ::openTab,
                onOpenCheckIn = { openTab(Routes.RECEPTION_CHECKIN) },
                onOpenWalkIn = { openTab(Routes.RECEPTION_WALKIN) },
                onOpenRoleSwitcher = { showRoleSheet = true }
            )
        }

        composable(Routes.RECEPTION_CHECKIN) {
            ReceptionCheckInScreen(
                state = state,
                currentRoute = currentRoute,
                onTabSelected = ::openTab,
                onMarkArrival = viewModel::markArrival,
                onOpenWalkIn = { openTab(Routes.RECEPTION_WALKIN) }
            )
        }

        composable(Routes.RECEPTION_WALKIN) {
            ReceptionWalkInScreen(
                currentRoute = currentRoute,
                onTabSelected = ::openTab,
                onRegister = { clientName, service, specialist, station ->
                    viewModel.registerWalkIn(clientName, service, specialist, station)
                    openTab(Routes.RECEPTION_CHECKIN)
                }
            )
        }

        composable(Routes.ADMIN_LIVE) {
            AdminLiveScreen(
                currentRoute = currentRoute,
                onTabSelected = ::openTab,
                onOpenSpecialists = { openTab(Routes.ADMIN_SPECIALISTS) },
                onOpenRoleSwitcher = { showRoleSheet = true }
            )
        }

        composable(Routes.ADMIN_SPECIALISTS) {
            AdminSpecialistsScreen(
                currentRoute = currentRoute,
                onTabSelected = ::openTab
            )
        }

        composable(Routes.ADMIN_REPORTS) {
            AdminReportsScreen(
                currentRoute = currentRoute,
                onTabSelected = ::openTab
            )
        }
    }

    if (showRoleSheet) {
        RoleSwitcherSheet(
            currentRole = state.role,
            onSelect = { role ->
                showRoleSheet = false
                viewModel.setRole(role)
                goToRoleStart(role)
            },
            onDismiss = { showRoleSheet = false }
        )
    }
}
