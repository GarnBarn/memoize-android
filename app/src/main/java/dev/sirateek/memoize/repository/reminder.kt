package dev.sirateek.memoize.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun ReminderRepository(): CollectionReference {
    return Firebase.firestore.collection("reminder")
}

fun TagRepository(): CollectionReference {
    return Firebase.firestore.collection("tag")
}

fun UserRepository(): CollectionReference {
    return Firebase.firestore.collection("user")
}