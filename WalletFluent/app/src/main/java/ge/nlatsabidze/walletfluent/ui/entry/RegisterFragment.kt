package ge.nlatsabidze.walletfluent.ui.entry

import android.graphics.Color
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.MainActivity
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.databinding.FragmentRegisterBinding
import ge.nlatsabidze.walletfluent.extensions.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val loginViewModel: LoginRegisterViewModel by activityViewModels()

    override fun start() {
        binding.btnSignUp.setOnSafeClickListener { registerUser() }
    }

    private fun registerUser() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (loginViewModel.validateRegister(email, password, name)) loginViewModel.register(
            email,
            password,
            email,
            1000
        )
        else showDialogError("Fill the fields", requireContext())
    }

    private fun navigateToSignInPage(email: String, password: String) {
        val actionRegisterFragmentToPersonal =
            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment2(email, password)
        findNavController().navigate(actionRegisterFragmentToPersonal)
    }

    private fun showDialogError(message: String) {
        showDialogError(message, requireContext())
    }


    override fun observes() {
        collectFlow(loginViewModel.userMutableLiveFlow) {
            when (it) {
                is Resource.Loading -> binding.progressBar2.visible()
                is Resource.Success -> displaySuccessState()
                is Resource.Error -> it.message?.let { it1 -> displayErrorState(it1) }
                else -> showDialogError("Unexpected Error Occurred!", requireContext())
            }
        }

        collectFlow(loginViewModel.dialogError) { showDialogError ->
            if (showDialogError != "") {
                showDialogError(showDialogError)
                loginViewModel.changeRepositoryValue()
            }
        }
    }

    private fun displaySuccessState() {
        binding.progressBar2.gone()
        (activity as MainActivity).setDisableToDrawer()
        navigateToSignInPage(
            binding.emailEditText.text.toString(),
            binding.passwordEditText.text.toString()
        )
    }

    private fun displayErrorState(error: String) {
        binding.progressBar2.gone()
        showDialogError(error, requireContext())
    }

}