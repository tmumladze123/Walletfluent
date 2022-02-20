package ge.nlatsabidze.walletfluent.ui.entry

import android.annotation.SuppressLint
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.MainActivity
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.databinding.FragmentLoginBinding
import ge.nlatsabidze.walletfluent.extensions.setOnSafeClickListener
import ge.nlatsabidze.walletfluent.extensions.showDialogError
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val logInViewModel: LoginRegisterViewModel by activityViewModels()
    private val args: LoginFragmentArgs by navArgs()

    @Inject
    lateinit var checkInternetConnection: CheckInternetConnection

    override fun start() {

        onBackPressed()

        binding.tvSignUp.setOnClickListener { navigateToRegisterPage() }

        binding.btnSignin.setOnSafeClickListener {
            loginUser()
        }

        binding.tvForgotPassword.setOnClickListener { resetPassword() }

        if (!checkInternetConnection.isOnline(requireContext())) {
            showDialogError(
                resources.getString(R.string.CheckConnection),
                requireContext()
            )
        }

        observes()
        setDataFromRegisterPage()
    }

    private fun observes() {

        viewLifecycleOwner.lifecycleScope.launch {
            logInViewModel.userMutableLiveFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect { userLogedIn ->
                if (userLogedIn) {
                    (activity as MainActivity).setDisableToDrawer()
                    (activity as MainActivity).setUnVisible()
                    navigateToSettingsPage()
                    logInViewModel.changeUserValue()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            logInViewModel.dialogError.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect { showResetPasswordError ->
                if (showResetPasswordError != "") {
                    showDialogError(showResetPasswordError)
                    logInViewModel.changeRepositoryValue()
                }
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun loginUser() {
        with(binding) {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            checkInputValidation(email, password)
            logInViewModel.login(email, password)
        }
    }

    private fun resetPassword() {
        with(binding) {
            val email = emailEditText.text.toString()
            logInViewModel.resetPassword(email)
        }
    }


    private fun checkInputValidation(email: String, password: String) {
        with(binding) {
            val shake: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.vibrate)
            if (email.isEmpty() && password.isEmpty()) {
                emailEditTextWrapper.startAnimation(shake)
                emailEditTextWrapper.helperText = resources.getString(R.string.invalidField)
                emailEditText.setBackgroundResource(R.drawable.border)

                passwordEditTextWrapper.startAnimation(shake)
                passwordEditTextWrapper.helperText = resources.getString(R.string.invalidField)
                passwordEditText.setBackgroundResource(R.drawable.border)

            } else if (password.isEmpty()) {
                passwordEditTextWrapper.startAnimation(shake)
                passwordEditTextWrapper.helperText = resources.getString(R.string.invalidField)
                passwordEditText.setBackgroundResource(R.drawable.border)

            } else if (email.isEmpty()) {
                emailEditTextWrapper.startAnimation(shake)
                emailEditTextWrapper.helperText = resources.getString(R.string.invalidField);
                emailEditText.setBackgroundResource(R.drawable.border)

            } else {
                passwordEditTextWrapper.helperText = ""
                emailEditTextWrapper.helperText = ""
                emailEditText.setBackgroundResource(R.color.transparent)
                passwordEditText.setBackgroundResource(R.color.transparent)
            }
        }
    }

    private fun navigateToRegisterPage() {
        val actionLoginFragmentToRegister = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(actionLoginFragmentToRegister)
    }

    private fun navigateToSettingsPage() {
        val actionLoginFragmentToSettings = LoginFragmentDirections.actionLoginFragmentToAccountSettings()
        findNavController().navigate(actionLoginFragmentToSettings)
    }

    private fun showDialogError(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        showDialogError(message, requireContext())
    }

    private fun setDataFromRegisterPage() {
        if (args.email != "email" && args.password != "password") {
            binding.emailEditText.setText(args.email)
            binding.passwordEditText.setText(args.password)
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finish()
        }
    }
}