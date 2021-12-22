package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.calculatorCurrencies

import android.text.Editable
import android.util.Log.d
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.CalculatorPageFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.widget.AutoCompleteTextView
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener


@AndroidEntryPoint
class CalculatorPageFragment :
    BaseFragment<CalculatorPageFragmentBinding>(CalculatorPageFragmentBinding::inflate) {

    private val calculatorViewModel: CalculatorPageViewModel by viewModels()

    override fun start() {
        collectConvertedValue()
        setValueToResult()
    }

    private fun collectConvertedValue() {

        val countries = resources.getStringArray(R.array.countries)
//        val value = binding.autoCompleteFrom.text.toString()

        val countriesFrom = ArrayAdapter(requireContext(), R.layout.dropdown_item, countries)
        binding.autoCompleteFrom.setAdapter(countriesFrom)
        binding.autoCompleteTo.setAdapter(countriesFrom)

        var firstValue = binding.textInputLayoutWrapper.editText?.text.toString()
        binding.autoCompleteFrom.onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String? = countriesFrom.getItem(position)
                firstValue = selectedValue!!
                d("dsadsa", firstValue)
            }

        var secondValue = binding.secondTextInputLayoutWrapper.editText?.text.toString()
        binding.autoCompleteTo.onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String? = countriesFrom.getItem(position)
                secondValue = selectedValue!!
                d("dsadsa", secondValue)
            }

        binding.btnConverter.setOnClickListener {
            if (binding.etNumber.text.toString().isNotEmpty()) {
                val amount = binding.etNumber.text.toString().toDouble()
                d("amount", amount.toString())
                calculatorViewModel.getConvertedValue(amount, firstValue, secondValue)
            }
        }

        binding.btnSwap.setOnClickListener {
            firstValue = secondValue.also { secondValue = firstValue }

            binding.textInputLayoutWrapper.editText?.setText(firstValue)
            binding.secondTextInputLayoutWrapper.editText?.setText(secondValue)

//            val newCountries = ArrayAdapter(requireContext(), R.layout.dropdown_item, countries)
//            binding.autoCompleteFrom.setAdapter(newCountries)
//            binding.autoCompleteTo.setAdapter(newCountries)
            binding.autoCompleteFrom.showDropDown()
            binding.autoCompleteTo.showDropDown()

//            binding.autoCompleteFrom.onItemClickListener =
//                OnItemClickListener { adapterView, view, position, id ->
//                    val selectedValue: String? = countriesFrom.getItem(position)
//                    secondValue = selectedValue!!
//                    d("dsadsa", secondValue)
//                }
        }

    }

    private fun setValueToResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            calculatorViewModel.convertedValue.collectLatest {
                binding.etConvertedNumber.setText(it.value.toString())
            }
        }
    }
}