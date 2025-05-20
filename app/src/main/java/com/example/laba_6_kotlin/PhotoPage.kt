package com.example.laba_6_kotlin

data class PhotoPage(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<Photo>
)
