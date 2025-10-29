package com.kgurgul.cpuinfo.features.custom

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry

interface INavPage {

    val route: String

    fun content(): @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit

    fun args(): List<NamedNavArgument> = emptyList()

    fun withArgs() = false

}
