package com.aking.syncchord

import com.aking.base.app
import com.aking.data.Convex
import com.aking.data.datasource.AuthDataSource
import com.aking.data.datasource.WorkspaceDataSource
import com.aking.syncchord.auth.AuthRepository
import com.aking.syncchord.auth.AuthUiState
import com.aking.syncchord.auth.AuthViewModel
import com.aking.syncchord.home.ui.HomeState
import com.aking.syncchord.home.ui.HomeViewModel
import com.aking.syncchord.home.ui.WorkspaceRepository
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

    viewModelOf(::AuthViewModel)
    viewModelOf(::HostViewModel)
    viewModelOf(::HomeViewModel)

    singleOf(::AuthRepository)
    factoryOf(::WorkspaceRepository)

    factoryOf(::AuthDataSource)
    factoryOf(::WorkspaceDataSource)

    factory { AuthUiState() }
    factory { HostState() }
    factory { HomeState() }
}