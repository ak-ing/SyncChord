package com.aking.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Created by Ak on 2024-12-19 21:50.
 */
@Serializable
data class Workspace(
    @SerialName("_creationTime")
    val creationTime: Double,
    @SerialName("_id")
    val id: String,
    val joinCode: String,
    val name: String,
    val userId: String
)