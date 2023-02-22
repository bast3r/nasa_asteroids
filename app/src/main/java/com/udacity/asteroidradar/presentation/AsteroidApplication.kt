package com.udacity.asteroidradar.presentation

import android.app.Application
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.udacity.asteroidradar.data.AsteroidRepository
import com.udacity.asteroidradar.domain.workers.DeleteOldDataWorker
import com.udacity.asteroidradar.domain.workers.LoadTodayDataWorker
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class AsteroidApplication: Application() {
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }

    override fun onCreate() {
        super.onCreate()
        initWorkManager()

        GlobalScope.launch(exceptionHandler) {
            AsteroidRepository(applicationContext).loadTodayAsteroids()
        }
    }

    private fun initWorkManager() {
        initLoaderWorker()
        initDeleterWorker()
    }

    private fun initLoaderWorker() {
        val loadingConstraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val loadTodayDataRequest = PeriodicWorkRequestBuilder<LoadTodayDataWorker>(1, TimeUnit.DAYS, 1, TimeUnit.MINUTES)
            .setConstraints(loadingConstraints)
            .addTag(LOADER_DATA_WORKER_TAG)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(loadTodayDataRequest)
    }

    private fun initDeleterWorker() {
        val deleteOldDataRequest = PeriodicWorkRequestBuilder<DeleteOldDataWorker>(1, TimeUnit.DAYS, 10, TimeUnit.MINUTES)
            .addTag(DELETER_DATA_WORKER_TAG)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(deleteOldDataRequest)
    }

    companion object {
        const val LOADER_DATA_WORKER_TAG = "LOADER_DATA_WORKER_TAG"
        const val DELETER_DATA_WORKER_TAG = "DELETER_DATA_WORKER_TAG"
    }
}