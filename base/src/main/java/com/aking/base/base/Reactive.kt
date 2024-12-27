package com.aking.base.base

/**
 * 用于渲染状态的反应式接口.
 * @author Created by Ak on 2024-12-19 23:05.
 */
interface Reactive<S> {
    /**
     * 渲染状态.
     * @param state 状态.
     */
    suspend fun render(state: S)
}