package com.example.simplechatapp.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.communcators.UserDataCommunicator
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.bottom_image_choose_view.view.*
import java.io.ByteArrayOutputStream

@Suppress("IMPLICIT_CAST_TO_ANY")
class BottomSheetFragment : BottomSheetDialogFragment(){

    private val db by lazy{ FirebaseFirestore.getInstance()}
    private val auth by lazy{ FirebaseAuth.getInstance()}
    private val storage by lazy{ FirebaseStorage.getInstance()}

    lateinit var com: UserDataCommunicator
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = storage.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.bottom_image_choose_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.take_photo.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, ProfileFragment.REQUEST_CODE_CAMERA)
        }

        view.choose_photo.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                ProfileFragment.REQUEST_CODE_GALLERY
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ProfileFragment.REQUEST_CODE_CAMERA -> if (resultCode == Activity.RESULT_OK
                && data != null) {
               val bitmap = data.extras?.get("data") as Bitmap
                val path = MediaStore.Images.Media.insertImage(context?.contentResolver, bitmap, "Profile", null)
                filePath = Uri.parse(path.toString())
                com.userImgUri(bitmap)
                uploadImgStorage()
                dismiss()
            } else{
                Log.d("tttttttt", "$resultCode ${data?.data} $requestCode")
                Toast.makeText(activity?.baseContext, "Что то пошло не так, попробуйте позже", Toast.LENGTH_SHORT).show()
            }
            ProfileFragment.REQUEST_CODE_GALLERY -> if (resultCode == Activity.RESULT_OK
                && data != null && data.data != null) {
                filePath = data.data
               val bitmap =
                    MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
                    com.userImgUri(bitmap)
                    uploadImgStorage()
                    dismiss()
            }
        }
    }


    private fun uploadImgStorage() = if (filePath != null) {
        val ref = storageReference?.child("uploads/" + auth.currentUser!!.uid)
        val uploadTask = ref?.putFile(filePath!!)

        val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        })?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                addUploadRecordToDb(downloadUri.toString())
            } else {
                Log.d("ERRORTAG", task.toString())
            }
        }?.addOnFailureListener{
            Log.d("ERRORTAG", it.toString())
        }
    }else Log.d("ERRORTAG", "bitmap is null")



    private fun addUploadRecordToDb(img: String){
        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(auth.currentUser!!.uid)
            .update("userImg", img)
            .addOnSuccessListener {
               Log.d("SUCCESSTAG", "Successfully uploaded to db")
            }
            .addOnFailureListener {
                Log.d("SUCCESSTAG", "Not Successfully uploaded to db")
            }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        com = targetFragment as UserDataCommunicator
    }



}