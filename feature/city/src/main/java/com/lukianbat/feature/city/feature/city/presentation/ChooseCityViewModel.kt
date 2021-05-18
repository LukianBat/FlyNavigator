package com.lukianbat.feature.city.feature.city.presentation

import com.lukianbat.architecture.mvvm.RxViewModel
import com.lukianbat.architecture.mvvm.RxViewOutput
import com.lukianbat.core.common.CityModel
import com.lukianbat.core.utils.Optional
import com.lukianbat.core.utils.toOptional
import com.lukianbat.feature.city.common.domain.model.ChooseCityType
import com.lukianbat.feature.city.common.domain.usecase.CityInteractor
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChooseCityViewModel @Inject constructor(
    private val chooseCityType: ChooseCityType,
    private val interactor: CityInteractor,
    errorAdapter: RxViewOutput.ErrorAdapter
) : RxViewModel() {

    private val cities = RxViewOutput<CitiesSearchAction>(this, RxViewOutput.Strategy.ONCE)
    private val onNext = RxViewOutput<Unit>(this, RxViewOutput.Strategy.ONCE)
    private val savedCity = RxViewOutput<Optional<CityModel>>(this)

    private val citiesBehaviorSubject = BehaviorSubject.create<String>()
    private var selectedCityModel: CityModel? = null

    init {
        val savedCitySource = interactor.getChosenCities()
            .map {
                val city = when (chooseCityType) {
                    ChooseCityType.CHOOSE_CITY_FROM -> it.fromCity
                    ChooseCityType.CHOOSE_CITY_TO -> it.toCity
                }
                city.toOptional()
            }

        val searchOrganizationsObservable = citiesBehaviorSubject
            .doOnNext { selectedCityModel = null }
            .debounce(REQUEST_DELAY, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap { name ->
                if (name.length < CITY_NAME_MIN_LENGTH) {
                    return@switchMap Observable.just(CitiesSearchAction.WrongCitiesFormatInput)
                }
                interactor.searchCity(name)
                    .flatMap { cities ->
                        if (cities.isNullOrEmpty()) return@flatMap Single.just(CitiesSearchAction.CitiesNotFound)
                        Single.just(
                            CitiesSearchAction.CitiesFound(
                                cities.map { CityListItem.CityItem(it) }
                            )
                        )
                    }
                    .toObservable()
            }

        cities.source(searchOrganizationsObservable, errorAdapter)
        savedCity.source(savedCitySource, errorAdapter)
    }

    fun savedCity() = savedCity.asOutput()

    fun cities() = cities.asOutput()

    fun onNext() = onNext.asOutput()

    fun onCitySelected(item: CityModel) {
        selectedCityModel = item
        savedCity.valueSource(item.toOptional())
    }

    fun onCityNameChanged(name: String) {
        if (name == selectedCityModel?.fullName) return
        savedCity.valueSource(null.toOptional())
        citiesBehaviorSubject.onNext(name)
        cities.retry()
    }

    fun onNextButtonClicked() {
        when (chooseCityType) {
            ChooseCityType.CHOOSE_CITY_FROM -> interactor.setFromCity(selectedCityModel)
            ChooseCityType.CHOOSE_CITY_TO -> interactor.setToCity(selectedCityModel)
        }
        onNext.valueSource(Unit)
    }

    companion object {
        private const val REQUEST_DELAY = 300L
        private const val CITY_NAME_MIN_LENGTH = 3
    }
}
