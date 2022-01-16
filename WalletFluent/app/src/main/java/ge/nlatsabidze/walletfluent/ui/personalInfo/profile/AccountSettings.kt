package ge.nlatsabidze.walletfluent.ui.personalInfo.profile

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.AccountSettingsFragmentBinding
import ge.nlatsabidze.walletfluent.ui.entry.LoginFragmentDirections
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AccountSettings : BaseFragment<AccountSettingsFragmentBinding>(AccountSettingsFragmentBinding::inflate) {

    private val accountsSettingsViewModel: AccountSettingsViewModel by viewModels()

    override fun start() {
        accountsSettingsViewModel.initializeFirebase()
        accountsSettingsViewModel.getUserName()

        binding.btnTransactions.setOnClickListener { navigateToTransactionsPage() }
        binding.btnLogout.setOnClickListener{
            navigateBack()
            logOUT()
        }
        observers()
    }
    
    @SuppressLint("SetTextI18n")
    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            accountsSettingsViewModel.userName.collect { it ->
                val name = it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                binding.tvFirebaseUser.text = "Welcome $name"
            }
        }
    }

    private fun navigateToTransactionsPage() {
        val actionSettingsFragmentToPersonalPage = AccountSettingsDirections.actionAccountSettingsToPersonalInfoFragment()
        findNavController().navigate(actionSettingsFragmentToPersonalPage)
    }

    private fun navigateBack() {
        val actionSettingsFragmentToLogin = AccountSettingsDirections.actionAccountSettingsToLoginFragment()
        findNavController().navigate(actionSettingsFragmentToLogin)
    }

    private fun logOUT() {
        accountsSettingsViewModel.logOutCurrentUser()
    }


}