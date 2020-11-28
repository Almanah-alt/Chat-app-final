package com.example.simplechatapp.communcators

import android.graphics.Bitmap
import android.net.Uri

interface UserImageCommunicator {
    fun userImgUri(bitmap: Bitmap)
}