package ge.nlatsabidze.walletfluent.ui.personalInfo

import android.util.Log.d
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.databinding.PersonalInfoFragmentBinding

@AndroidEntryPoint
class PersonalInfoFragment :
    BaseFragment<PersonalInfoFragmentBinding>(PersonalInfoFragmentBinding::inflate) {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser

    override fun start() {

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!

        setInformationFromDatabase()
        binding.btnIncrease.setOnClickListener {
            changeAmount(binding.btnIncrease.text.toString())
        }

        binding.btnDecrease.setOnClickListener {
            changeAmount(binding.btnDecrease.text.toString())
        }

    }

    private fun setInformationFromDatabase() {
        database =
            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(firebaseUser.uid)

        database.get().addOnCompleteListener {
            if (it.result?.exists() == true) {
                val balance = it.result!!.child("balance").value.toString().toInt()
                d("sssssss", balance.toString())
                binding.balance.setText(balance.toString() + "₾")
                binding.tvName.setText(it.result!!.child("name").value.toString() + "'s" + " card")
            }
        }
    }

    private fun changeAmount(operator: String) {

        val valueAsString = binding.balance.text.toString()
        var convertedValueToInt = valueAsString.dropLast(1).toInt()

        if (operator == "+") {
            convertedValueToInt += 20
        } else {
            convertedValueToInt -= 20
        }

        binding.balance.setText(convertedValueToInt.toString() + "₾")

        val user = HashMap<String, Any>()
        user.put("balance", convertedValueToInt)
        database.updateChildren(user).addOnCompleteListener {
            if (it.isSuccessful) {
                d("sdadasdas", convertedValueToInt.toString())
            }
        }
    }
}