package com.kgurgul.cpuinfo.features.custom.pages.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kgurgul.cpuinfo.features.custom.RouteEntity
import com.kgurgul.cpuinfo.features.custom.RouteManager
import com.kgurgul.cpuinfo.features.custom.Routes
import com.kgurgul.cpuinfo.features.custom.nav.LocalNavHostController
import kotlin.random.Random
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OwnerPage(ownerViewModel: OwnerViewModel = koinViewModel(), jobListResult: String?) {
    BackHandler(true) {
        RouteManager.pop()
    }

    val random by ownerViewModel.random.collectAsStateWithLifecycle()
    Text(
        text = "Owner Page $random, $jobListResult",
        fontSize = 50.sp,
        color = Color.Yellow,
        modifier = Modifier.padding(top = 100.dp).fillMaxWidth().clickable {
            RouteManager.push(
                RouteEntity(route = Routes.Job.List)
            )
        }
    )
}
