package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.calculatorCurrencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.model.valuteModel.Converter
import ge.nlatsabidze.walletfluent.network.currencyNetwork.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CalculatorPageViewModel @Inject constructor(private val currencyRepository: CurrencyRepository): ViewModel() {

    private val _convertedValue = MutableSharedFlow<Converter>()
    val convertedValue: MutableSharedFlow<Converter> get() = _convertedValue

    fun getConvertedValue(amount: Double, from: String, to: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                currencyRepository.getConvertedValues(amount, from, to).collectLatest {
                    _convertedValue.emit(it.data!!)
                }
            }
        }
    }
}