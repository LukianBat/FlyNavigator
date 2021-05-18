package com.lukianbat.tickets.feature

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.lukianbat.feature.map.R
import com.lukianbat.tickets.common.di.TicketsFlowComponentController
import javax.inject.Inject

class TicketsFlowFragment: Fragment(R.layout.fragment_tickets_flow) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<TicketsFlowViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as TicketsFlowComponentController)
            .provideTicketsFlowComponent()
            .inject(this)
    }

}