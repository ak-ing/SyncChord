package com.aking.syncchord

import com.aking.data.Convex
import com.aking.data.ConvexDataSource
import com.aking.syncchord.profile.ui.data.repository.AuthRepository
import com.aking.syncchord.profile.ui.login.LoginViewModel
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

    factoryOf(::ConvexDataSource)

    factoryOf(::AuthRepository)

    viewModelOf(::LoginViewModel)
}