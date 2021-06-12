package com.inspiringteam.xchange.util.chromeTabsUtils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.core.content.ContextCompat
import com.inspiringteam.xchange.R

// TODO Deprecate this wrapper
class ChromeTabsWrapper constructor(private val mContext: Context) : ServiceConnectionCallback {
    private var mConnection: CustomTabsServiceConnection? = null
    private var mClient: CustomTabsClient? = null

    fun openCustomtab(url: String?) {
        val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
        builder.setExitAnimations(mContext, R.anim.fade_in, R.anim.fade_out)
        builder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
        val customTabsIntent: CustomTabsIntent = builder.build()
        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.launchUrl(mContext, Uri.parse(url))
    }

    fun bindCustomTabsService() {
        if (mClient != null) return
        if (mConnection == null) {
            mConnection = ServiceConnection(this)
        }
        CustomTabsClient.bindCustomTabsService(mContext, CUSTOM_TAB_PACKAGE_NAME, mConnection)
    }

    fun unbindCustomTabsService() {
        if (mConnection == null) return
        mContext.unbindService(mConnection!!)
        mClient = null
        mConnection = null
    }

    public override fun onServiceConnected(client: CustomTabsClient) {
        mClient = client
    }

    public override fun onServiceDisconnected() {
        mClient = null
    }

    companion object {
        private val CUSTOM_TAB_PACKAGE_NAME: String = "com.android.chrome"
    }
}