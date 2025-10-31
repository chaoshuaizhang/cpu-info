package com.kgurgul.cpuinfo.features.custom.nav

import android.content.Context
import android.content.Intent
import org.koin.core.context.GlobalContext


actual fun moveAppToBackground() {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    GlobalContext.get().get<Context>().startActivity(intent)
}
