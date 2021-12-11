package ge.nlatsabidze.walletfluent.ui.login

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.RED
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private lateinit var firebaseAuth: FirebaseAuth
    private val loginFragmentViewModel: LoginViewModel by viewModels()

    override fun start() {
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnSignin.setOnClickListener { loginUser() }
        binding.tvRegister.setOnClickListener { navigateToRegisterPage() }
    }

    @SuppressLint("ResourceAsColor")
    private fun loginUser() {

        with(binding) {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            checkInputValidation(email, password)
            navigateToPersonalPage(email, password)
        }
    }

    private fun navigateToRegisterPage() {
        val actionLoginFragmentToRegister =
            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(actionLoginFragmentToRegister)
    }

    private fun showDialogError() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("ვწუხვართ, მითითებული სახელი ან პაროლი არასწორია, სცადე განმეორებით")
        builder.setPositiveButton("yes", { dialogInterface: DialogInterface, i: Int -> })
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
                passwordEditTextWrapper.helperText ="ველი არ არის შევსებული"
                emailEditTextWrapper.helperText = ""
                passwordEditText.setBackgroundResource(R.drawable.border)
                emailEditText.setBackgroundResource(R.color.transparent)
            } else if (email.isEmpty()) {
                emailEditTextWrapper.startAnimation(shake)
                emailEditTextWrapper.helperText = "ველი არ არის შევსებული"
                passwordEditTextWrapper.helperText = ""
                emailEditText.setBackgroundResource(R.drawable.border)
                passwordEditText.setBackgroundResource(R.color.transparent)
            }
            else {
                passwordEditTextWrapper.helperText = ""
                emailEditTextWrapper.helperText = ""
                emailEditText.setBackgroundResource(R.color.transparent)
                passwordEditText.setBackgroundResource(R.color.transparent)
                passwordEditText.setBackgroundResource(R.color.transparent)
            }
        }
    }

    private fun navigateToPersonalPage(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            val task = firebaseAuth.signInWithEmailAndPassword(email, password)
            task.addOnCompleteListener {
                if (task.isSuccessful) {
                    val actionLoginFragmentToPersonal =
                        LoginFragmentDirections.actionLoginFragmentToPersonalInfoFragment()
                    findNavController().navigate(actionLoginFragmentToPersonal)
                } else {
                    showDialogError()
                }
            }
        }
    }


}