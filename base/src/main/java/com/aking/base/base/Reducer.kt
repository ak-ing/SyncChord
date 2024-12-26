package com.aking.base.base

/**
 * @author Created by Ak on 2024-12-19 23:04.
 */
interface Intent

interface Reducer<I : Intent> {
    fun reducer(intent: I)
}