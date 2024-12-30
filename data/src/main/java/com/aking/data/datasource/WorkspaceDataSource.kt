package com.aking.data.datasource

import com.aking.data.model.Workspace
import com.auth0.android.result.Credentials
import dev.convex.android.ConvexClientWithAuth

/**
 * Description: 群组
 * Created by Rick at 2024-11-21 19:55.
 */
class WorkspaceDataSource(private val convex: ConvexClientWithAuth<Credentials>) {

    suspend fun getWorkspaces() = convex.subscribe<MutableList<Workspace>>("workspaces_auth0:get")

    suspend fun createWorkspace(name: String) = runCatching {
        convex.mutation<String>("workspaces_auth0:create", mapOf("name" to name))
    }
}