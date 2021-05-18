package com.lukianbat.flynavigator.presentation.start

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lukianbat.flynavigator.R
import javax.inject.Inject

class StartFragment : Fragment(R.layout.fragment_start) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController.navigate(R.id.chooseCitiesAction)
//        viewModel.onboardingPassed().observeData(viewLifecycleOwner, this::handleOnboardingPassed)
    }

    private fun handleOnboardingPassed(onboardingPassed: Boolean) {
//        if (onboardingPassed) return navController.navigate(R.id.chooseCityAction)
//        navController.navigate(R.id.onboardingAction)
    }
}