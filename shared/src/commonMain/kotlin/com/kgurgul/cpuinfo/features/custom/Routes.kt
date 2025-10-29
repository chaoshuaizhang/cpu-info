package com.kgurgul.cpuinfo.features.custom

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    object Main : Routes

    @Serializable
    object Job : Routes {
        @Serializable
        data object List : Routes

        @Serializable
        data class Detail(val name: String = "", val age: Int = 0) : Routes
    }
}
