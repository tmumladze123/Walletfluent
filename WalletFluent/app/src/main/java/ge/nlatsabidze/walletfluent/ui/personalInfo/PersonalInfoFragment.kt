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
import ge.nlatsabidze.walletfluent.databinding.PersonalInfoFragmentBinding


@AndroidEntryPoint
class PersonalInfoFragment :
    BaseFragment<PersonalInfoFragmentBinding>(PersonalInfoFragmentBinding::inflate) {

    private val personalInfoViewModel: PersonalInfoViewModel by viewModels()

    private lateinit var transactionAdapter: TransactionsAdapter

    override fun start() {

        initializeRecyclerView()

        personalInfoViewModel.initializeFirebase()
        personalInfoViewModel.setInformationFromDatabase()
        personalInfoViewModel.addTransaction()
        personalInfoViewModel.expireDate()

        binding.btnIncrease.setOnClickListener {
            val actionToIncrease =
                PersonalInfoFragmentDirections.actionPersonalInfoFragmentToIncreaseAmountFragment()
            findNavController().navigate(actionToIncrease)
        }

        binding.btnDecrease.setOnClickListener {
            val actionToDecrease =
                PersonalInfoFragmentDirections.actionPersonalInfoFragmentToDecreaseAmountFragment()
            findNavController().navigate(actionToDecrease)
        }

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