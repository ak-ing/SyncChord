package com.aking.syncchord

import com.aking.data.Convex
import com.aking.data.datasource.AuthDataSource
import com.aking.data.datasource.WorkspaceDataSource
import com.aking.reactive.app
import com.aking.syncchord.auth.AuthRepository
import com.aking.syncchord.auth.AuthViewModel
import com.aking.syncchord.home.HomeViewModel
import com.aking.syncchord.home.domain.WorkspaceRepository
import com.aking.syncchord.home.ui.workspace.WorkspaceViewModel
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
    single { App.applicationScope }

    viewModelOf(::MainViewModel)

    viewModelOf(::AuthViewModel)
    viewModelOf(::HostViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::WorkspaceViewModel)

    singleOf(::AuthRepository)
    factoryOf(::WorkspaceRepository)

    factoryOf(::AuthDataSource)
    factoryOf(::WorkspaceDataSource)

}