package com.aking.syncchord

import com.aking.data.Convex
import com.aking.data.datasource.AuthDataSource
import com.aking.syncchord.auth.AuthRepository
import com.aking.syncchord.auth.AuthViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.lazyModule

/**
 * koin module
 * @author Ak
 * 2024/9/29 15:43
 */
val appModule = lazyModule {
    single { Convex.client }

    factoryOf(::AuthDataSource)

    factoryOf(::AuthRepository)

    viewModelOf(::AuthViewModel)
}