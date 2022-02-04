package ge.nlatsabidze.walletfluent.ui.personalInfo

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import android.annotation.SuppressLint
import android.util.Log.d
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.databinding.PersonalInfoFragmentBinding
import ge.nlatsabidze.walletfluent.extensions.changeVisibility
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

    var relatedViews: ArrayList<View> = ArrayList()

    override fun start() {

        relatedViews.add(binding.recyclerCardView)
        relatedViews.add(binding.yourCard)
        relatedViews.add(binding.tvAvailable)
        relatedViews.add(binding.btnIncrease)
        relatedViews.add(binding.btnDecrease)
        relatedViews.add(binding.balance)

        initializeRecyclerView()

        if (personalInfoViewModel.checkConnection()) {
            personalInfoViewModel.initializeFirebase()
            personalInfoViewModel.setInformationFromDatabase()
            personalInfoViewModel.addTransaction()
            personalInfoViewModel.expireDate()
        }

        if (!personalInfoViewModel.checkConnection()) {
            changeVisibility(relatedViews, View.INVISIBLE)
            binding.progressBarInfo.visibility = View.VISIBLE
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

        observes()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun observes() {

        viewLifecycleOwner.lifecycleScope.launch {
            personalInfoViewModel.balance.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                binding.balance.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            personalInfoViewModel.name.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                binding.tvName.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            personalInfoViewModel.transaction.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                transactionAdapter.userTransactions.add(it)
                transactionAdapter.notifyDataSetChanged()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            personalInfoViewModel.expireYear.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
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