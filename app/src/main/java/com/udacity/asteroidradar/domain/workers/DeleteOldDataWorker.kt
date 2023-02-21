package com.udacity.asteroidradar.domain.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.AsteroidRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DeleteOldDataWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private val repository = AsteroidRepository(context)
    private var viewModelJob = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    override fun doWork(): Result {
        try {
            ioScope.launch {
                repository.deleteOldAsteroids()
            }
        } catch (ex: Exception) {
            return Result.failure()
        }
        return Result.success()
    }
}