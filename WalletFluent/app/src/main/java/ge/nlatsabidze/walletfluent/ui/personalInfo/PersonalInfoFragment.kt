package ge.nlatsabidze.walletfluent.ui.personalInfo

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log.d
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asFlow
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckLiveConnection
import ge.nlatsabidze.walletfluent.databinding.PersonalInfoFragmentBinding
import ge.nlatsabidze.walletfluent.extensions.changeVisibility
import ge.nlatsabidze.walletfluent.extensions.collectFlow
import ge.nlatsabidze.walletfluent.extensions.onSnack
import ge.nlatsabidze.walletfluent.extensions.setOnSafeClickListener
import javax.inject.Inject


@AndroidEntryPoint
class PersonalInfoFragment :
    BaseFragment<PersonalInfoFragmentBinding>(PersonalInfoFragmentBinding::inflate) {

    private val personalInfoViewModel: PersonalInfoViewModel by viewModels()

    private lateinit var transactionAdapter: TransactionsAdapter
    private var defineOnClick: Boolean = false

    @Inject
    lateinit var checkLiveConnection: CheckLiveConnection

    var relatedViews: ArrayList<View> = ArrayList()

    override fun start() {

        personalInfoViewModel.initializeFirebase()
        personalInfoViewModel.setInformationFromDatabase()
        personalInfoViewModel.getTransactions()
        personalInfoViewModel.expireDate()

        setViews()
        checkLiveConnection()

        initRecycler()

        binding.btnIncrease.setOnSafeClickListener {
            defineOnClick = true
            val actionToIncrease =
                PersonalInfoFragmentDirections.actionPersonalInfoFragmentToIncreaseAmountFragment(
                    defineOnClick
                )
            findNavController().navigate(actionToIncrease)
        }

        binding.btnDecrease.setOnSafeClickListener {
            defineOnClick = false
            val actionToDecrease =
                PersonalInfoFragmentDirections.actionPersonalInfoFragmentToIncreaseAmountFragment(
                    defineOnClick
                )
            findNavController().navigate(actionToDecrease)
        }
    }

    override fun observes() {
        observers()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun observers() {

        collectFlow(personalInfoViewModel.balance) {
            binding.balance.text = it
        }

        collectFlow(personalInfoViewModel.name) {
            binding.tvName.text = it
        }

        collectFlow(personalInfoViewModel.transaction) {
            transactionAdapter.userTransactions.add(0, it)
            binding.rvItems.startLayoutAnimation()
            transactionAdapter.notifyDataSetChanged()
        }

        collectFlow(personalInfoViewModel.expireYear) {
            binding.tvDate.text = it
        }

    }

    private fun initRecycler() {
        transactionAdapter = TransactionsAdapter()
        binding.rvItems.adapter = transactionAdapter
        binding.rvItems.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun checkLiveConnection() {
        collectFlow(checkLiveConnection.asFlow()) {
            if (!it) {
                onSnack(
                    binding.root,
                    resources.getString(R.string.InternetRequired),
                    Color.RED
                )
            } else {
                binding.progressBarInfo.visibility = View.INVISIBLE
                changeVisibility(relatedViews, View.VISIBLE)
            }
        }
    }

    private fun setViews() {
        relatedViews.add(binding.recyclerCardView)
        relatedViews.add(binding.yourCard)
        relatedViews.add(binding.tvAvailable)
        relatedViews.add(binding.btnIncrease)
        relatedViews.add(binding.btnDecrease)
        relatedViews.add(binding.balance)
    }
}