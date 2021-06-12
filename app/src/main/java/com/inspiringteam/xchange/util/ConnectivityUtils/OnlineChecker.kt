package com.inspiringteam.xchange.util.ConnectivityUtils

/**
 * Simple interface that contains online/offline state indicator
 */
interface OnlineChecker {
    fun isOnline(): Boolean
}