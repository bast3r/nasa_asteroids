package com.udacity.asteroidradar.presentation.main

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.AsteroidRepository
import com.udacity.asteroidradar.data.entities.Asteroid
import com.udacity.asteroidradar.data.mappers.map
import com.udacity.asteroidradar.data.ws.NasaRetrofit
import com.udacity.asteroidradar.domain.model.PictureOfDayModel
import com.udacity.asteroidradar.presentation.utils.Constants.NASA_DATE_FORMATTER
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class MainViewModel(context: Context) : AndroidViewModel(context as Application) {

    private val nasaApi = NasaRetrofit.nasaApi
    private val repository: AsteroidRepository = AsteroidRepository(context)

    private var _pictureOfDay = MutableLiveData<PictureOfDayModel>()
    val pictureOfDay: LiveData<PictureOfDayModel> = _pictureOfDay

    private val today = Calendar.getInstance().time
    private val weekAgo = Calendar.getInstance()

    init {
        weekAgo.add(Calendar.DAY_OF_YEAR, -7)
    }

    private var _queryFrom = MutableLiveData(TODAY_DATA)
    private var _asteroidList = Transformations.switchMap(_queryFrom) {
        _progressBarShowing.value = true
        when (it) {
            ALL_DATA -> repository.getAll()
            WEEK_DATA -> getWeekAsteroids()
            else -> repository.getAllForToday(Date())
        }
    }
    val asteroidList: LiveData<List<Asteroid>> = _asteroidList

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
        Toast.makeText(context, R.string.cant_load_data, Toast.LENGTH_LONG).show()
        _progressBarShowing.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun loadTodayAsteroids() {
        _queryFrom.value = TODAY_DATA
    }

    fun loadAllSavedAsteroids() {
        _queryFrom.value = ALL_DATA
    }

    fun loadWeekAsteroids() {
        _queryFrom.value = WEEK_DATA
    }

    private fun getWeekAsteroids(): LiveData<List<Asteroid>> {
        loadWeekData(weekAgo.time, today)
        return repository.getWeekAsteroids(weekAgo.time, today)
    }

    private var _progressBarShowing = MutableLiveData(true)
    val progressBarShowing: LiveData<Boolean> = _progressBarShowing

    private fun loadWeekData(dateStart: Date, dateEnd: Date) {
        uiScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                repository.loadAsteroids(
                    dateStart = NASA_DATE_FORMATTER.format(dateStart),
                    dateEnd = NASA_DATE_FORMATTER.format(dateEnd)
                )
            }
            _progressBarShowing.value = false
        }
    }

    fun getPictureOfDay() {
        uiScope.launch(exceptionHandler) {
            _pictureOfDay.value = nasaApi.getPictureOfDay().map()
        }
    }

    companion object {
        const val ALL_DATA = 0
        const val WEEK_DATA = 1
        const val TODAY_DATA = 2
    }
}