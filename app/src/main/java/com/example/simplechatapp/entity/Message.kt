package com.example.simplechatapp.entity

import com.google.firebase.Timestamp

data class Message (
    val chatid: String,
    val senderId: String,
    val text: String,
    val timestamp: com.google.firebase.Timestamp
){
    constructor():this("","","", Timestamp.now())
}
