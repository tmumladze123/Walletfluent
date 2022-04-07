package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile

import android.graphics.Color
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asFlow
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckLiveConnection
import ge.nlatsabidze.walletfluent.databinding.UserProfileFragmentBinding
import ge.nlatsabidze.walletfluent.extensions.changeVisibility
import ge.nlatsabidze.walletfluent.extensions.collectFlow
import ge.nlatsabidze.walletfluent.extensions.onSnack
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment :
    BaseFragment<UserProfileFragmentBinding>(UserProfileFragmentBinding::inflate) {

    private val userViewModel: UserProfileViewModel by viewModels()

    @Inject
    lateinit var checkLiveConnection: CheckLiveConnection

    var relatedViews: ArrayList<View> = ArrayList()

    override fun start() {
        setViewToList()
        checkLiveConnection()
    }

    override fun observes() {
        observeInformation()
    }

    private fun observeInformation() {

        collectFlow(userViewModel.name) {
            binding.tvName.text = it
        }

        collectFlow(userViewModel.currentData) {
            binding.tvDate.text = it
        }

        collectFlow(userViewModel.emailHolder) {
            binding.tvEmail.text = it
        }

        collectFlow(userViewModel.balance) {
            binding.tvBalance.text = it
        }

    }

    private fun checkLiveConnection() {
        viewLifecycleOwner.lifecycleScope.launch {
            checkLiveConnection.asFlow()
                .flowWithLifecycle(
                    viewLifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                ).collect {
                    if (it) {
                        changeVisibility(relatedViews, View.VISIBLE)
                        binding.progressBarProfile.visibility = View.INVISIBLE

                    } else if (!it) {
                        onSnack(binding.root, "Internet Connection Required", Color.RED)
                    }
                }
        }

    }

    private fun setViewToList() {
        relatedViews.add(binding.SecondMaterial)
        relatedViews.add(binding.firstMaterial)
        relatedViews.add(binding.fourthMaterial)
        relatedViews.add(binding.thirdMaterial)
        relatedViews.add(binding.accountInfo)
    }
}