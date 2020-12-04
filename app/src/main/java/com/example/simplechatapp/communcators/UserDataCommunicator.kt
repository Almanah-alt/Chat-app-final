package com.example.simplechatapp.communcators

import android.graphics.Bitmap
import android.net.Uri

interface UserDataCommunicator {
    fun userImgUri(bitmap: Bitmap)
    fun userUsername(username: String)
}