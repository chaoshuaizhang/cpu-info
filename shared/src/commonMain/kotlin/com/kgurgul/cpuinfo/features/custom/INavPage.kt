package com.kgurgul.cpuinfo.features.custom

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry

interface INavPage {

    val route: String

    val params: List<NamedNavArgument>?

    fun content(): @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit

}
