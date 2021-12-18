package ge.nlatsabidze.walletfluent.ui.login

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val logInViewModel: LoginViewModel by activityViewModels()

    private lateinit var firebaseAuth: FirebaseAuth
    private val args: LoginFragmentArgs by navArgs()

    override fun start() {

        firebaseAuth = FirebaseAuth.getInstance()
        binding.tvSignUp.setOnClickListener { navigateToRegisterPage() }

        binding.btnSignin.setOnClickListener { loginUser() }

        binding.tvForgotPassword.setOnClickListener { resetPassword() }

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

    private fun showDialogError(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
        builder.setPositiveButton("yes", { dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }

    private fun showVerificationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("დაადასტურეთ მეილი!")
        builder.setPositiveButton("კარგი", { dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }

    private fun showErrorDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("მომხმარებლის სახელი ან პაროლი არასწორია, გთხოვთ სცადოთ თავიდან")
        builder.setPositiveButton("კარგი", { dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }


    private fun checkInputValidation(email: String, password: String) {
        with(binding) {
            val shake: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.vibrate)
            if (email.isEmpty() && password.isEmpty()) {
                emailEditTextWrapper.startAnimation(shake)
                emailEditTextWrapper.helperText = "ველი არ არის შევსებული"
                emailEditText.setBackgroundResource(R.drawable.border)

                passwordEditTextWrapper.startAnimation(shake)
                passwordEditTextWrapper.helperText = "ველი არ არის შევსებული"
                passwordEditText.setBackgroundResource(R.drawable.border)

            } else if (password.isEmpty()) {
                passwordEditTextWrapper.startAnimation(shake)
                passwordEditTextWrapper.helperText = "ველი არ არის შევსებული"
                emailEditTextWrapper.helperText = ""
                passwordEditText.setBackgroundResource(R.drawable.border)
                emailEditText.setBackgroundResource(R.color.transparent)
            } else if (email.isEmpty()) {
                emailEditTextWrapper.startAnimation(shake)
                emailEditTextWrapper.helperText = "ველი არ არის შევსებული"
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
        val actionLoginFragmentToRegister =
            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(actionLoginFragmentToRegister)
    }

    private fun navigateToPersonalPage() {
        val actionLoginFragmentToPersonal =
            LoginFragmentDirections.actionLoginFragmentToPersonalInfoFragment()
        findNavController().navigate(actionLoginFragmentToPersonal)
    }

    private fun setDataFromRegisterPage() {
        if (args.email != "email" && args.password != "password") {
            binding.emailEditText.setText(args.email)
            binding.passwordEditText.setText(args.password)
        }
    }

    private fun resetPassword() {
        with(binding) {
            val email = emailEditText.text.toString()
            logInViewModel.resetPassword(email)
        }
    }
}