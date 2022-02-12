package ge.nlatsabidze.walletfluent.ui.entry

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
import ge.nlatsabidze.walletfluent.databinding.FragmentRegisterBinding
import ge.nlatsabidze.walletfluent.extensions.setOnSafeClickListener
import ge.nlatsabidze.walletfluent.extensions.showDialogError
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private lateinit var firebaseAuth: FirebaseAuth
    private val loginViewModel: LoginRegisterViewModel by activityViewModels()


    override fun start() {

        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnSignUp.setOnSafeClickListener {
            registerUser()
        }

        observervers()
    }

    private fun registerUser() {
        with(binding) {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            checkInputValidation(email, password)
            loginViewModel.register(email, password, name, 1000)
        }
    }

    private fun observervers() {
        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.userMutableLiveFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect { userLogedIn ->
                if (userLogedIn) {
                    (activity as MainActivity).setDisableToDrawer()
                    navigateToSignInPage(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    )
                    loginViewModel.changeUserValue()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.dialogError.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect { showDialogError ->
                if (showDialogError != "") {
                    showDialogError(showDialogError)
                    loginViewModel.changeRepositoryValue()
                }
            }
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
                emailEditTextWrapper.helperText = resources.getString(R.string.invalidField)
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

    private fun navigateToSignInPage(email: String, password: String) {
        val actionRegisterFragmentToPersonal =
            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment2(email, password)
        findNavController().navigate(actionRegisterFragmentToPersonal)
    }

    private fun showDialogError(message: String) {
        showDialogError(message, requireContext())
    }


}