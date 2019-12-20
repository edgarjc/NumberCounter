package com.example.android.trackmysleepquality.ui

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.android.trackmysleepquality.database.Number
import com.example.android.trackmysleepquality.database.NumberDatabaseDao
import kotlinx.coroutines.*


class DashboardViewModel (val database: NumberDatabaseDao,
                          application: Application): ViewModel() {

    private var viewModelJob = Job()

    //Latest number
    private var number = MutableLiveData<Number?>()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    // TODO: Implement the ViewModel
    //Database number
    val counts = database.getAllNumbers()

    init {
        createDatabase()
    }

    /**
     * Navigation for the Number fragment.
     */
    private val _navigateToNumber = MutableLiveData<Long>()
    val navigateToNumber
        get() = _navigateToNumber

    fun onNumberNavigated() {
        _navigateToNumber.value = null
    }
    fun onCountClicked(id: Long) {
        Log.i("Test,","Number" + id)
        /*_navigateToSleepDetail.value = id*/
        _navigateToNumber.value = id

    }

    //On init, if database is empy, its populated with this data
    fun createDatabase(){
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val counts = database.getCount()

                if (counts == 0) {
                    var newNumber = Number()

                    newNumber.number = 0

                    insert(newNumber)
                }
                Log.i("test-------------->",":" + counts)
            }
        }
    }

    private suspend fun insert(night: Number) {
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }
}
