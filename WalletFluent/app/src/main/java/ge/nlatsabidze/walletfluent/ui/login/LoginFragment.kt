package ge.nlatsabidze.walletfluent.ui.login

import android.annotation.SuppressLint
import android.content.DialogInterface
<<<<<<< Updated upstream
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.RED
=======
>>>>>>> Stashed changes
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private lateinit var firebaseAuth: FirebaseAuth
<<<<<<< Updated upstream
    private val loginFragmentViewModel: LoginViewModel by viewModels()
=======
>>>>>>> Stashed changes

    override fun start() {
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnSignin.setOnClickListener {
            loginUser() }
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
                emailEditText.startAnimation(shake)
                emailEditText.error = "ველი არ არის შევსებული"

                passwordEditText.startAnimation(shake)
                passwordEditText.error = "ველი არ არის შევსებული"

            } else if (password.isEmpty()) {
                passwordEditText.startAnimation(shake)
                passwordEditText.error = "ველი არ არის შევსებული"
            } else if (email.isEmpty()) {
                emailEditText.startAnimation(shake)
                emailEditText.error = "ველი არ არის შევსებული"

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