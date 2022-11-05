package com.example.porject

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

data class myPetType(var petName: String = " ", var petType: String = " ", var userUID: String? = " ")