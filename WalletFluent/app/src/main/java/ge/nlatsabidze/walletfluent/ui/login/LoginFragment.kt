package ge.nlatsabidze.walletfluent.ui.login

import android.annotation.SuppressLint
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private lateinit var firebaseAuth: FirebaseAuth


    override fun start() {

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener { loginUser() }
        binding.registerTextView.setOnClickListener { navigateToRegisterPage() }
    }

    @SuppressLint("ResourceAsColor")
    private fun loginUser() {
        with(binding) {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val task = firebaseAuth.signInWithEmailAndPassword(email, password)
                task.addOnCompleteListener {
                    if (task.isSuccessful) {

                        val actionLoginFragmentToPersonal =
                            LoginFragmentDirections.actionLoginFragmentToPersonalInfoFragment()
                        findNavController().navigate(actionLoginFragmentToPersonal)
                    } else {

                        val shake: Animation = AnimationUtils.loadAnimation(
                            requireContext(),
                            ge.nlatsabidze.walletfluent.R.anim.vibrate
                        )

                        emailEditText.startAnimation(shake)
                        emailEditText.error = "არაა ბიძია სწორი მაგი"
                        emailEditTextWrapper.boxStrokeColor = R.color.red

                        passwordEditText.startAnimation(shake)
                        passwordEditText.error = "არაა ბიძია სწორი მაგი"

                    }
                }
            }
        }
    }

    private fun navigateToRegisterPage() {
        val actionLoginFragmentToRegister = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(actionLoginFragmentToRegister)
    }
}