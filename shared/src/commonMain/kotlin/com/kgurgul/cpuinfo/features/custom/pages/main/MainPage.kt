package com.kgurgul.cpuinfo.features.custom.pages.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.kgurgul.cpuinfo.features.custom.CustomNavigationBar
import com.kgurgul.cpuinfo.features.custom.INavPage
import com.kgurgul.cpuinfo.features.custom.Routes
import com.kgurgul.cpuinfo.features.custom.viewmodel.MainIntent
import com.kgurgul.cpuinfo.features.custom.viewmodel.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

class MainPage : INavPage {

    override val route = Routes.Main

    override fun invoke(navGraphBuilder: NavGraphBuilder, navHostController: NavHostController) {
        navGraphBuilder.composable<Routes.Main> {
            MainPageContent()
        }
    }
}

@Composable
private fun MainPageContent(mainViewModel: MainViewModel = koinViewModel()) {
    val pagerState = rememberPagerState { mainViewModel.mainPageTabs.size }
    val selectedTab by mainViewModel.selectedTab.collectAsStateWithLifecycle()
    LaunchedEffect(selectedTab) {
        pagerState.scrollToPage(selectedTab.tabIndex)
    }
    Column(
        modifier = Modifier.navigationBarsPadding().fillMaxSize(),
    ) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            HorizontalPager(
                pagerState,
                modifier = Modifier.fillMaxSize().background(Color.Black),
                userScrollEnabled = false,
                beyondViewportPageCount = 2,
            ) {
                when (mainViewModel.mainPageTabs[it].tabIndex) {
                    0 -> HomePage()
                    1 -> OwnerPage()
                }
            }
        }
        CustomNavigationBar(mainViewModel.mainPageTabs) {
            mainViewModel.processIntents(MainIntent.SwitchTab(it))
        }
    }
}
