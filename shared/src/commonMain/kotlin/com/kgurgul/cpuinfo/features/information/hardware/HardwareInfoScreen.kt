package com.kgurgul.cpuinfo.features.information.hardware

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.kgurgul.cpuinfo.features.information.base.InformationRow
import com.kgurgul.cpuinfo.ui.theme.spacingSmall
import com.kgurgul.cpuinfo.utils.collectAsStateMultiplatform
import org.koin.androidx.compose.koinViewModel

@Composable
fun HardwareInfoScreen(
    viewModel: HardwareInfoViewModel = koinViewModel(),
) {
    registerPowerPlugListener(
        onRefresh = viewModel::refreshHardwareInfo
    )
    val uiState by viewModel.uiStateFlow.collectAsStateMultiplatform()
    HardwareInfoScreen(
        uiState = uiState,
    )
}

@Composable
fun HardwareInfoScreen(
    uiState: HardwareInfoViewModel.UiState,
) {
    LazyColumn(
        contentPadding = PaddingValues(spacingSmall),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier.fillMaxSize(),
    ) {
        itemsIndexed(
            uiState.hardwareItems
        ) { index, (title, value) ->
            InformationRow(
                title = title,
                value = value,
                isLastItem = index == uiState.hardwareItems.lastIndex,
                modifier = Modifier.focusable(),
            )
        }
    }
}

@Composable
expect fun registerPowerPlugListener(onRefresh: () -> Unit)
