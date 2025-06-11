package id.ac.unpas.blogging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import id.ac.unpas.blogging.ui.components.AppDrawerContent
import id.ac.unpas.blogging.ui.components.DrawerMenuItem
import id.ac.unpas.blogging.ui.navigation.AppNavHost
import id.ac.unpas.blogging.ui.navigation.AppRoutes
import id.ac.unpas.blogging.ui.theme.BloggingTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BloggingTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val menuItems = listOf(
                    DrawerMenuItem(AppRoutes.HOME, "Beranda", Icons.Filled.Home),
                    DrawerMenuItem(AppRoutes.CREATE_POST, "Buat Postingan", Icons.Filled.AddCircle),
                    DrawerMenuItem(null, "Logout", Icons.AutoMirrored.Filled.ExitToApp, action = {
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    })
                )

                val showAppBarsAndDrawer = currentRoute !in listOf(AppRoutes.LOGIN, AppRoutes.REGISTER)

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = showAppBarsAndDrawer,
                    drawerContent = {
                        if (showAppBarsAndDrawer) {
                            AppDrawerContent(
                                currentRoute = currentRoute,
                                menuItems = menuItems,
                                onMenuItemClick = { menuItem ->
                                    if (menuItem.action != null) {
                                        menuItem.action.invoke()
                                    } else if (menuItem.route != null) {
                                        navController.navigate(menuItem.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }

                                            launchSingleTop = true

                                            restoreState = true
                                        }
                                    }
                                    scope.launch { drawerState.close() }
                                },
                                onCloseDrawer = { scope.launch { drawerState.close() } }
                            )
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            if (showAppBarsAndDrawer) {
                                TopAppBar(
                                    title = {
                                        val title = when (currentRoute) {
                                            AppRoutes.HOME -> "Beranda"
                                            AppRoutes.CREATE_POST -> "Buat Postingan Baru"
                                            AppRoutes.EDIT_POST -> "Edit Postingan"
                                            else -> navBackStackEntry?.destination?.route?.replaceFirstChar { it.uppercase() } ?: "Blog App"
                                        }
                                        Text(title)
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                            }
                                        }) {
                                            Icon(Icons.Filled.Menu, contentDescription = "Buka menu navigasi")
                                        }
                                    }
                                )
                            }
                        },
                        floatingActionButton = {
                            if (showAppBarsAndDrawer && currentRoute == AppRoutes.HOME) {
                                FloatingActionButton(onClick = {
                                    navController.navigate(AppRoutes.CREATE_POST)
                                }) {
                                    Icon(Icons.Filled.AddCircle, contentDescription = "Buat Postingan Baru")
                                }
                            }
                        }
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier.fillMaxSize().padding(innerPadding),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AppNavHost(navController = navController)
                        }
                    }
                }
            }
        }
    }
}