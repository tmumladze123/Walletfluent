package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.countryCurrencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.useCases.GetCountryCurrenciesUseCase
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyRoomRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrencyPageViewModel @Inject constructor(
    private val currencyRoomRepository: CurrencyRoomRepoImpl,
    private val getCountryCurrienciescase: GetCountryCurrenciesUseCase
) :
    ViewModel() {

    private val _commercialRates = MutableStateFlow<List<CommercialRates>>(listOf())
    val commercialRates: MutableStateFlow<List<CommercialRates>> get() = _commercialRates

    private var _showLoadingViewModelState = MutableStateFlow<Boolean>(false)
    val showLoadingViewModel: MutableStateFlow<Boolean> get() = _showLoadingViewModelState

    val currencyValues = currencyRoomRepository.currencyValues

    fun getCommercialRates() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getCountryCurrienciescase().collect {
                    when (it) {
                        is Resource.Success -> {
                            _showLoadingViewModelState.value = false
                            _commercialRates.value = it.data?.commercialRatesList!!
                            currencyRoomRepository.deleteAllValues()
                            currencyRoomRepository.insertValues(it.data.commercialRatesList)
                        }
                        is Resource.Loading -> {
                            _showLoadingViewModelState.value = true
                        }
                    }

                }
            }
        }
    }

}