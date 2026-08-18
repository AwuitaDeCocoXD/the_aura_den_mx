package com.rork.theauraden.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rork.theauraden.data.UserRole
import com.rork.theauraden.ui.navigation.tabsForRole
import com.rork.theauraden.ui.theme.AuraBlue
import com.rork.theauraden.ui.theme.AuraCanvas
import com.rork.theauraden.ui.theme.AuraInkMuted
import com.rork.theauraden.ui.theme.AuraNavy
import com.rork.theauraden.ui.theme.AuraWhite
import com.rork.theauraden.ui.theme.AuraYellow

/** Bottom navigation shown only on top-level destinations of the active role. */
@Composable
fun AuraBottomBar(
    role: UserRole,
    currentRoute: String?,
    onTabSelected: (String) -> Unit
) {
    NavigationBar(containerColor = AuraWhite, tonalElevation = 0.dp) {
        tabsForRole(role).forEach { tab ->
            val selected = currentRoute == tab.route
            NavigationBarItem(
                selected = selected,
                onClick = { if (!selected) onTabSelected(tab.route) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label
                    )
                },
                label = {
                    Text(text = tab.label, style = MaterialTheme.typography.labelSmall)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AuraNavy,
                    selectedTextColor = AuraNavy,
                    unselectedIconColor = AuraBlue,
                    unselectedTextColor = AuraInkMuted,
                    indicatorColor = AuraYellow
                )
            )
        }
    }
}

/** Scaffold for top-level destinations: header + content + role bottom bar. */
@Composable
fun AuraTabScaffold(
    role: UserRole,
    currentRoute: String?,
    onTabSelected: (String) -> Unit,
    header: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = AuraCanvas,
        topBar = header,
        bottomBar = { AuraBottomBar(role, currentRoute, onTabSelected) },
        floatingActionButton = floatingActionButton,
        content = content
    )
}

/**
 * Scaffold for detail, form and full-screen destinations: no bottom navigation, so a bottom
 * call to action owns the bottom edge alone.
 */
@Composable
fun AuraDetailScaffold(
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    bottomAction: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        containerColor = AuraCanvas,
        topBar = header,
        bottomBar = {
            if (bottomAction != null) {
                Surface(color = AuraWhite, shadowElevation = 12.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) { bottomAction() }
                }
            }
        },
        content = content
    )
}
