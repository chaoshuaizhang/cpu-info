package com.kgurgul.cpuinfo.features

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import com.kgurgul.cpuinfo.features.applications.ApplicationsBaseRoute
import com.kgurgul.cpuinfo.features.applications.applicationsScreen
import com.kgurgul.cpuinfo.features.custom.nav.CustomNavHost
import com.kgurgul.cpuinfo.features.information.InformationBaseRoute
import com.kgurgul.cpuinfo.features.information.informationScreen
import com.kgurgul.cpuinfo.features.processes.ProcessesBaseRoute
import com.kgurgul.cpuinfo.features.processes.processesScreen
import com.kgurgul.cpuinfo.features.settings.SettingsRoute
import com.kgurgul.cpuinfo.features.settings.settingsScreen
import com.kgurgul.cpuinfo.features.temperature.TemperaturesBaseRoute
import com.kgurgul.cpuinfo.features.temperature.temperaturesScreen
import com.kgurgul.cpuinfo.shared.Res
import com.kgurgul.cpuinfo.shared.applications
import com.kgurgul.cpuinfo.shared.hardware
import com.kgurgul.cpuinfo.shared.ic_android
import com.kgurgul.cpuinfo.shared.ic_cpu
import com.kgurgul.cpuinfo.shared.ic_process
import com.kgurgul.cpuinfo.shared.ic_settings
import com.kgurgul.cpuinfo.shared.ic_temperature
import com.kgurgul.cpuinfo.shared.processes
import com.kgurgul.cpuinfo.shared.settings
import com.kgurgul.cpuinfo.shared.temp
import com.kgurgul.cpuinfo.ui.components.CpuNavigationSuiteScaffold
import com.kgurgul.cpuinfo.ui.components.CpuNavigationSuiteScaffoldDefault
import com.kgurgul.cpuinfo.ui.components.NavigationSuiteItemColors
import com.kgurgul.cpuinfo.ui.theme.CpuInfoTheme
import com.kgurgul.cpuinfo.utils.navigation.TopLevelRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HostScreen(
    viewModel: HostViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    Logger.i { "NCSNJCNSJCNJS 1 ${navController.hashCode()}" }
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    HostScreen(
        uiState = uiState,
        navController = navController,
    )
}
var job: Job? = null
@Composable
fun HostScreen(
    uiState: HostViewModel.UiState,
    navController: NavHostController = rememberNavController(),
) {
//    RouteManager.register(MainPage())
//    RouteManager.register(DetailPage())
    val useCustom = true
    if (useCustom.not()) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val itemDefaultColors = CpuNavigationSuiteScaffoldDefault.itemDefaultColors()
        OriginProject(uiState, currentDestination, itemDefaultColors, navController)
    } else CustomNavHost(navController)
}



@Composable
fun OriginProject(
    uiState: HostViewModel.UiState,
    currentDestination: NavDestination?,
    itemDefaultColors: NavigationSuiteItemColors,
    navController: NavHostController
) {
    CpuNavigationSuiteScaffold(
        navigationSuiteItems = {
            buildTopLevelRoutes(
                isProcessesVisible = uiState.isProcessSectionVisible,
                isApplicationsVisible = uiState.isApplicationSectionVisible,
            ).forEach { topLevelRoute ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(topLevelRoute.icon),
                            contentDescription = stringResource(topLevelRoute.name),
                        )
                    },
                    label = {
                        Text(
                            text = "AAA",
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    selected = currentDestination?.hierarchy?.any {
                        it.hasRoute(topLevelRoute.route::class)
                    } == true,
                    onClick = {
                        navController.navigate(topLevelRoute.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = itemDefaultColors,
                )
            }
        },
    ) {
        NavHost(
            navController = navController,
            startDestination = InformationBaseRoute,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
            popExitTransition = { fadeOut() },
        ) {
            informationScreen(navController) {
                navController.navigate(SettingsRoute.Licenses)
            }
            applicationsScreen()
            processesScreen()
            temperaturesScreen()
            settingsScreen(
                onLicensesClicked = {
                    navController.navigate(SettingsRoute.Licenses)
                },
                onNavigateBackClicked = {
                    navController.popBackStack()
                },
            )
        }
    }
}

private fun buildTopLevelRoutes(
    isProcessesVisible: Boolean,
    isApplicationsVisible: Boolean,
) = buildList {
    add(
        TopLevelRoute(
            name = Res.string.hardware,
            route = InformationBaseRoute,
            icon = Res.drawable.ic_cpu,
        ),
    )
    if (isApplicationsVisible) {
        add(
            TopLevelRoute(
                name = Res.string.applications,
                route = ApplicationsBaseRoute,
                icon = Res.drawable.ic_android,
            ),
        )
    }
    if (isProcessesVisible) {
        add(
            TopLevelRoute(
                name = Res.string.processes,
                route = ProcessesBaseRoute,
                icon = Res.drawable.ic_process,
            ),
        )
    }
    add(
        TopLevelRoute(
            name = Res.string.temp,
            route = TemperaturesBaseRoute,
            icon = Res.drawable.ic_temperature,
        ),
    )
    add(
        TopLevelRoute(
            name = Res.string.settings,
            route = SettingsRoute,
            icon = Res.drawable.ic_settings,
        ),
    )
}

@Preview
@Composable
fun HostScreenPreview() {
    CpuInfoTheme {
        HostScreen(
            uiState = HostViewModel.UiState(),
        )
    }
}

