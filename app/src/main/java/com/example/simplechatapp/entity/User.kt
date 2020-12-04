package com.example.simplechatapp.entity

import android.graphics.Bitmap

data class User (
    val uid: String,
    val email: String,
    val username: String,
    val status: Boolean = false,
    val userImg: String? = null,
    val psw: String
){
    constructor():this("","","", false,  psw = "")
}