package com.example.android.trackmysleepquality.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.NumberDatabase
import com.example.android.trackmysleepquality.databinding.DashboardFragmentBinding

class DashboardFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding:  DashboardFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.dashboard_fragment, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = NumberDatabase.getInstance(application).numberDatabaseDao

        val viewModelFactory = DashboardViewModelFactory(dataSource, application)


        // Get a reference to the ViewModel associated with this fragment.
        val dashboardViewModel =
                ViewModelProviders.of(
                        this, viewModelFactory).get(DashboardViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.dashboardViewModel = dashboardViewModel

        val adapter = CounterAdapter(CounterListener { nightId ->
            dashboardViewModel.onCountClicked(nightId)
        })

        binding.counterList.adapter = adapter
        dashboardViewModel.counts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        dashboardViewModel.navigateToNumber.observe(this, Observer { night ->
            night?.let {
                this.findNavController().navigate(
                        DashboardFragmentDirections
                                .actionDashboardFragmentToNumberFragment())
                    dashboardViewModel.onNumberNavigated()

            }
        })

       /* //The complete onClickListener with Navigation
        binding.navigate.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_dashboardFragment_to_number_fragment)
        }
*/
        //The complete onClickListener with Navigation
        binding.counterList.setOnClickListener() { view : View ->
            view.findNavController().navigate(R.id.action_dashboardFragment_to_number_fragment)
        }

        binding.setLifecycleOwner(this)

        return binding.root




    }



}
