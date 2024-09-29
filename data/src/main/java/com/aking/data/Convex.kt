package com.aking.data

import android.app.Application
import com.auth0.android.result.Credentials
import dev.convex.android.ConvexClientWithAuth
import dev.convex.android.auth0.Auth0Provider

/**
 * Convex客户端，配置文件: workout.properties
 * @author Ak
 * 2024/9/29 14:03
 */
object Convex {

    internal lateinit var app: Application
    private lateinit var _client: ConvexClientWithAuth<Credentials>
    val client = _client

    fun init(application: Application) {
        if (this::app.isInitialized) return
        app = application

        val auth0 = Auth0Provider(
            app,
            app.getString(R.string.com_auth0_client_id),
            app.getString(R.string.com_auth0_domain),
            app.getString(R.string.com_auth0_scheme),
            enableCachedLogins = true
        )
        _client = ConvexClientWithAuth(app.getString(R.string.convex_url), auth0)
    }

}