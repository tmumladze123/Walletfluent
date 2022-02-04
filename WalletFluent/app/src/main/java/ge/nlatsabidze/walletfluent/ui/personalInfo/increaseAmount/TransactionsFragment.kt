package ge.nlatsabidze.walletfluent.ui.personalInfo.increaseAmount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckLiveConnection
import ge.nlatsabidze.walletfluent.databinding.TransactionsFragmentBinding
import ge.nlatsabidze.walletfluent.extensions.showDialogError
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TransactionsFragment : BottomSheetDialogFragment() {

    private var _binding: TransactionsFragmentBinding? = null
    val binding get() = _binding!!

    private val transactionsViewModel: TransactionsViewModel by viewModels()

    private val args: TransactionsFragmentArgs by navArgs()

    @Inject
    lateinit var connectionManager: CheckLiveConnection

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TransactionsFragmentBinding.inflate(inflater, container, false)
        start()
        observes()
        return binding.root
    }

    private fun start() {

        transactionsViewModel.initializeFirebase()

        binding.btnIncreaseAmount.setOnClickListener {
            if (binding.etEnterAmount.text!!.isNotEmpty() && binding.etPurpose.text!!.isNotEmpty()) {
                if (args.defineClickType) {
                    transactionsViewModel.pushTransaction("-" + binding.etEnterAmount.text.toString(), binding.etPurpose.text.toString(), getCurrentTime(), args.defineClickType)
                } else {
                    transactionsViewModel.pushTransaction(binding.etEnterAmount.text.toString(), binding.etPurpose.text.toString(), getCurrentTime(), args.defineClickType)
                }
            }
            val action =
                TransactionsFragmentDirections.actionIncreaseAmountFragmentToPersonalInfoFragment()
            findNavController().navigate(action)
        }

        transactionsViewModel.setInformationFromDatabase()
    }

    private fun observes() {
        viewLifecycleOwner.lifecycleScope.launch {
            transactionsViewModel.balance.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                binding.tvCurrentBalance.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            transactionsViewModel.showDialogError.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                showDialogError(it, requireContext())
            }
        }
    }

    private fun getCurrentTime(): String {
        val currentFormatter = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return currentFormatter.format(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}