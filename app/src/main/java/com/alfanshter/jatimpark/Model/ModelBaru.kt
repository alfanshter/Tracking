package com.alfanshter.jatimpark.Model

data class ModelBaru(
    val email:String,
    val latidude: String?,
    val longitude:String

) {
    constructor() : this("", "", "")
}