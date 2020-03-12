package com.alfanshter.jatimpark.ui.shareRombongan.listuser.Model

class Users : UsersId {
    var nama: String? = null
    var image: String? = null

    constructor() {}

    constructor(name: String?, image: String?) {
        this.nama = name
        this.image = image
    }
}
