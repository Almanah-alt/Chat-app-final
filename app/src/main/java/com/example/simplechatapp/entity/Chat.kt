package com.example.simplechatapp.entity

import com.google.firebase.Timestamp

data class Chat(
    val id:String,
    val participants: List<User>,
    val participantId: List<String>,
    val last_msgTimestamp: Timestamp,
    val last_msg:String
){
    constructor():this("", emptyList(),  emptyList(), Timestamp.now(),"")

}