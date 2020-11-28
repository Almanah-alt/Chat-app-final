package com.example.simplechatapp.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.simplechatapp.R
import com.example.simplechatapp.communcators.UserImageCommunicator
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment(R.layout.fragment_profile), UserImageCommunicator{

    companion object{
        const val REQUEST_CODE_CAMERA = 0
        const val REQUEST_CODE_GALLERY = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.toolbar.title = getString(R.string.profile_toolbar_title)

        view.set_profile_img.setOnClickListener {
            val bottomSheetDialog = BottomSheetFragment()
            bottomSheetDialog.setTargetFragment(this, 1)
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "BottomSheet")
        }


    }

    override fun userImgUri(bitmap: Bitmap) {
        view?.profile_img?.setImageBitmap(bitmap)
        view?.photo_label?.visibility = View.INVISIBLE
    }

}