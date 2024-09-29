package com.aking.data

import android.content.Context
import com.auth0.android.result.Credentials
import dev.convex.android.ConvexClientWithAuth

/**
 * @author Ak
 * 2024/9/29 16:11
 */
class ConvexDataSource(private val convex: ConvexClientWithAuth<Credentials>)  {

    val authState get() = convex.authState

//    suspend fun storeWorkout(workout: Workout) {
//        convex.mutation<String?>(
//            "workouts:store",
//            workout.toArgs()
//        )
//    }
//
//    suspend fun subscribeToWorkoutsInRange(
//        startDate: String,
//        endDate: String
//    ): Flow<Result<List<Workout>>> =
//        convex.subscribe<List<Workout>>(
//            "workouts:getInRange",
//            mapOf("startDate" to startDate, "endDate" to endDate)
//        )


    suspend fun signIn(context: Context) {
        convex.login(context)
    }

    suspend fun signInWithCachedCredentials() {
        convex.loginFromCache()
    }

    suspend fun logout(context: Context) {
        convex.logout(context)
    }

}