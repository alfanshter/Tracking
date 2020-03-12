package com.alfanshter.jatimpark.Rombongan

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.alfanshter.jatimpark.Model.ModelBaru
import com.alfanshter.jatimpark.Model.ModelNamaProfil
import com.alfanshter.jatimpark.R
import com.alfanshter.jatimpark.Session.SessionManager
import com.alfanshter.jatimpark.Tracking_Rombongan
import com.alfanshter.jatimpark.cekkode
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_nama_profil.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


@Suppress("UNREACHABLE_CODE")
class NamaProfil : AppCompatActivity() {

     var storageReference : StorageReference? = null
     var resultUri:Uri? = null
     var reference: DatabaseReference? = null
     var user: FirebaseUser? = null
     var userID:String? = null
    var auth: FirebaseAuth? = null
    private var myUrl = ""

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nama_profil)
        sessionManager = SessionManager(this)

        if (sessionManager.getNama()!!)
        {
            startActivity<Tracking_Rombongan>()
            finish()
        }

        auth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference.child("Selecta/Users")

        storageReference = FirebaseStorage.getInstance().reference.child("gambaruser")
        val nama = edt_nama.text.toString()
        fotoprofil.setOnClickListener {
            selectimage()
        }

        btn_nextfoto.setOnClickListener {
            uploadgambar()
            generateCode()
        }
    }

    private fun uploadgambar()
    {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("uploading....")
        progressDialog.show()

        when{
            resultUri == null -> toast("ambil gambar telebih dahulu ")
            else ->
            {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Akun Setting")
                progressDialog.setMessage("Tunggu , sedang update")
                progressDialog.show()
                val fileref = storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")
                var uploadTask : StorageTask<*>
                uploadTask = fileref.putFile(resultUri!!)

                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot,Task<Uri>>{task ->
                    if (!task.isSuccessful){
                        task.exception?.let {
                            throw  it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileref.downloadUrl
                }).addOnCompleteListener(OnCompleteListener<Uri> {task ->
                    if (task.isSuccessful)
                    {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val ref = FirebaseDatabase.getInstance().reference.child("Selecta").child("Users").child(userID.toString())
//                            val postId = ref.push().key
                        val postMap = HashMap<String,Any>()
                        //                          postMap["postid"] = postId!!

                        ref.child("gambar").setValue(myUrl)
                        toast("upload sukses")
                        progressDialog.dismiss()
                    }
                    else
                    {
                        progressDialog.dismiss()
                    }
                })

            }
        }
    }



    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode ==12 && resultCode == Activity.RESULT_OK && data!=null ){
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this)
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
           val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK){
                 resultUri = result.uri
                fotoprofil.setImageURI(resultUri)

            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val error : Exception = result.error
            }
        }
    }

    private fun selectimage() {
        val i = Intent()
        i.action = Intent.ACTION_GET_CONTENT
        i.type = "image/*"
        startActivityForResult(i,12)

    }

    private fun generateCode() {
        user = auth?.currentUser!!
        userID = user!!.uid

        val myDate = Date()
        val format1 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())
        val date :String = format1.format(myDate)
        val random = Random()
        val n:Int = 100000 + random.nextInt(900000)
        val code = n.toString()
        val parcel = ParcelObjectName(resultUri.toString(),"")
        reference!!.child(userID!!).child("nama").setValue(edt_nama.text.toString())
        reference!!.child(userID!!).child("date").setValue(date)
            .addOnCompleteListener {
                if (it.isSuccessful)
                {
                    toast("identitas berhasil di simpan")
                    sessionManager.setNama(true)
                    sessionManager.setprofil(edt_nama.text.toString())
                }
                else
                {
                    toast("identitas gagal disimpan")
                }
            }

        startActivity<Tracking_Rombongan>()
        toast(resultUri.toString())




    }
}
