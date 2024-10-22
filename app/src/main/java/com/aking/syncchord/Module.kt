package com.aking.syncchord

import com.aking.base.app
import com.aking.data.Convex
import com.aking.data.datasource.AuthDataSource
import com.aking.syncchord.auth.AuthRepository
import com.aking.syncchord.auth.AuthUiState
import com.aking.syncchord.auth.AuthViewModel
import com.aking.syncchord.host.HostState
import com.aking.syncchord.host.HostViewModel
import com.aking.syncchord.util.dataStore
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.lazyModule

/**
 * koin module
 * @author Ak
 * 2024/9/29 15:43
 */
val appModule = lazyModule {
    single { Convex.client }
    single { app.dataStore }
    singleOf(::AuthRepository)

    viewModelOf(::AuthViewModel)
    viewModelOf(::HostViewModel)
    factoryOf(::AuthDataSource)
    factory { AuthUiState() }
    factory { HostState() }
}