package com.alfanshter.jatimpark.Model

import com.google.firebase.database.Exclude


data class ModelSharing(
    val name:String,
    val latidude:Double,
    val longitude:Double

) {
    constructor() : this("", 0.00, 0.00)
}