package com.example.mancingku.model

import java.io.Serializable

data class modelToko (
    val id: String="",
    val alamat: String = "",
    val namatoko: String = "",
    val deskripsitoko: String = "",
    val linktoko: String = "",
    var imgURL: String = "" // Properti untuk menyimpan URL gambar
): Serializable