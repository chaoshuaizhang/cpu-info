package com.kgurgul.cpuinfo.features.custom.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.kgurgul.cpuinfo.features.custom.INavPage
import com.kgurgul.cpuinfo.features.custom.RouteEntity
import com.kgurgul.cpuinfo.features.custom.RouteManager
import com.kgurgul.cpuinfo.features.custom.Routes
import kotlinx.serialization.Serializable

class JobGroup : INavPage {

    override val route: Routes = Routes.Job

    override fun invoke(navGraphBuilder: NavGraphBuilder, navHostController: NavHostController) {
        navGraphBuilder.navigation<Routes.Job>(Routes.Job.Detail()) {
            navJobList()
            navJobDetail()
        }
    }
}

fun NavGraphBuilder.navJobList() {
    composable<Routes.Job.List> {
        Text(
            text = "Job List",
            fontSize = 30.sp,
            color = Color.Blue,
            modifier = Modifier.fillMaxWidth().padding(top = 100.dp)
        )
    }
}

fun NavGraphBuilder.navJobDetail() {
    composable<Routes.Job.Detail> {
        val detail = it.toRoute<Routes.Job.Detail>()
        Text(
            text = "Job Detail - ${detail.name} | ${detail.age}",
            fontSize = 30.sp,
            color = Color.Blue,
            modifier = Modifier.fillMaxWidth().padding(top = 100.dp).clickable {
                RouteManager.pop()
            }
        )
    }
}
