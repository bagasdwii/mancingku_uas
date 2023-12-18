package com.example.mancingku.model

import java.io.Serializable


data class modelSpotMancing(
    val id: String = "",
    val alamat: String = "",
    val namaspot: String = "",
    val deskripsispot: String = "",
    val linkspot: String = "",
    var imgURL: String = "" // Properti untuk menyimpan URL gambar
): Serializable

