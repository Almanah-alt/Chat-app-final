package com.example.simplechatapp.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.simplechatapp.R
import com.example.simplechatapp.communcators.UserImageCommunicator
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_image_choose_view.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.io.IOException

class BottomSheetFragment : BottomSheetDialogFragment(){


   lateinit var com: UserImageCommunicator

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
//                val selectedImage: Uri? = data.data
                val bitmap = data.extras?.get("data") as Bitmap
                    com.userImgUri(bitmap)
                    dismiss()
            } else{
                Log.d("tttttttt", "$resultCode ${data?.data} $requestCode")
                Toast.makeText(activity?.baseContext, "Что то пошло не так, попробуйте позже", Toast.LENGTH_SHORT).show()
            }
            ProfileFragment.REQUEST_CODE_GALLERY -> if (resultCode == Activity.RESULT_OK
                && data != null && data.data != null) {
                val selectedImage: Uri? = data.data
                val bitmap =
                    MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImage)
                    com.userImgUri(bitmap)
                    dismiss()
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        com = targetFragment as UserImageCommunicator
    }



}