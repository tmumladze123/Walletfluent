package ge.nlatsabidze.walletfluent.ui.entry


import android.util.Log.d
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.FragmentRegisterBinding
import ge.nlatsabidze.walletfluent.extensions.showDialogError
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private lateinit var firebaseAuth: FirebaseAuth
    private val loginViewModel: LoginRegisterViewModel by activityViewModels()
    private lateinit var dataBase : DatabaseReference
    /*val dataBase = FirebaseDatabase.getInstance();
    val myRef = dataBase.getReference("message");
*/


    override fun start() {
        dataBase = FirebaseDatabase.getInstance().getReference("Users");

        // Read from the database
        // Read from the database

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener { registerUser() }

        listeners()
    }

    private fun registerUser() {
        with(binding) {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            checkInputValidation(email, password)
            loginViewModel.register(email, password)
        }
    }

    private fun listeners() {
        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.userMutableLiveFlow.collect { userLogedIn ->
                if (userLogedIn) {
                    navigateToSignInPage(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
                    loginViewModel.changeUserValue()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.dialogError.collect { showDialogError ->
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

    private fun navigateToSignInPage(email: String, password: String) {
        val actionRegisterFragmentToPersonal =
            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment2(email, password)
        findNavController().navigate(actionRegisterFragmentToPersonal)
    }

    private fun showDialogError(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.showDialogError(message, requireContext())
    }
}