package com.lukianbat.feature.city.feature.routes.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.lukianbat.core.common.model.ChosenCitiesModel
import com.lukianbat.coreui.utils.color
import com.lukianbat.coreui.utils.viewBinding
import com.lukianbat.feature.city.R
import com.lukianbat.feature.city.common.di.CitiesFlowComponentController
import com.lukianbat.feature.city.common.domain.model.ChooseCityType
import com.lukianbat.feature.city.databinding.FragmentRoutesBinding
import javax.inject.Inject

class RoutesFragment : Fragment(R.layout.fragment_routes) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by navGraphViewModels<RoutesViewModel>(R.id.navigation_cities) { viewModelFactory }

    private val citiesNavController by lazy { requireActivity().findNavController(R.id.host_cities) }
    private val globalNavController by lazy { requireActivity().findNavController(R.id.host_global) }

    private val binding by viewBinding(FragmentRoutesBinding::bind)

    private val nextButton get() = binding.nextButton
    private val fromEditTextInputLayout get() = binding.fromEditTextView
    private val toEditTextInputLayout get() = binding.toEditTextView
    private val messageTextView get() = binding.messageTextView
    private val messageCardView get() = binding.messageCardView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as CitiesFlowComponentController)
            .provideCitiesFlowComponent()
            .routesComponent()
            .create()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        viewModel.onNext().observeData(viewLifecycleOwner, {
            globalNavController.navigate(R.id.chosenCitiesAction)
        })
        viewModel.chooseCity().observeData(viewLifecycleOwner, ::handleChooseCity)
        viewModel.chosenCities().observeData(viewLifecycleOwner, ::handleChosenCity)

    }

    private fun setupView() {
        nextButton.setOnClickListener { viewModel.onNextClicked() }
        fromEditTextInputLayout.setOnClickListener { viewModel.onFromCityClicked() }
        toEditTextInputLayout.setOnClickListener { viewModel.onToCityClicked() }
    }

    private fun handleChooseCity(type: ChooseCityType) {
        val bundle = bundleOf(CHOOSE_CITY_TYPE to type)
        citiesNavController.navigate(R.id.chooseCityAction, bundle)
    }

    private fun handleChosenCity(chosenCities: ChosenCitiesModel) {
        val fromCityName = chosenCities.fromCity?.fullName ?: ""
        val toCityName = chosenCities.toCity?.fullName ?: ""
        fromEditTextInputLayout.setText(fromCityName)
        toEditTextInputLayout.setText(toCityName)
        val (messageId, isError) = when {
            fromCityName.isEmpty() && toCityName.isEmpty() -> {
                R.string.routes_select_from_text to false
            }
            toCityName.isEmpty() -> {
                R.string.routes_select_to_text to false
            }
            fromCityName.isNotEmpty() && toCityName.isNotEmpty() && fromCityName == toCityName -> {
                R.string.routes_same_city_error to true
            }
            fromCityName.isNotEmpty() && toCityName.isNotEmpty() -> {
                R.string.routes_selected to false
            }
            else -> {
                R.string.routes_select_from_text to false
            }
        }
        showMessage(messageId, isError)
        nextButton.isEnabled =
            fromCityName.isNotEmpty() && toCityName.isNotEmpty() && fromCityName != toCityName
    }

    private fun showMessage(@StringRes messageId: Int, isError: Boolean) {
        messageTextView?.setText(messageId)
        if (!isError) {
            messageTextView?.setTextColor(color(R.color.textColorPrimary))
            messageCardView?.setCardBackgroundColor(color(R.color.message_card_background_color))
            return
        }
        messageCardView?.setCardBackgroundColor(color(R.color.error_card_background_color))
        messageTextView?.setTextColor(color(android.R.color.white))
    }

    companion object {
        private const val CHOOSE_CITY_TYPE = "CHOOSE_CITY_TYPE"
    }
}
