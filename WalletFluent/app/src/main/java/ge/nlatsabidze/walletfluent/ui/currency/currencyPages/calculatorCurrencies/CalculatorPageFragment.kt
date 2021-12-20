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

    override fun onResume() {
        super.onResume()

        val countries: Array<String> = resources.getStringArray(R.array.countries)
        val value = binding.autoCompleteFrom.text.toString()

        val countriesFrom = ArrayAdapter(requireContext(), R.layout.dropdown_item, countries)
        binding.autoCompleteFrom.setAdapter(countriesFrom)

        val countriesTo = ArrayAdapter(requireContext(), R.layout.dropdown_item, countries)
        binding.autoCompleteTo.setAdapter(countriesTo)

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
    }

    override fun start() {
        collectConvertedValue()

    }

    private fun collectConvertedValue() {

        lifecycleScope.launch {
            calculatorViewModel.convertedValue.collectLatest {
                d("dsadas", it.value.toString())
                binding.etConvertedNumber.setText(it.value.toString())
            }
        }


    }
}