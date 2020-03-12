package com.alfanshter.jatimpark.Model

class UsersInfo {

    var deskripsi: String? = null
    var gambar: String? = null
    var nama:String? = null


    constructor():this("","","")
    {}

    constructor(deskripsi: String?, gambar: String?, nama: String?) {
        this.deskripsi = deskripsi
        this.gambar = gambar
        this.nama = nama
    }


}