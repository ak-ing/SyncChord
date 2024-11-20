package com.aking.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Description: 用户信息
 * Created by Rick at 2024-11-18 19:50.
 */
@Serializable
data class UserModel(
    @SerialName("_creationTime")
    val creationTime: Double,
    @SerialName("_id")
    val id: String,
    val email: String,
    val image: String,
    val name: String
)