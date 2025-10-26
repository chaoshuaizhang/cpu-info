package com.kgurgul.cpuinfo.features.custom

actual class CustomRamCollector : ICustomMemoryCollector {
    actual override fun collect(type: Int): String {
        return when (type) {
            1 -> "RAM - 200MB"
            else -> throw Exception("Error type = $type")
        }
    }
}

actual class CustomRomCollector : ICustomMemoryCollector {
    actual override fun collect(type: Int): String {
        return when (type) {
            2 -> "ROM - 200GB"
            else -> throw Exception("Error type = $type")
        }
    }
}
