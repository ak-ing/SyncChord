package com.aking.base.base

import kotlinx.coroutines.flow.Flow

/**
 * @author Created by Ak on 2024-12-19 23:05.
 */
abstract class Reactive<S>(val state: Flow<S>) {
    abstract fun render(state: S)
}