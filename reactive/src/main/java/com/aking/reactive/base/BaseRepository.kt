package com.aking.reactive.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.Closeable

/**
 * Created by Rick on 2023-07-08  16:57.
 * Description: Model层基类，异常捕获，状态回调封装
 */
abstract class BaseRepository : Closeable {

    /**
     *
     * 发起请求封装
     * 该方法将切换至IO线程执行
     *
     * @param requestBlock 请求的整体逻辑
     * @return T
     */
    protected suspend inline fun <T> request(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        crossinline requestBlock: suspend () -> T
    ): T = withContext(dispatcher) { requestBlock.invoke() }

    /**
     *
     * 发起请求封装
     * 该方法将返回flow，并将请求切换至IO线程
     *
     * @param requestBlock 请求的整体逻辑
     * @return Flow<T>
     */
    protected fun <T> requestFlow(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        requestBlock: suspend () -> T
    ): Flow<T> {
        return flow { emit(requestBlock()) }.flowOn(dispatcher)
    }

    override fun close() {
        Timber.d("[close]")
    }
}