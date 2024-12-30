package com.aking.syncchord.home.domain

import com.aking.base.base.BaseRepository
import com.aking.data.datasource.WorkspaceDataSource

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