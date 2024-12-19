package com.aking.base.base

/**
 * @author Created by Ak on 2024-12-19 23:05.
 */
interface Reactive<S> {
    fun render(state: S)
}