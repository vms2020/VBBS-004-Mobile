package io.bbs.seva.vbbs004mobile.presentation.root

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import io.bbs.seva.vbbs004mobile.presentation.home.HomeScreen
import io.bbs.seva.vbbs004mobile.presentation.home.HomeViewModel
import io.bbs.seva.vbbs004mobile.presentation.login.LoginScreen
import io.bbs.seva.vbbs004mobile.presentation.login.LoginViewModel
import io.bbs.seva.vbbs004mobile.presentation.menu.MenuItem
import io.bbs.seva.vbbs004mobile.presentation.navigation.Destination
import io.bbs.seva.vbbs004mobile.presentation.navigation.getDestinationIcon
import io.bbs.seva.vbbs004mobile.presentation.weather.WeatherScreen
import io.bbs.seva.vbbs004mobile.session.SessionManager
import kotlinx.coroutines.launch


@Composable
fun getMenuItems(isAuthenticated: Boolean): List<MenuItem> {

//    Log.i(
//        "GETMENUITEMS",
//        "!!!!!!!!!!!!!!\n" +
//                "getMenuItems: ${Destination.all}\n" +
//                "!!!!!!!!!!!!!!!!"
//    )
    return Destination.all
        .filter { dest ->
            when {
                dest == Destination.Logout -> isAuthenticated   // show logout only if logged in
                dest.requiresAuth -> isAuthenticated             // protected screens only if logged in
                else -> !isAuthenticated || dest == Destination.CurrencyRates
                // public screens (Login, Signup) only if not logged in,
                // but CurrencyRates is always visible
            }
        }
        .map { dest -> MenuItem(dest, getDestinationIcon(dest)) }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot(
    authRepository: AuthRepository,
    sessionManager: SessionManager
) {
    val activity = LocalActivity.current
    val isAuthenticatedState = authRepository.isAuthenticated.collectAsState(initial = null)
    val isAuthenticated = isAuthenticatedState.value
    if (isAuthenticated == null) {
        // Splash screen
        return
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val backstack = rememberNavBackStack(
        if (isAuthenticated) Destination.Home else Destination.Login
    )
    val entryProvider = remember {
        /* your entryProvider as before */
        entryProvider<NavKey> {
            entry<Destination.Login> {
                val viewModel: LoginViewModel = hiltViewModel()
                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToHome = { backstack.add(Destination.Home) }
                )
            }
            entry<Destination.Home> {
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
//                    onBack = {
//                        if (backstack.size > 1) backstack.removeAt(backstack.lastIndex)
//                    },
//                    onNavigateToWeather = { backstack.add(Destination.Weather) },
                    viewModel = viewModel,
                )
            }
            entry<Destination.Weather> {
                WeatherScreen(
                    onBack = {
                        if (backstack.size > 1) backstack.removeAt(backstack.lastIndex)
                    },
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        sessionManager.logoutEvents.collect {
            Log.i("APPROOT", "!!!!!!!!!!!!!!!!!!!!!!!!\nAppRoot: backstack.add(Destination.Login)\n!!!!!!!!!!!!!!")
            backstack.removeAll { true }
            backstack.add(Destination.Login)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                // Forces the drawer sheet to a dedicated width, leaving explicit screen space on the right side
                modifier = Modifier
                    .requiredWidth(300.dp)
//                    .fillMaxHeight()
//                    .padding(top = 32.dp)

            ) {

                // Add a structured header row containing a close action icon
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {

                    // Visible close action icon inside the open drawer sheet
                    IconButton(
                        onClick = {
                            scope.launch { drawerState.close() }
                        },

                        ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Close Menu"
                        )
                    }
                    Text(
                        "Menu",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                    )

                }


                //Text("Menu", style = MaterialTheme.typography.headlineSmall)
                HorizontalDivider()
                val menuItems = getMenuItems(isAuthenticated)
                menuItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.destination.title) },
                        icon = { Icon(item.icon, contentDescription = null) },
                        selected = backstack.lastOrNull() == item.destination,
                        onClick = {
                            scope.launch { drawerState.close() }
                            // Navigate
                            if (item.destination == Destination.Logout) {
                                // Perform logout (call authRepository.logout(), etc.)
                                // You can also emit a logout event
                                scope.launch {
                                    authRepository.logout()
                                    // The isAuthenticated state will become false,
                                    // and the LaunchedEffect will reset backstack
                                }
                            } else {
                                // For simplicity, add to backstack
                                backstack.add(item.destination)
                            }
                        }
                    )
                }
            }
        }
    ) {
        // Main content with Scaffold and BottomBar
        Scaffold(
            bottomBar = {
                // Optional: show bottom bar only for authenticated users
                if (isAuthenticated) {
                    NavigationBar {
                        listOf(Destination.Home, Destination.Weather, Destination.CurrencyRates)
                            .forEach { dest ->
                                NavigationBarItem(
                                    selected = backstack.lastOrNull() == dest,
                                    onClick = { backstack.add(dest) },
                                    icon = { Icon(getDestinationIcon(dest), null) },
                                    label = { Text(dest.title) }
                                )
                            }
                    }
                }
            },
            topBar = {
                TopAppBar(
                    title = { Text("V BBS 004") },
                    navigationIcon = {
                        IconButton({
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Menu, "Menu")
                        }
                    }
                )
            }

        ) { innerPadding ->
            NavDisplay(
                modifier = Modifier.padding(innerPadding),
                backStack = backstack,
                onBack = {
                    if (backstack.size > 1) backstack.removeAt(backstack.lastIndex)
                    else activity?.finish()
                },
                entryProvider = entryProvider
            )
        }
    }
}
