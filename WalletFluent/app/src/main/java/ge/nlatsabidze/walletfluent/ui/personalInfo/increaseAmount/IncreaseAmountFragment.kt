package ge.nlatsabidze.walletfluent.ui.personalInfo.increaseAmount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.databinding.IncreaseAmountFragmentBinding
import ge.nlatsabidze.walletfluent.extensions.showDialogError
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IncreaseAmountFragment : BottomSheetDialogFragment() {

    private var _binding: IncreaseAmountFragmentBinding? = null
    val binding get() = _binding!!

    private val increaseAmountViewModel: IncreaseAmountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = IncreaseAmountFragmentBinding.inflate(inflater, container, false)
        start()
        observes()
        return binding.root
    }

    private fun start() {
        increaseAmountViewModel.initializeFirebase()
        binding.btnIncreaseAmount.setOnClickListener {
            if (binding.etEnterAmount.text!!.isNotEmpty() && binding.etPurpose.text!!.isNotEmpty()) {
                increaseAmountViewModel.pushTransaction(
                    binding.etEnterAmount.text.toString().toLong(),
                    binding.etPurpose.text.toString()
                )
            }
            val action =
                IncreaseAmountFragmentDirections.actionIncreaseAmountFragmentToPersonalInfoFragment()
            findNavController().navigate(action)
        }

        increaseAmountViewModel.setInformationFromDatabase()
    }

    private fun observes() {
        viewLifecycleOwner.lifecycleScope.launch {
            increaseAmountViewModel.balance.collect {
                binding.tvCurrentBalance.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            increaseAmountViewModel.showDialogError.collect {
                showDialogError("LESS MONEY", requireContext())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}