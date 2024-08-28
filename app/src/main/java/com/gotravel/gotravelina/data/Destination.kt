package com.gotravel.gotravelina.data

data class Destination(
    val id: Int,
    val name: String,
    val location: String,
    val image: Int,
    val description: String,
    val rating: Double,
    var isFavorite: Boolean = false
)