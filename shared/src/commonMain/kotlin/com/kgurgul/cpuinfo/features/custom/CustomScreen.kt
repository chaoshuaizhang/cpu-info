package com.kgurgul.cpuinfo.features.custom

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import com.kgurgul.cpuinfo.features.custom.pages.main.MainPage
import com.kgurgul.cpuinfo.features.custom.viewmodel.MainViewModel
import com.kgurgul.cpuinfo.utils.navigation.NavigationConst
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

@Serializable
data object CustomBaseRoute {
    @SerialName(NavigationConst.CUSTOM)
    @Serializable
    data object CustomRoute
}

class PageB {
    fun content(): @Composable () -> Unit {
        Logger.i { "NCJSNCJSNJNC -> content()" }
        return {
            Logger.i { "NCJSNCJSNJNC -> Text()" }
            Text(
                "Page B ${Random.nextInt(1000)}",
                fontSize = 50.sp,
                color = Color.White,
                modifier = Modifier.fillMaxSize().padding(top = 100.dp)
            )
        }
    }
}

fun NavGraphBuilder.navCustomScreen() {
    navigation<CustomBaseRoute>(
        startDestination = CustomBaseRoute.CustomRoute
    ) {
        composable<CustomBaseRoute.CustomRoute>(content = MainPage().content())
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
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        val navHostController = rememberNavController()
        val state = rememberPagerState { 2 }
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            HorizontalPager(
                state,
                modifier = Modifier.fillMaxSize().background(Color.Black),
                userScrollEnabled = false,
                beyondViewportPageCount = 2,
            ) {
                Logger.i { "HorizontalPagerAAA -> $it" }
                when (it) {
                    0 -> {
                        Text(
                            "0 -> Page A ${
                                Random.nextInt(1000).also {
                                    Logger.i { "CSBCHBSHCB   0 -> v = $it" }
                                }
                            }",
                            fontSize = 50.sp,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth().padding(top = 100.dp)
                        )
                    }

                    1 -> {
                        PageB().content().invoke()
                    }
                }
            }
        }
    }
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
    factoryOf(::MainViewModel)
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


@Composable
fun CustomNavigationBar(tabs: List<TabItem>, onTabChanged: (Int) -> Unit) {
    var selectedItem by remember { mutableIntStateOf(0) }
    NavigationBar(
        modifier = Modifier.height(68.dp).background(Color.White)
    ) {
        tabs.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { item.selectIcon },
                label = { Text(item.title) },
                selected = selectedItem == index,
                onClick = {
                    onTabChanged.invoke(index)
//                    selectedItem = index
//                    val topLevelNavOptions = navOptions {
//                        // Pop up to the start destination of the graph to
//                        // avoid building up a large stack of destinations
//                        // on the back stack as users select items
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                            inclusive = true
//                        }
//                        // Avoid multiple copies of the same destination when
//                        // reselecting the same item
//                        launchSingleTop = true
//                        // Restore state when reselecting a previously selected item
//                        restoreState = true
//                    }
//                    navController.navigate(item, topLevelNavOptions)
                }
            )
        }
    }
}


data class TabItem(
    val tabIndex: Int,
    val title: String,
    val selectIcon: ImageVector,
    val unselectIcon: ImageVector? = null
)
