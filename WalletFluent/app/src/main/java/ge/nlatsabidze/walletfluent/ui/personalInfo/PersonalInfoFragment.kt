package ge.nlatsabidze.walletfluent.ui.personalInfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.PersonalInfoFragmentBinding
import okhttp3.internal.trimSubstring
import okio.utf8Size

@AndroidEntryPoint
class PersonalInfoFragment :
    BaseFragment<PersonalInfoFragmentBinding>(PersonalInfoFragmentBinding::inflate) {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser
    override fun start() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        setData()
        binding.btnIncrease.setOnClickListener{
            increaseMoney()
        }


    }
    private fun setData(){
        database =
            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(firebaseUser.uid)

        database.get().addOnCompleteListener {
            if (it.result?.exists() == true) {
                val balance = it.result!!.child("balance").value.toString().toInt()
                d("sssssss", balance.toString())
                binding.balance.setText(balance.toString() +"₾")
                binding.tvName.setText(it.result!!.child("name").value.toString() + "'s" + " card")
            }
        }
    }

   /* private fun increaseMoney(){
        var size = binding.balance.text.toString().length
        var doubleBalance = binding.balance.text.toString().dropLast(size-1).dropLast(size-2).toInt()
        doubleBalance=+20
        binding.balance.setText(doubleBalance.toString())


    }*/
}