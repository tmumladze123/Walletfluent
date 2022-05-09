package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.countryCurrencies

import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.extensions.collect
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyUseCases.CurrencyRoomUseCases
import ge.nlatsabidze.walletfluent.ui.baseViewModel.BaseViewModel
import ge.nlatsabidze.walletfluent.useCases.GetCountryCurrenciesUseCase
import ge.nlatsabidze.walletfluent.util.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CurrencyPageViewModel @Inject constructor(
    private val getCountryCurrencies: GetCountryCurrenciesUseCase,
    private val currencyRoomUseCases: CurrencyRoomUseCases,
    dispatchers: Dispatchers
) :
    BaseViewModel(dispatchers) {

    private val _commercialRates = MutableStateFlow<List<CommercialRates>>(listOf())
    val commercialRates: MutableStateFlow<List<CommercialRates>> get() = _commercialRates

    val currencyValues = currencyRoomUseCases.getValuesUseCase()

    fun getCommercialRates() = coroutineIoLauncher {
        collect(getCountryCurrencies.invoke()) { state ->
            when (state) {
                is Resource.Success -> {
                    showLoadingViewModel.value = false
                    val commercialList = state.data?.commercialRatesList!!
                    _commercialRates.value = commercialList
                    deleteAndInsertValues(state.data.commercialRatesList)
                }
                is Resource.Loading -> {
                    showLoadingViewModel.value = true
                }
                else -> {
                    _commercialRates.value = emptyList()
                }
            }
        }
    }

    private suspend fun deleteAndInsertValues(commercialList: List<CommercialRates>) {
        currencyRoomUseCases.deleteValues()
        currencyRoomUseCases.insertValuesUseCase(commercialList)
    }

}
