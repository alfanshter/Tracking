package com.alfanshter.jatimpark.Model

data class ModelUsers(
    val latitude: String,
    val longitude: String,
    val email: String,
    val iduser: String,
    val password: String,
    val key: String,
    val gambar: String,
    val date: String,
    val name: String

) {
    constructor() : this("", "", "", "", "", "", "", "", "")
}
