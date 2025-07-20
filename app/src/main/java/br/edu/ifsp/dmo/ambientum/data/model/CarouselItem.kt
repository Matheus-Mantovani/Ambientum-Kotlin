package br.edu.ifsp.dmo.ambientum.data.model

data class CarouselItem(
    val title: String, val imageResId: Int, var desc: String, var quality: Quality, var value: Float? = null
)
