package ge.nlatsabidze.walletfluent.ui.personalInfo

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import android.annotation.SuppressLint
import android.util.Log.d
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.databinding.PersonalInfoFragmentBinding
import ge.nlatsabidze.walletfluent.extensions.setOnSafeClickListener
import javax.inject.Inject


@AndroidEntryPoint
class PersonalInfoFragment :
    BaseFragment<PersonalInfoFragmentBinding>(PersonalInfoFragmentBinding::inflate) {

    private val personalInfoViewModel: PersonalInfoViewModel by viewModels()

    private lateinit var transactionAdapter: TransactionsAdapter
    private var defineOnClick: Boolean = false

    @Inject
    lateinit var checkInternetConnection: CheckInternetConnection

    override fun start() {

        initializeRecyclerView()

        if (checkInternetConnection.isOnline(requireContext())) {
            personalInfoViewModel.initializeFirebase()
            personalInfoViewModel.setInformationFromDatabase()
            personalInfoViewModel.addTransaction()
            personalInfoViewModel.expireDate()
        }
        
        binding.btnIncrease.setOnSafeClickListener {
            defineOnClick = true
            val actionToIncrease =
                PersonalInfoFragmentDirections.actionPersonalInfoFragmentToIncreaseAmountFragment(defineOnClick)
            findNavController().navigate(actionToIncrease)
        }

        binding.btnDecrease.setOnSafeClickListener {
            defineOnClick = false
            val actionToDecrease =
                PersonalInfoFragmentDirections.actionPersonalInfoFragmentToIncreaseAmountFragment(defineOnClick)
            findNavController().navigate(actionToDecrease)
        }

//        transactionAdapter.onItemSelected = {
//            transactionAdapter.userTransactions.remove(it)
//            transactionAdapter.notifyDataSetChanged()
//        }

        observes()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun observes() {

        viewLifecycleOwner.lifecycleScope.launch {
            personalInfoViewModel.balance.collect {
                binding.balance.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            personalInfoViewModel.name.collect {
                binding.tvName.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            personalInfoViewModel.transaction.collect {
                transactionAdapter.userTransactions.add(it)
                transactionAdapter.notifyDataSetChanged()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            personalInfoViewModel.expireYear.collect {
                binding.tvDate.text = it
            }
        }

    }

    private fun initializeRecyclerView() {
        transactionAdapter = TransactionsAdapter()
        binding.rvItems.adapter = transactionAdapter
        binding.rvItems.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}