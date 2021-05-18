package com.lukianbat.feature.city.feature

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.lukianbat.feature.city.R
import com.lukianbat.feature.city.common.di.CitiesFlowComponentController
import javax.inject.Inject

class CitiesFlowFragment : Fragment(R.layout.fragment_cities_flow) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<CitiesFlowViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as CitiesFlowComponentController)
            .provideCitiesFlowComponent()
            .inject(this)
    }
}
