package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.countryCurrencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.model.valuteModel.CommercialRates
import ge.nlatsabidze.walletfluent.network.currencyNetwork.CurrencyRepositoryImpl
import ge.nlatsabidze.walletfluent.roomDatabase.CurrencyRoomRepository.CurrencyRoomRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrencyPageViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepositoryImpl,
    private val currencyRoomRepository: CurrencyRoomRepoImpl
) :
    ViewModel() {

    private val _commercialRates = MutableStateFlow<List<CommercialRates>>(listOf())
    val commercialRates: MutableStateFlow<List<CommercialRates>> get() = _commercialRates

    private var _showLoadingViewModelState = MutableStateFlow<Boolean>(false)
    val showLoadingViewModel: MutableStateFlow<Boolean> get() = _showLoadingViewModelState

    val currencyValues = currencyRoomRepository.currencyValues

    init {
        _showLoadingViewModelState = currencyRepository.showLoadingError()
    }

    fun getCommercialRates() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                currencyRepository.getCountryCurrencies().collectLatest {
                    if (it.data?.commercialRatesList != null) {
                        _commercialRates.value = it.data.commercialRatesList
                        currencyRoomRepository.deleteAllValues()
                        currencyRoomRepository.insertValues(it.data.commercialRatesList)
                    }
                }
            }
        }
    }

}