package com.alfanshter.jatimpark.FirebaseHelper

import com.alfanshter.jatimpark.Model.userModel
import com.alfanshter.jatimpark.Utils.Contants
import com.google.firebase.database.FirebaseDatabase

//register punya
class FirebaseHelper {
    companion object{
        val mFirebaseDatabase = FirebaseDatabase.getInstance()

        fun pushUserData(
            name: String,
            email: String,
            uid: String,
            nomor: String
        ){
            val meref = mFirebaseDatabase.getReference(Contants.USERS)
            val me = userModel(name,email,uid,Contants.PHOTO)
            meref.child(uid).setValue(me)
        }

/*        fun getTimeDate(timeStamp : Long) :String{
            return try {
            val dateFormat = DateFormat.getDateTimeInstance()
                val nedDate = Date(timeStamp)
                dateFormat.format(nedDate)
            } catch (e :Exception){
                "undefined date"
            }
        }*/
    }
}