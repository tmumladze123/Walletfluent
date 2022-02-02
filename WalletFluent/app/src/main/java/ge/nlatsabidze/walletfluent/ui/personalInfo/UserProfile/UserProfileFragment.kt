package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckLiveConnection
import ge.nlatsabidze.walletfluent.databinding.UserProfileFragmentBinding
import ge.nlatsabidze.walletfluent.extensions.changeVisibility
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment :
    BaseFragment<UserProfileFragmentBinding>(UserProfileFragmentBinding::inflate) {

    private val userViewModel: UserProfileViewModel by viewModels()
    @Inject lateinit var connectionManager: CheckLiveConnection

    var relatedViews: ArrayList<View> = ArrayList()

    override fun start() {
        relatedViews.add(binding.SecondMaterial)
        relatedViews.add(binding.firstMaterial)
        relatedViews.add(binding.fourthMaterial)
        relatedViews.add(binding.thirdMaterial)
        relatedViews.add(binding.accountInfo)

        connectionManager.observe(viewLifecycleOwner, {
            if (it) {
                changeVisibility(relatedViews, View.VISIBLE)
                binding.progressBarProfile.visibility = View.INVISIBLE
                userViewModel.initializeFirebase()
                userViewModel.setInformationFromDatabase()

            } else if (!it) {
                changeVisibility(relatedViews, View.INVISIBLE)
                binding.progressBarProfile.visibility = View.VISIBLE
            }
        })

        if (!userViewModel.checkConnection()) {
            changeVisibility(relatedViews, View.INVISIBLE)
            binding.progressBarProfile.visibility = View.VISIBLE
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