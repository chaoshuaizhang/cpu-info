package com.kgurgul.cpuinfo.features.custom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface INavPage {

    val route: Routes

    operator fun invoke(navGraphBuilder: NavGraphBuilder, navHostController: NavHostController)

}
