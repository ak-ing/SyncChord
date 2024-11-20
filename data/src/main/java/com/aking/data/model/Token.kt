package com.aking.data.model

import kotlinx.serialization.Serializable

/**
 * Description:
 * tokens结果
 * Created by Rick at 2024-10-15 0:14.
 */
@Serializable
data class Auth0Token(
    val refreshToken: String,
    val token: String
)

