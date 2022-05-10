package ge.nlatsabidze.walletfluent.ui.entry

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.MainActivity
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckLiveConnection
import ge.nlatsabidze.walletfluent.databinding.FragmentLoginBinding
import ge.nlatsabidze.walletfluent.extensions.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val logInViewModel: LoginRegisterViewModel by activityViewModels()
    private val args: LoginFragmentArgs by navArgs()

    override fun start() {

        binding.tvSignUp.setOnSafeClickListener { navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment()) }
        binding.btnSignin.setOnSafeClickListener { loginUser() }
        binding.tvForgotPassword.setOnSafeClickListener { resetPassword() }

        setDataFromRegisterPage()
    }

    private fun loginUser() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        if (logInViewModel.validate(email, password)) logInViewModel.login(email, password)
        else showDialogError("Fill the fields", requireContext())
    }

    private fun resetPassword() {
        val email = binding.emailEditText.text.toString()
        logInViewModel.resetPassword(email)
    }

    private fun navigate(directions: NavDirections) = findNavController().navigate(directions)

    private fun showDialogError(message: String) {
        AlertDialog.Builder(requireContext())
        showDialogError(message, requireContext())
    }

    private fun setDataFromRegisterPage() {
        if (args.email != "email" && args.password != "password") {
            binding.emailEditText.setText(args.email)
            binding.passwordEditText.setText(args.password)
        }
    }

    override fun observes() {
        collectFlow(logInViewModel.userMutableLiveFlow) { authResult ->
            when (authResult) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> logUser()
                is Resource.Error -> displayError(authResult.message!!)
                else -> showDialogError("Something went wrong!", requireContext())
            }
        }

        collectFlow(logInViewModel.dialogError) { showResetPasswordError ->
            if (showResetPasswordError != "") {
                showDialogError(showResetPasswordError)
                logInViewModel.changeRepositoryValue()
            }
        }
    }

    private fun logUser() {
        binding.progressBar.gone()
        (activity as MainActivity).setDisableToDrawer()
        (activity as MainActivity).setUnVisible()
        navigate(LoginFragmentDirections.actionLoginFragmentToAccountSettings())
    }

    private fun displayError(item: String) {
        showDialogError(item, requireContext())
        binding.progressBar.gone()
    }
}