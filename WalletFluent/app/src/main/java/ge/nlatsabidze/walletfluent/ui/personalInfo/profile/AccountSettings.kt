package ge.nlatsabidze.walletfluent.ui.personalInfo.profile

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.*
import ge.nlatsabidze.walletfluent.databinding.AccountSettingsFragmentBinding
import ge.nlatsabidze.walletfluent.extensions.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AccountSettings : BaseFragment<AccountSettingsFragmentBinding>(AccountSettingsFragmentBinding::inflate) {

    private val accountsSettingsViewModel: AccountSettingsViewModel by viewModels()

    @Inject lateinit var settingsManager: SettingsManager
    private var isDarkMode = true

    override fun start() {

        observeUiPreferences()

        binding.switchMode.setOnClickListener {
            setUpUi()
        }

        accountsSettingsViewModel.initializeFirebase()
        accountsSettingsViewModel.getUserName()


        binding.btnTransactions.setOnClickListener { navigateToTransactionsPage() }
        binding.btnLogout.setOnClickListener{
            logOUT()
            (activity as MainActivity).setVisible()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            (activity as MainActivity).recreate()
            navigateBack()
        }

        binding.btnUserProfileInfo.setOnClickListener {
            val action = AccountSettingsDirections.actionAccountSettingsToUserProfileFragment()
            findNavController().navigate(action)
        }

        binding.btnResetPassword.setOnSafeClickListener {
            accountsSettingsViewModel.changeUserPassword()
        }

    }

    override fun observes() {
        observers()
    }

    @SuppressLint("SetTextI18n")
    private fun observers() {

        collectFlow(accountsSettingsViewModel.userName) {
            binding.rootConstraint.showAll()
            binding.loadingBar.visibility = View.INVISIBLE
            binding.tvFirebaseUser.text = "Welcome $it"
        }

        collectFlow(accountsSettingsViewModel.loaderFlow) {
            if (it) {
                binding.rootConstraint.hideAll()
                binding.loadingBar.visible()
            } else {
                binding.rootConstraint.showAll()
                binding.loadingBar.gone()
            }
        }

        collectFlow(accountsSettingsViewModel.showChangePasswordDialog) {
            showDialogError(it, requireContext())
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

    private fun setUpUi() {
        lifecycleScope.launch {
            when (isDarkMode) {
                true -> settingsManager.setUpUiMode(UiMode.LIGHT)
                false -> settingsManager.setUpUiMode(UiMode.DARK)
            }
        }
    }

    private fun observeUiPreferences() {
        lifecycleScope.launch {
            settingsManager.uiModeFlow.collect {
                it.let {
                    when (it) {
                        UiMode.LIGHT -> onLightMode()
                        UiMode.DARK -> onDarkMode()
                    }
                }
            }
        }
    }

    private fun onLightMode() {
        isDarkMode = false
        binding.switchMode.isChecked = false
        binding.switchMode.text = resources.getString(R.string.SwitchToDark)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun onDarkMode() {
        isDarkMode = true
        binding.switchMode.isChecked = true
        binding.switchMode.text = resources.getString(R.string.SwitchToLight)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

}