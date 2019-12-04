/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.trackmysleepquality.database.NumberDatabaseDao
import com.example.android.trackmysleepquality.database.Number
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for SleepTrackerFragment.
 */
class NumberViewModel(
        val database: NumberDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    //Latest number
    private var number = MutableLiveData<Number?>()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val dbCount = database.getFirst()

    //Holds the count
    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int>
        get() = _count


    fun plus(){
        createDatabase()

        uiScope.launch{
            //adds to current count
            _count.value = (_count.value)?.plus(1)

            //Gets entry with ID 1 and updates number
            withContext(Dispatchers.IO) {
                val tonight = database.get(1) ?: return@withContext

                tonight.number = _count.value!!

                database.update(tonight)
            }


            number.value = getTonightFromDatabase()
        }
    }

    init {
        _count.value = dbCount.value
        initializeTonight()

    }



    private fun initializeTonight() {
        uiScope.launch {
            number.value = getTonightFromDatabase()
        }
    }



    private suspend fun getTonightFromDatabase(): Number? {

        return withContext(Dispatchers.IO) {
            var night = database.getNumber()

            night
        }
    }

    fun createDatabase(){
        if(number.value ==null) {
            uiScope.launch {
                var newNumber = Number()

                _count.value = (_count.value)?.plus(1)
                newNumber.number = count.value!!

                insert(newNumber)

                number.value = getTonightFromDatabase()

            }
        }
    }




    fun onStartTracking() {
        uiScope.launch {
            val newNight = Number()
            insert(newNight)
            number.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: Number) {
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    private suspend fun update(night: Number) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    private suspend fun updateNight(night: Number) {
        withContext(Dispatchers.IO) {
            database.get(0)
        }
    }


    fun onClear() {
        uiScope.launch {
            clear()
            number.value = null
        }
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

