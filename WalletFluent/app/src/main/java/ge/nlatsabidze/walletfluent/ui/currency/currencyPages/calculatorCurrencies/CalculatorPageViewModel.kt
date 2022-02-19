package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.calculatorCurrencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.model.valuteModel.Converter
import ge.nlatsabidze.walletfluent.network.currencyNetwork.CurrencyRepositoryImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class CalculatorPageViewModel @Inject constructor(private val currencyRepository: CurrencyRepositoryImpl): ViewModel() {

    private var job: Job? = null

    private val _convertedValue = MutableSharedFlow<Converter>()
    val convertedValue: MutableSharedFlow<Converter> get() = _convertedValue

    private val listOfCharacter = mutableListOf<Char>(',', ' ', '-')

    fun getConvertedValue(amount: Double, from: String, to: String) {
        job?.cancel()
        job = viewModelScope.launch {
            delay(300)
            withContext(Dispatchers.IO) {
                currencyRepository.getConvertedValues(amount, from, to).collect {
                    if (it.data != null) {
                        _convertedValue.emit(it.data)
                    }
                }
            }
        }
    }

    fun containsError(number: String): Boolean {
        var result = false
        for (i in number.indices) {
            if (listOfCharacter.contains(number[i])) {
                result = true
            }
        }
        return result
    }

}