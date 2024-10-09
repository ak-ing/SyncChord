package com.aking.syncchord.util

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment

/**
 * @author Ak
 * 2024/10/8 15:37
 */

/**
 * Find the NavController from the NavHostFragment
 */
fun AppCompatActivity.findNavController(id: Int): NavController {
    return (supportFragmentManager.findFragmentById(id) as NavHostFragment).navController
}

/**
 * Navigate to a destination and clear the back stack
 */
fun NavController.navigateAndClearBackStack(destinationId: Int) {
    val current = currentDestination?.id
    if (current == destinationId || current == null) {
        return
    }
    navigate(
        destinationId, null, NavOptions.Builder()
            .setPopUpTo(current, true)
            .build()
    )
}