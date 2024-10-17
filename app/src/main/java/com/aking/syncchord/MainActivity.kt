package com.aking.syncchord

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.aking.base.extended.collectWithLifecycle
import com.aking.syncchord.auth.AuthViewModel
import com.aking.syncchord.util.findNavController
import com.aking.syncchord.util.navigateAndClearBackStack
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host)
        // 观察身份验证状态并进行相应导航
        authViewModel.stateFlow.collectWithLifecycle(this) {
            val destinationId = when {
                it.isAuthenticated -> R.id.host
                else -> R.id.auth
            }
            navController.navigateAndClearBackStack(destinationId)
        }
        //authViewModel.signInAutomatically()
    }

}