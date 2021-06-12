package com.inspiringteam.xchange.di.scopes

import javax.inject.Scope

/**
 * Replacement scope for @Singleton to improve readability
 */
@MustBeDocumented
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AppScoped 