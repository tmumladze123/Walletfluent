//package ge.nlatsabidze.walletfluent.util.workmanager
//
//import android.content.Context
//import androidx.work.Worker
//import androidx.work.WorkerParameters
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//import java.util.concurrent.CountDownLatch
//import javax.inject.Inject
//
//class FirebaseWork @Inject constructor(
//    context: Context,
//    params: WorkerParameters
//) : Worker(context, params) {
//    override fun doWork(): Result {
//        val latch = CountDownLatch(1)
//
//        var firebaseUser = FirebaseAuth.getInstance().currentUser!!
//
//        var database =
//            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
//                .getReference("Users").child(firebaseUser.uid)
//
//        database.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val currentBalance = dataSnapshot.child("balance").value.toString()
//                var convertedBalance = currentBalance.toLong()
//                convertedBalance += 1000
//                val updatedBalance = HashMap<String, Any>()
//                updatedBalance.put("balance", convertedBalance)
//                database.updateChildren(updatedBalance)
//                latch.countDown()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
//
//        latch.await()
//        return Result.success()
//
//    }
//}