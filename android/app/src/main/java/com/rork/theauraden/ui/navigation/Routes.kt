package com.rork.theauraden.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.HowToReg
import androidx.compose.material.icons.rounded.InsertChartOutlined
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonAddAlt1
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Sensors
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.ui.graphics.vector.ImageVector
import com.rork.theauraden.data.UserRole

object Routes {
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val CREATE_ACCOUNT = "create_account"

    const val NOTIFICATIONS = "notifications"

    const val SPECIALIST_HOME = "specialist/home"
    const val SPECIALIST_AGENDA = "specialist/agenda"
    const val SPECIALIST_SPACES = "specialist/spaces"
    const val SPECIALIST_PAYMENTS = "specialist/payments"

    const val RESERVE_SPACE = "specialist/reserve"
    const val MEMBERSHIPS = "specialist/memberships"
    const val SCHEDULE_CLIENT = "specialist/schedule"
    const val APPOINTMENT_DETAIL = "specialist/appointment"
    const val PROFILE = "specialist/profile"
    const val EDIT_PROFILE = "specialist/profile/edit"

    const val CONTRACT_READ = "contract/read"
    const val CONTRACT_SIGN = "contract/sign"
    const val CONTRACT_SIGNED = "contract/signed"
    const val CONTRACT_REVIEW = "contract/review"
    const val MY_CONTRACT = "specialist/contract"

    const val SUCCESS = "success"
    const val MEMBERSHIP_CHECKOUT = "checkout/membership"
    const val SERVICE_CHECKOUT = "checkout/service"
    const val RECEIPT = "receipt"

    const val CLIENT_APPOINTMENT = "client/appointment"
    const val CLIENT_EXPLORE = "client/explore"
    const val CLIENT_HISTORY = "client/history"
    const val CLIENT_PROFILE = "client/profile"
    const val CLIENT_BOOKING = "client/booking"
    const val CLIENT_REVIEW = "client/review"

    const val LEGAL = "legal"

    const val RECEPTION_TODAY = "reception/today"
    const val RECEPTION_CHECKIN = "reception/checkin"
    const val RECEPTION_WALKIN = "reception/walkin"

    const val ADMIN_LIVE = "admin/live"
    const val ADMIN_SPECIALISTS = "admin/specialists"
    const val ADMIN_REPORTS = "admin/reports"

    fun createAccount(mode: String): String = "$CREATE_ACCOUNT/$mode"
    fun booking(specialistId: String): String = "$CLIENT_BOOKING/$specialistId"
    fun review(appointmentId: String): String = "$CLIENT_REVIEW/$appointmentId"
    fun reserveSpace(stationId: String): String = "$RESERVE_SPACE/$stationId"
    fun appointmentDetail(appointmentId: String): String = "$APPOINTMENT_DETAIL/$appointmentId"
    fun success(kind: String): String = "$SUCCESS/$kind"
    fun membershipCheckout(planId: String): String = "$MEMBERSHIP_CHECKOUT/$planId"
    fun serviceCheckout(specialistId: String, serviceId: String): String =
        "$SERVICE_CHECKOUT/$specialistId/$serviceId"
    fun receipt(paymentId: String): String = "$RECEIPT/$paymentId"
}

/** A bottom navigation destination. */
data class AuraTab(val route: String, val label: String, val icon: ImageVector)

/** Each role only ever sees its own destinations. */
fun tabsForRole(role: UserRole): List<AuraTab> = when (role) {
    UserRole.SPECIALIST -> listOf(
        AuraTab(Routes.SPECIALIST_HOME, "Inicio", Icons.Rounded.Home),
        AuraTab(Routes.SPECIALIST_AGENDA, "Agenda", Icons.Rounded.CalendarMonth),
        AuraTab(Routes.SPECIALIST_SPACES, "Espacios", Icons.Rounded.Chair),
        AuraTab(Routes.SPECIALIST_PAYMENTS, "Pagos", Icons.Rounded.Wallet)
    )
    UserRole.CLIENT -> listOf(
        AuraTab(Routes.CLIENT_APPOINTMENT, "Mi cita", Icons.Rounded.EventAvailable),
        AuraTab(Routes.CLIENT_EXPLORE, "Explorar", Icons.Rounded.Search),
        AuraTab(Routes.CLIENT_HISTORY, "Mis citas", Icons.Rounded.History),
        AuraTab(Routes.CLIENT_PROFILE, "Perfil", Icons.Rounded.Person)
    )
    UserRole.RECEPTION -> listOf(
        AuraTab(Routes.RECEPTION_TODAY, "Hoy", Icons.Rounded.Storefront),
        AuraTab(Routes.RECEPTION_CHECKIN, "Check-in", Icons.Rounded.HowToReg),
        AuraTab(Routes.RECEPTION_WALKIN, "Walk-in", Icons.Rounded.PersonAddAlt1)
    )
    UserRole.ADMIN -> listOf(
        AuraTab(Routes.ADMIN_LIVE, "En vivo", Icons.Rounded.Sensors),
        AuraTab(Routes.ADMIN_SPECIALISTS, "Rentistas", Icons.Rounded.Groups),
        AuraTab(Routes.ADMIN_REPORTS, "Reportes", Icons.Rounded.InsertChartOutlined)
    )
}

fun startRouteForRole(role: UserRole): String = tabsForRole(role).first().route
