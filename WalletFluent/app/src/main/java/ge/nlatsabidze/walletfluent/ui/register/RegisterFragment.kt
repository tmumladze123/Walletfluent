package ge.nlatsabidze.walletfluent.ui.register

import androidx.navigation.fragment.findNavController
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.databinding.FragmentRegisterBinding
import ge.nlatsabidze.walletfluent.ui.login.LoginFragmentDirections


class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    override fun start() {
        binding.tvSignin.setOnClickListener { navigateToSigninPage() }
    }

    fun navigateToSigninPage() {
        val actionRegisterFragment =
            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(actionRegisterFragment)
    }
}