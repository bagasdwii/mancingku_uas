package com.example.mancingku.model

import java.io.Serializable

data class modelEvent(
    val id : String="",
    val title : String="",
    val alamat: String = "",
    val deskripsievent: String = "",
    val linkevent: String = "",
    var imgURL: String = "" // Properti untuk menyimpan URL gambar
):Serializable