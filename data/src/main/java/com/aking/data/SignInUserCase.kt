package com.aking.data

import com.auth0.android.result.Credentials
import dev.convex.android.AuthState

/**
 * 是否已登录
 * @author Ak
 * 2024/9/29 17:51
 */
class SignInUserCase {

    operator fun invoke(authState: AuthState<Credentials>) = authState is AuthState.Authenticated

}