package ge.nlatsabidze.walletfluent.ui.login

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.FragmentLoginBinding

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val logInViewModel: LoginViewModel by viewModels()

    private lateinit var firebaseAuth: FirebaseAuth
    private val args: LoginFragmentArgs by navArgs()

    override fun start() {
        firebaseAuth = FirebaseAuth.getInstance()
        binding.tvSignUp.setOnClickListener { navigateToRegisterPage() }
        binding.btnSignin.setOnClickListener {
        }
        binding.tvForgotPassword.setOnClickListener { resetPassword() }
        setDataFromRegisterPage()
    }


    @SuppressLint("ResourceAsColor")
    private fun loginUser() {

        with(binding) {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            checkInputValidation(email, password)
            loginUserWithEmailAndPassword(email, password)
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
            } else {
                passwordEditTextWrapper.helperText = ""
                emailEditTextWrapper.helperText = ""
                emailEditText.setBackgroundResource(R.color.transparent)
                passwordEditText.setBackgroundResource(R.color.transparent)
                passwordEditText.setBackgroundResource(R.color.transparent)
            }
        }
    }

    private fun loginUserWithEmailAndPassword(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            val task = firebaseAuth.signInWithEmailAndPassword(email, password)
            task.addOnCompleteListener {
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user!!.isEmailVerified) {
                        navigateToPersonalPage()
                    } else {
                        user.sendEmailVerification()
                        showVerificationDialog()
                    }

                } else {
                    showDialogError("ვწუხვართ, მითითებული სახელი ან პაროლი არასწორია, სცადე განმეორებით")
                }
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
        with (binding) {
            if (emailEditText.text.toString().isNotEmpty()){
                firebaseAuth.sendPasswordResetEmail(binding.emailEditText.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showDialogError("მიყევით ინსტრუქციას მითითებულ მეილზე")
                        }
                    }
            }
        }
    }
}