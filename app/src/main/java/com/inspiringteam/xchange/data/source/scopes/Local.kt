package com.inspiringteam.xchange.data.source.scopes

import javax.inject.Qualifier

/**
 * This scope has been created for Dagger to differentiate between types of Data Sources
 */
@Qualifier
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Local 