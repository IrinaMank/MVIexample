package com.mankovskaya.mviexample.core.android

import android.content.Context
import androidx.annotation.StringRes

class ResourceManager(private val context: Context) {
    private val resources = context.resources

    fun getString(@StringRes stringId: Int, vararg args: Any? = arrayOf()): String =
        when (args.isEmpty()) {
            true -> resources.getString(stringId)
            else -> resources.getString(stringId, *args)
        }

}
