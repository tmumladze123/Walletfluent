package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.databinding.UserProfileFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment :
    BaseFragment<UserProfileFragmentBinding>(UserProfileFragmentBinding::inflate) {

    private val userViewModel: UserProfileViewModel by viewModels()

    @Inject
    lateinit var checkInternetConnection: CheckInternetConnection

    override fun start() {

        if (checkInternetConnection.isOnline(requireContext())) {
            userViewModel.initializeFirebase()
            userViewModel.setInformationFromDatabase()
        }

        observeInformation()
    }

    private fun observeInformation() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.name.collect {
                binding.tvName.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.currentData.collect {
                binding.tvDate.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.emailHolder.collect {
                binding.tvEmail.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.balance.collect {
                binding.tvBalance.text = it
            }
        }
    }
}