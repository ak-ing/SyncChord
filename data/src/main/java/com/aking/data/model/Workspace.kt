package com.aking.data.model

import androidx.recyclerview.widget.DiffUtil
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ```
 * {"_creationTime":1734616071389.6255,"_id":"kn7bj5z6vh1fn1xm7a7ffv9tp576rhbh","joinCode":"qvafuecb","name":"ak的","userId":"kh7feq3fecgsnfpzahpc91janh76rcae"}
 * ```
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
) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Workspace>() {
            override fun areItemsTheSame(oldItem: Workspace, newItem: Workspace): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Workspace, newItem: Workspace): Boolean {
                return oldItem == newItem
            }
        }
    }
}