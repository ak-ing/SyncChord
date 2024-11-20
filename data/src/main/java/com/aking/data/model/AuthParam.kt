package com.aking.data.model

/**
 * Description:
 * 认证参数
 * Created by Rick at 2024-10-16 1:45.
 */
data class AuthParam(
    val params: Params?,
    val provider: String?,
    val providerAccountId: String?
) {
    /**
     * Converts this [AuthParam] to a map for easily sending as `args` to the Convex backend.
     */
    fun toArgs(): Map<String, Any?> = mutableMapOf(
        "params" to params?.toMap(),
        "provider" to (provider ?: ""),
        "providerAccountId" to (providerAccountId ?: "")
    )
}

data class Params(
    val email: String?,
    val image: String?,
    val name: String?
) {
    /**
     * Converts this [Params] to a map for easily embedding within the AuthParam map.
     */
    fun toMap(): Map<String, Any?> = mapOf(
        "email" to (email ?: ""),
        "image" to (image ?: ""),
        "name" to (name ?: "")
    )
}

/**
 * 登录参数
 */
data class SignParam(
    val type: String = "retrieveAccountWithCredentials",
    val token: String
) {
    fun toArgs(): Map<String, Any?> = mapOf(
        "type" to type,
        "token" to token
    )
}