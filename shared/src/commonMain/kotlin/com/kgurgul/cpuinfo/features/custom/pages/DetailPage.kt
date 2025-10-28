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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.serialization.decodeArguments
import androidx.savedstate.savedState
import co.touchlab.kermit.Logger
import com.kgurgul.cpuinfo.features.custom.INavPage
import kotlin.random.Random
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

private fun List<String>.toArgs(): String {
    val sb = StringBuilder("?")
    forEachIndexed { index, it ->
        sb.append(it)
        sb.append("={")
        sb.append(it)
        sb.append("}")
        if (index < size - 1) sb.append("&")
    }
    return sb.toString()
}

abstract class NavPageWithArgs : INavPage {
    override val route = route() + keys().toArgs()

    protected abstract fun route(): String

    protected abstract fun keys(): List<String>

    override fun args() = keys().map {
        navArgument(it) {
            nullable = true
            type = NavType.StringType
            defaultValue = ""
        }
    }
}

class DetailPage : NavPageWithArgs() {

    companion object {
        const val ROUTE = "Detail"
    }

    override fun route() = ROUTE

    override fun keys() = listOf("name", "age")

    @OptIn(InternalSerializationApi::class)
    override fun content(): @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit) {
        return {
            val savedState = it.arguments ?: savedState()
            val typeMap = it.destination.arguments.mapValues { it.value.type }
            val params = DetailPageArgs::class.serializer()
                .decodeArguments(savedState, typeMap) as DetailPageArgs
            Text(
                text = "Detail ${Random.nextInt(1000)}, ${params.name}|${params.age}",
                fontSize = 30.sp,
                color = Color.Blue,
                modifier = Modifier.fillMaxWidth().padding(top = 100.dp)
            )
        }
    }
}

@Serializable
data class DetailPageArgs(val name: String, val age: String)
