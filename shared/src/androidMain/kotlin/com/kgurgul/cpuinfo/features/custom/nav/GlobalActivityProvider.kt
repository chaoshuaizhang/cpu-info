package com.kgurgul.cpuinfo.features.custom.nav

import android.app.Activity
import java.lang.ref.WeakReference

/**
 * A lifecycle-aware provider that holds a WeakReference to the current Activity.
 * This prevents memory leaks by allowing the Activity to be garbage collected
 * even if the onDestroy() cleanup call is missed.
 */
class GlobalActivityProvider {

    private var activityReference: WeakReference<Activity>? = null

    /**
     * Provides the current Activity instance if it's still available.
     * Returns null if the Activity has been garbage collected.
     */
    val activity: Activity?
        get() = activityReference?.get()

    /**
     * Call this in Activity.onCreate() to set the current Activity.
     */
    fun onActivityCreated(activity: Activity) {
        activityReference = WeakReference(activity)
    }

    /**
     * Call this in Activity.onDestroy() to clear the reference.
     * While WeakReference helps, explicit cleanup is still good practice.
     */
    fun onActivityDestroyed() {
        activityReference?.clear()
    }
}
