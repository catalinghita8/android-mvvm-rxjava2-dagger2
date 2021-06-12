package com.inspiringteam.xchange.util.providers

import android.content.Context
import androidx.annotation.StringRes
import com.google.common.base.Preconditions

/**
 * Concrete implementation of the [BaseResourceProvider] interface.
 */
class ResourceProvider(context: Context) : BaseResourceProvider {
    private val mContext: Context = Preconditions.checkNotNull(context, "context cannot be null")

    override fun getString(@StringRes id: Int): String {
        return mContext.getString(id)
    }

    override fun getString(@StringRes id: Int, vararg formatArgs: Any?): String {
        return mContext.getString(id, *formatArgs)
    }

}