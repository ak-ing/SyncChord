package com.aking.syncchord

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aking.syncchord.auth.AuthViewModel
import com.aking.syncchord.util.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 设置导航图的起始目的地
        val navController = findNavController(R.id.nav_host)
        lifecycleScope.launch {
            val navGraph = navController.navInflater.inflate(R.navigation.host_graph)
            val destination = if (authViewModel.hasCachedCredentials()) {
                R.id.host
            } else {
                R.id.auth
            }
            navGraph.setStartDestination(destination)
            navController.graph = navGraph

        }

        // 尝试使用以前缓存的凭据登录
        //authViewModel.signInAutomatically()
    }

}