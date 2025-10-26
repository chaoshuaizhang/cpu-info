package com.kgurgul.cpuinfo.features.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kgurgul.cpuinfo.utils.navigation.NavigationConst
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.Qualifier
import org.koin.core.annotation.Single
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

@Serializable
data object CustomBaseRoute {
    @SerialName(NavigationConst.CUSTOM)
    @Serializable
    data object CustomRoute
}

fun NavGraphBuilder.customScreen() {
    navigation<CustomBaseRoute>(
        startDestination = CustomBaseRoute.CustomRoute
    ) {
        composable<CustomBaseRoute.CustomRoute> {
            CustomScreen()
        }
    }
}

@Composable
fun CustomScreen(viewModel: CustomViewModel = koinViewModel()) {
    var switchState by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        launch {
            while (true) {
                delay(2_000)
                switchState = switchState.not()
            }
        }
    }
    Text(
        text = if (switchState) viewModel.ramInfo() else viewModel.romInfo(),
        modifier = Modifier.fillMaxSize()
            .background(Color.Yellow),
        fontSize = 100.sp
    )
}

class CustomViewModel(
    private val ramUseCase: CustomRamUseCase,
    private val romUseCase: CustomRomUseCase
) : ViewModel() {

    fun ramInfo() = ramUseCase.invoke()

    fun romInfo() = romUseCase.invoke()

}

class CustomRamUseCase(private val memoryCollector: ICustomMemoryCollector) {
    operator fun invoke(): String {
        return memoryCollector.collect(1)
    }
}

class CustomRomUseCase(private val memoryCollector: ICustomMemoryCollector) {
    operator fun invoke(): String {
        return memoryCollector.collect(2)
    }
}

interface ICustomMemoryCollector {
    fun collect(type: Int): String
}

expect class CustomRamCollector() : ICustomMemoryCollector {
    override fun collect(type: Int): String
}

expect class CustomRomCollector() : ICustomMemoryCollector {
    override fun collect(type: Int): String
}

val viewModelModules = module {
    factoryOf(::CustomViewModel)
}

val useCaseModules = module {
    factory {
        // TODO: 绑定 CustomRamUseCase，并使用限定符注入 CustomRamCollector
        CustomRamUseCase(memoryCollector = get(RAM_COLLECTOR_QUALIFIER))
    }
    factory {
        CustomRomUseCase(memoryCollector = get(ROM_COLLECTOR_QUALIFIER))
    }
}

val RAM_COLLECTOR_QUALIFIER = named("CustomRamCollector")
val ROM_COLLECTOR_QUALIFIER = named("CustomRomCollector")

val collectorModules = module {
    // TODO: 必须显式限定范型类型为 ICustomMemoryCollector
    factory<ICustomMemoryCollector>(RAM_COLLECTOR_QUALIFIER) {
        CustomRamCollector()
    }

    factory<ICustomMemoryCollector>(ROM_COLLECTOR_QUALIFIER) {
        CustomRomCollector()
    }
}

// FIXME: 上面两个TODO，为什么要这么复杂的写，加上Qualifier？因为ICustomMemoryCollector有
//  两个子类，注入时 koin 分不清究竟注入到哪个类：CustomRamUseCase和CustomRomUseCase的参数都是
//  ICustomMemoryCollector类型
