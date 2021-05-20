package com.lukianbat.feature.city.feature.city.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.lukianbat.architecture.mvvm.State
import com.lukianbat.core.common.model.CityModel
import com.lukianbat.core.utils.Optional
import com.lukianbat.coreui.utils.addAfterTextChangedListener
import com.lukianbat.coreui.utils.argument
import com.lukianbat.coreui.utils.viewBinding
import com.lukianbat.feature.city.R
import com.lukianbat.feature.city.common.di.CitiesFlowComponentController
import com.lukianbat.feature.city.common.domain.model.ChooseCityType
import com.lukianbat.feature.city.databinding.FragmentChooseCityBinding
import javax.inject.Inject

class ChooseCityFragment : Fragment(R.layout.fragment_choose_city) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by navGraphViewModels<ChooseCityViewModel>(R.id.navigation_cities) { viewModelFactory }

    private val navController by lazy { requireActivity().findNavController(R.id.host_cities) }

    private val argChooseCityType by argument { getSerializable(CHOOSE_CITY_TYPE) as ChooseCityType }

    private val binding by viewBinding(FragmentChooseCityBinding::bind)

    private val selectButton get() = binding.selectButton
    private val recyclerView get() = binding.recyclerView
    private val searchCityView get() = binding.searchCityView

    private lateinit var searchCityAdapter: CitiesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as CitiesFlowComponentController)
            .provideCitiesFlowComponent()
            .chooseCityComponent()
            .create(argChooseCityType)
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as OnBackPressedDispatcherOwner).onBackPressedDispatcher.addCallback(this) {
            requireActivity().findNavController(R.id.host_cities).popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSearchCityView()

        selectButton.setOnClickListener { viewModel.onNextButtonClicked() }

        viewModel.savedCity().observeData(viewLifecycleOwner, ::handleSavedCity)
        viewModel.cities().observe(viewLifecycleOwner, ::handleCitiesList)
        viewModel.onNext().observe(viewLifecycleOwner, {
            navController.navigate(
                R.id.cityChosenAction,
                null,
                NavOptions.Builder().setPopUpTo(R.id.navigation_cities, true).build()
            )

        })
    }

    private fun initSearchCityView() {
        searchCityAdapter = CitiesAdapter {
            viewModel.onCitySelected(it)
        }
        recyclerView.adapter = searchCityAdapter
        searchCityView.requestFocus()
        searchCityView.addAfterTextChangedListener { viewModel.onCityNameChanged(it) }
    }

    private fun handleSavedCity(city: Optional<CityModel>) {
        city.toNullable()?.let {
            selectButton.isEnabled = true
            searchCityView.setText(it.fullName)
            searchCityAdapter.submitList(listOf(CityListItem.CitySelectedItem))
            return
        }
        selectButton.isEnabled = false
        searchCityAdapter.submitList(listOf())
    }

    private fun handleCitiesList(state: State<CitiesSearchAction>) {
        when (state) {
            is State.Error -> {
                searchCityAdapter.submitList(
                    listOf(CityListItem.ErrorItem(R.string.choose_city_error_text))
                )
            }
            is State.Completed -> {
                when (val action = state.data) {
                    is CitiesSearchAction.CitiesFound -> {
                        searchCityAdapter.submitList(action.cities)
                    }
                    CitiesSearchAction.CitiesNotFound -> {
                        searchCityAdapter.submitList(
                            listOf(CityListItem.ErrorItem(R.string.choose_city_not_found_error_text))
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val CHOOSE_CITY_TYPE = "CHOOSE_CITY_TYPE"
    }
}
