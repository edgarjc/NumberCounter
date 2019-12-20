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

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.NumberDatabase
import com.example.android.trackmysleepquality.databinding.FragmentNumberTrackerBinding
import kotlinx.android.synthetic.main.fragment_number_tracker.*

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class NumberFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentNumberTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_number_tracker, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = NumberDatabase.getInstance(application).numberDatabaseDao

        val viewModelFactory = NumberViewModelFactory(dataSource, application)

        val sleepTrackerViewModel =
                ViewModelProviders.of(
                        this, viewModelFactory).get(NumberViewModel::class.java)

        setHasOptionsMenu(true)

        binding.setLifecycleOwner(this)

        binding.sleepTrackerViewModel = sleepTrackerViewModel

        //Observes for any changes on database and updates the UI
        sleepTrackerViewModel.dbCount.observe(this, Observer {dbCount ->
            dbView.text = dbCount.toString()
        }

        )
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,view!!.findNavController())
                ||super.onOptionsItemSelected(item)
    }
}
