package dev.sirateek.memoize.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

fun GetFirebaseStorageForUser(): StorageReference {
    val storage = Firebase.storage("gs://memoize-develop-3fb42.appspot.com")
    val user = Firebase.auth.currentUser
    return storage.reference.child(user!!.uid)
}