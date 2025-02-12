package com.aking.syncchord.home.data.repository

import com.aking.data.datasource.WorkspaceDataSource
import com.aking.reactive.base.BaseRepository

/**
 * Description:
 * Created by Rick at 2024-11-21 19:51.
 */
class WorkspaceRepository(private val dataSource: WorkspaceDataSource) : BaseRepository() {

    suspend fun getWorkspaces() = request {
        dataSource.getWorkspaces()
    }

    suspend fun createWorkspace(name: String) = request {
        dataSource.createWorkspace(name)
    }
}