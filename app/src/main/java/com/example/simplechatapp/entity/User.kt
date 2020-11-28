package com.example.simplechatapp.entity

data class User (
    val uid: String,
    val email: String,
    val username: String,
    val status: Boolean
){
    constructor():this("","","", false)
}