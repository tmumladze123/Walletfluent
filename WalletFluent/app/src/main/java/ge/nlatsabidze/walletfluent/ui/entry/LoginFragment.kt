package ge.nlatsabidze.walletfluent.ui.entry

import android.annotation.SuppressLint
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.databinding.FragmentLoginBinding
import ge.nlatsabidze.walletfluent.extensions.showDialogError
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val logInViewModel: LoginRegisterViewModel by activityViewModels()

    private lateinit var firebaseAuth: FirebaseAuth
    private val args: LoginFragmentArgs by navArgs()

    @Inject
    lateinit var checkInternetConnection: CheckInternetConnection

    override fun start() {


        firebaseAuth = FirebaseAuth.getInstance()
        binding.tvSignUp.setOnClickListener { navigateToRegisterPage() }

        binding.btnSignin.setOnClickListener { loginUser() }

        binding.tvForgotPassword.setOnClickListener { resetPassword() }

        if (checkInternetConnection.isOnline(activity!!.application).toString() == "false") {
            val builder = AlertDialog.Builder(requireContext())
            builder.showDialogError("In Order to use our application, you should be connected to internet", requireContext())
        }

        listeners()
        setDataFromRegisterPage()
    }

    private fun listeners() {

        viewLifecycleOwner.lifecycleScope.launch {
            logInViewModel.userMutableLiveFlow.collect { userLogedIn ->
                if (userLogedIn) {
                    navigateToPersonalPage()
                    logInViewModel.changeUserValue()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            logInViewModel.dialogError.collect { showResetPasswordError ->
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
                emailEditTextWrapper.helperText = ""
                passwordEditText.setBackgroundResource(R.drawable.border)
                emailEditText.setBackgroundResource(R.color.transparent)
            } else if (email.isEmpty()) {
                emailEditTextWrapper.startAnimation(shake)
                emailEditTextWrapper.helperText = resources.getString(R.string.invalidField);
                passwordEditTextWrapper.helperText = ""
                emailEditText.setBackgroundResource(R.drawable.border)
                passwordEditText.setBackgroundResource(R.color.transparent)
            } else {
                passwordEditTextWrapper.helperText = ""
                emailEditTextWrapper.helperText = ""
                emailEditText.setBackgroundResource(R.color.transparent)
                passwordEditText.setBackgroundResource(R.color.transparent)
                passwordEditText.setBackgroundResource(R.color.transparent)
            }
        }
    }

    private fun navigateToRegisterPage() {
        val actionLoginFragmentToRegister = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(actionLoginFragmentToRegister)
    }

    private fun navigateToPersonalPage() {
        val actionLoginFragmentToPersonal = LoginFragmentDirections.actionLoginFragmentToPersonalInfoFragment()
        findNavController().navigate(actionLoginFragmentToPersonal)
    }

    private fun showDialogError(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.showDialogError(message, requireContext())
    }

    private fun setDataFromRegisterPage() {
        if (args.email != "email" && args.password != "password") {
            binding.emailEditText.setText(args.email)
            binding.passwordEditText.setText(args.password)
        }
    }
}