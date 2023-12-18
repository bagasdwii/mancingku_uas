package com.example.mancingku.model

import java.io.Serializable

data class modelPost(
    val key : String="",
    val username : String="",
    val linkpost:String = "",
    val status: String = "",
    var profil : String="",
    var gambar: String = "" // Properti untuk menyimpan URL gambar
)