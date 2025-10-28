package com.kgurgul.cpuinfo.features.custom.pages

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.navArgument
import com.kgurgul.cpuinfo.features.custom.INavPage
import kotlin.random.Random

class DetailPage : INavPage {

    companion object {
        const val ROUTE = "Detail"
    }

    override val route: String
        get() = ROUTE

    override val params: List<NamedNavArgument> = listOf(
        navArgument("key1") {},
        navArgument("key2") {}
    )

    override fun content(): @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit) {
        return {
            Text(
                text = "Detail Page ${Random.nextInt(1000)}",
                fontSize = 50.sp,
                color = Color.Blue,
                modifier = Modifier.fillMaxWidth().padding(top = 100.dp)
            )
        }
    }
}
