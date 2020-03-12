package com.alfanshter.jatimpark.ui.shareRombongan.listuser

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.alfanshter.jatimpark.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_send.*
import java.util.*

class SendActivity : AppCompatActivity() {
    private val user_id_view: TextView? = null

    private var mUserId: String? = null
    private var mUserName: String? = null
    private var mCurrentId: String? = null

    private val mMessageView: EditText? = null
    private val mSendBtn: Button? = null
    private val mMessageProgress: ProgressBar? = null

    private var mFirestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)
        mFirestore = FirebaseFirestore.getInstance()
        mCurrentId = FirebaseAuth.getInstance().uid

        mUserId = intent.getStringExtra("user_id")
        mUserName = intent.getStringExtra("user_name")

            user_name_view.text ="Send to " + mUserName
        send_btn.setOnClickListener {
            val message = message_view.text.toString()
            if (!TextUtils.isEmpty(message)) {
                messageProgress.visibility = View.VISIBLE
                val notificationMessage: MutableMap<String, Any> =
                    HashMap()
                notificationMessage["message"] = message
                notificationMessage["from"] = mCurrentId!!
                mFirestore!!.collection("Users/$mUserId/Notifications")
                    .add(notificationMessage).addOnSuccessListener {
                        Toast.makeText(this@SendActivity, "Notification Sent.", Toast.LENGTH_LONG)
                            .show()
                        message_view.setText("")
                        messageProgress.visibility = View.INVISIBLE
                    }.addOnFailureListener { e ->
                        Toast.makeText(this@SendActivity, "Error : " + e.message, Toast.LENGTH_LONG)
                            .show()
                        messageProgress.visibility = View.INVISIBLE
                    }
            }
        }
    }
}
