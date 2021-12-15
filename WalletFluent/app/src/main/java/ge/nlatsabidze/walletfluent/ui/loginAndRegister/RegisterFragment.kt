package ge.nlatsabidze.walletfluent.ui.loginAndRegister

import android.content.DialogInterface
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.FragmentRegisterBinding

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private lateinit var firebaseAuth: FirebaseAuth
    private val registerViewModel: LoginAndRegisterViewModel by activityViewModels()


    override fun start() {

        firebaseAuth = FirebaseAuth.getInstance()


//        registerViewModel.showDialogError()?.observe(viewLifecycleOwner, {
//            if (it == true) {
//                showDialogError()
//            }
//        })

        binding.btnSignUp.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        with(binding) {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            checkInputValidation(email, password)
            registerViewModel.register(email, password)

            registerViewModel.getMutableLiveData()?.observe(viewLifecycleOwner, { firebaseUser ->
                if (firebaseUser != null) {
                    navigateToSignInPage(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
                }
            })
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
        val actionRegisterToLogin =
            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment2(email, password)
        findNavController().navigate(actionRegisterToLogin)
    }

    private fun showDialogError() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("ვწუხვართ, მითითებული სახელი ან პაროლი არასწორია, სცადე განმეორებით")
        builder.setPositiveButton("yes", { dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }

}