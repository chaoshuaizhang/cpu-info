package com.kgurgul.cpuinfo.features.custom.pages.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import com.kgurgul.cpuinfo.features.custom.INavPage
import com.kgurgul.cpuinfo.features.custom.RouteEntity
import com.kgurgul.cpuinfo.features.custom.RouteManager
import com.kgurgul.cpuinfo.features.custom.Routes
import com.kgurgul.cpuinfo.features.custom.pages.navJobDetail
import com.kgurgul.cpuinfo.features.custom.pages.navJobList
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomePage(homeViewModel: HomeViewModel = koinViewModel()) {
    val random by homeViewModel.random.collectAsStateWithLifecycle()
    BackHandler(true) {
        RouteManager.pop()
    }
    Text(
        text = "Home Page $random",
        fontSize = 50.sp,
        color = Color.Yellow,
        modifier = Modifier.padding(top = 100.dp).fillMaxSize().clickable {
            RouteManager.push(
                RouteEntity(
                    route = Routes.Job.Detail("zcs", 3)
                )
            )
        }
    )
}

class HomeViewModel : ViewModel() {
    var random = MutableStateFlow("${Random.nextInt(1000)}")
}

class OwnerViewModel : ViewModel() {
    var random = MutableStateFlow("${Random.nextInt(1000)}")
}

class HomePage2 : INavPage {

    override val route: Routes = Routes.Home.Home
    override fun invoke(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.navHome()
        navGraphBuilder.navOwner()
    }
}

class OwnerPage2 : INavPage {

    override val route: Routes = Routes.Owner.Owner
    override fun invoke(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.navigation<Routes.Owner>(Routes.Owner.Owner) {
            navOwner()
        }
    }
}

fun NavGraphBuilder.navHome() {
    composable<Routes.Home.Home> {
        HomePage()
    }
}

fun NavGraphBuilder.navOwner() {
    composable<Routes.Owner.Owner> {
        val jobListResult by it.savedStateHandle.getStateFlow<String?>("job.list.result", null)
            .collectAsStateWithLifecycle()
        LaunchedEffect(jobListResult) {
            Logger.i {
                "jobListResult: $jobListResult"
            }
        }
        OwnerPage(jobListResult = jobListResult)
    }
}
