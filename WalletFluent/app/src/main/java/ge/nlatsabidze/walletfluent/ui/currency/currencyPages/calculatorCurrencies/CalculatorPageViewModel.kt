package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.calculatorCurrencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.model.valuteModel.Converter
import ge.nlatsabidze.walletfluent.useCases.GetConvertedValuesUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class CalculatorPageViewModel @Inject constructor(private val getConvertedValuesUseCase: GetConvertedValuesUseCase) :
    ViewModel() {

    private var job: Job? = null

    private val _convertedValue = MutableSharedFlow<Converter>()
    val convertedValue: MutableSharedFlow<Converter> get() = _convertedValue

    private val listOfCharacter = mutableListOf(',', ' ', '-')

    fun getConvertedValue(amount: Double, from: String, to: String) {
        job?.cancel()
        job = viewModelScope.launch {
            delay(300)
            withContext(Dispatchers.IO) {
                getConvertedValuesUseCase(amount, from, to).collect {
                    when (it)  {
                        is Resource.Success -> {
                            _convertedValue.emit(it.data!!)
                        }
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

    fun checkIfNumberIsNotEmpty(number: String): Boolean {
        return number.isNotEmpty()
    }

}