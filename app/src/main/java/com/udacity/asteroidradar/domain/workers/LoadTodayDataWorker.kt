package com.udacity.asteroidradar.domain.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.AsteroidRepository
import com.udacity.asteroidradar.presentation.utils.Constants.NASA_DATE_FORMATTER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Date

class LoadTodayDataWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private val repository: AsteroidRepository = AsteroidRepository(context)
    private var viewModelJob = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    override fun doWork(): Result {
        try {
            ioScope.launch {
                val todayDate = NASA_DATE_FORMATTER.format(Date())
                repository.loadAsteroids(todayDate, todayDate)
            }
        } catch (ex: Exception) {
            return Result.failure(); //или Result.retry()
        }
        return Result.success()
    }
}