package com.example.simplechatapp.observer

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.simplechatapp.FirebaseConstant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ForegroundBackgroundListener : LifecycleObserver {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy {FirebaseAuth.getInstance()}

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onlineUser(){
        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(auth.currentUser!!.uid)
            .update("status", true)
            .addOnSuccessListener { Log.d("onnnnnnnn", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("rrrrrrrrr", "Error updating document", e) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun offlineUser(){
        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(auth.currentUser!!.uid)
            .update("status", false)
            .addOnSuccessListener { Log.d("offfffff", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("rrrrrr", "Error updating document", e) }
    }
}