package com.kgurgul.cpuinfo.features.custom.pages.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kgurgul.cpuinfo.features.custom.RouteManager
import com.kgurgul.cpuinfo.features.custom.pages.DetailPage
import com.kgurgul.cpuinfo.features.custom.pages.DetailPageArgs
import kotlin.random.Random

@Composable
fun HomePage() {
    Text(
        text = "Home Page ${Random.nextInt(1000)}",
        fontSize = 50.sp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth().padding(top = 100.dp).clickable {
            RouteManager.push(route = DetailPage.ROUTE, DetailPageArgs)
        }
    )
}
