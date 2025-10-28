package com.kgurgul.cpuinfo.features.custom.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.lifecycle.ViewModel
import com.kgurgul.cpuinfo.features.custom.TabItem
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {

    val mainPageTabs = listOf(
        TabItem(0, "Home", Icons.Filled.Favorite),
        TabItem(1, "Owner", Icons.Filled.Settings)
    )

    val selectedTab = MutableStateFlow(mainPageTabs[0])

    fun processIntents(intent: MainIntent) {
        when (intent) {
            is MainIntent.SwitchTab -> selectedTab.value = mainPageTabs[intent.tabIndex]
        }
    }

}

sealed class MainIntent {
    data class SwitchTab(val tabIndex: Int) : MainIntent()
}
