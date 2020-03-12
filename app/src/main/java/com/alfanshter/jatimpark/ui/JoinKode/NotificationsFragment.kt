//package com.alfanshter.jatimpark.ui.JoinKode
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProviders
//import com.alfanshter.jatimpark.Model.ModelBaru
//import com.alfanshter.jatimpark.R
//import com.alfanshter.jatimpark.Session.SessionManager
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.*
//import kotlinx.android.synthetic.main.fragment_notifications.*
//import org.jetbrains.anko.*
//
//class NotificationsFragment : Fragment(),AnkoLogger {
//
//    private lateinit var notificationsViewModel: NotificationsViewModel
//    private lateinit var auth: FirebaseAuth
//    lateinit var user: FirebaseUser
//    lateinit var userID: String
//    lateinit var referencesharing: DatabaseReference
//    private lateinit var butonkode:Button
//    private lateinit var sessionManager: SessionManager
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
//        referencesharing = FirebaseDatabase.getInstance().reference.child("Selecta").child("sharing")
//
//
//        butonkode = root.find(R.id.kodebuton)
//        sessionManager = SessionManager(context!!.applicationContext)
//        auth = FirebaseAuth.getInstance()
//        user = auth.currentUser!!
//        userID = user.uid
//
//        butonkode.setOnClickListener {
//
//            referencesharing.addValueEventListener(object : ValueEventListener {
//                override fun onCancelled(p0: DatabaseError) {
//
//                }
//                override fun onDataChange(p0: DataSnapshot) {
////                reference.child("Selecta/Users").child(userID)
//                    p0.children.forEach {
//                        info("informasi : ${it.key}")
//                        if (it.key.toString().equals(pinview.value.toString())) {
//                            referencesharing.child(it.key.toString()).child(userID)
//                                .setValue(ModelBaru("", 0.00, sessionManager.getlongitude()!!.toDouble()))
//                            sessionManager.setKunci(pinview.value.toString())
//                        }
//                    }
//                }
//            })
//
//
//
//        }
//
//
//
//
//
//        /*  btn_invitefragment.setOnClickListener {
//            kodefragment.text= code
//            user = auth.currentUser!!
//            userID = user.uid
//            reference.child(userID).child("key").setValue(code)
//            reference.child(userID).child("date").setValue(date)
//            referencebaru.child("sharing").child(code!!).child(userID).setValue(ModelBaru(sessionManager.getlongitude().toString(),"",""))
//
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful){
//                        toast("berhasil")
//                    }
//
//                    else
//                    {
//                        toast("gagal")
//                        info("Gagal : ${task.exception?.message}")
//                    }
//                }
//
//        }*/
//
//        return root
//    }
//}
//
