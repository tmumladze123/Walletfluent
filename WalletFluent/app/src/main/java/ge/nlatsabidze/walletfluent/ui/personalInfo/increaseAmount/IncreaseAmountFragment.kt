package ge.nlatsabidze.walletfluent.ui.personalInfo.increaseAmount

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.IncreaseAmountFragmentBinding

class IncreaseAmountFragment : BottomSheetDialogFragment() {

    private var _binding: IncreaseAmountFragmentBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = IncreaseAmountFragmentBinding.inflate(inflater, container, false)
        start()
        return binding.root
    }

    private fun start() {
        binding.btnIncreaseAmount.setOnClickListener {
            val action = IncreaseAmountFragmentDirections.actionIncreaseAmountFragmentToPersonalInfoFragment()
            findNavController().navigate(action)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}