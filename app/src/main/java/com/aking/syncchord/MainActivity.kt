package com.aking.syncchord

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.aking.base.extended.collectWithLifecycle
import com.aking.base.widget.logE
import com.aking.syncchord.auth.AuthViewModel
import dev.convex.android.AuthState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initStartUp()
        //authViewModel.signInAutomatically()
    }

    private fun initStartUp() {
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host)
                as NavHostFragment).navController
        authViewModel.authState.collectWithLifecycle(this) {
            logE(it.toString())
            if (it is AuthState.Authenticated) {
                logE("userInfo = ${it.userInfo.user}")
            }
            val destinationId = when (it) {
                is AuthState.Authenticated -> R.id.host
                is AuthState.Unauthenticated, is AuthState.AuthLoading -> R.id.auth
            }
            val current = navController.currentDestination?.id
            if (current == destinationId || current == null) {
                return@collectWithLifecycle
            }
            navController.navigate(
                destinationId, null, NavOptions.Builder()
                    .setPopUpTo(current, true)
                    .build()
            )
        }
    }

}