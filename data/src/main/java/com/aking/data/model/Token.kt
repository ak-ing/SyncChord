package com.aking.data.model

import kotlinx.serialization.Serializable

/**
 * Description:
 * Auth0认证返回的refreshToken
 * Created by Rick at 2024-10-15 0:14.
 */
@Serializable
data class Auth0Token(
    val refreshToken: String
)

/**
 * tokens结果
 */
@Serializable
data class Tokens(
    val refreshToken: String,
    val token: String
)