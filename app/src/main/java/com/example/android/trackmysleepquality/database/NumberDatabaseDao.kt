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

package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NumberDatabaseDao {

    @Insert
    fun insert(night: Number)

    @Update
    fun update(night: Number)

    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    fun get(key: Long): Number?

    /**
     * Selects and returns the night with given nightId.
     */
    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    fun getNumberWithId(key: Long): LiveData<Number>

    //Selects first entry
    @Query("SELECT number from daily_sleep_quality_table WHERE nightId = 1")
    fun getFirst(): LiveData<Int>

    //Selects first entry
    @Query("SELECT COUNT(*) from daily_sleep_quality_table")
    fun getCount(): Int

    @Query("DELETE FROM daily_sleep_quality_table")
    fun clear()


    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    fun getNumber(): Number?

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNumbers(): LiveData<List<Number>>





}