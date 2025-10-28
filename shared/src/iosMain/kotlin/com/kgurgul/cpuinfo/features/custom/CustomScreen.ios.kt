package com.kgurgul.cpuinfo.features.custom

actual class CustomRamCollector : ICustomMemoryCollector {
    actual override fun collect(type: Int): String {
        return "CustomRamCollector AAA"
    }
}

actual class CustomRomCollector actual constructor() : ICustomMemoryCollector {
    actual override fun collect(type: Int): String {
        return "CustomRomCollector BBB"
    }
}
