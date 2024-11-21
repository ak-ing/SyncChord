package com.aking.data.datasource

import com.auth0.android.result.Credentials
import dev.convex.android.ConvexClientWithAuth

/**
 * Description:
 * Created by Rick at 2024-11-21 19:55.
 */
class WorkspaceDataSource(private val convex: ConvexClientWithAuth<Credentials>) {

    suspend fun getWorkspaces() = convex.subscribe<List<String>>("workspaces:get")

    suspend fun createWorkspace(name: String) = runCatching {
        convex.mutation<String>("workspaces:create", mapOf("name" to name))
    }
}