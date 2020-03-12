package com.alfanshter.jatimpark.ui.shareRombongan.listuser.Model

class Notifications {
    var from: String? = null
    var message: String? = null

    constructor() {}

    constructor(from: String?, message: String?) {
        this.from = from
        this.message = message
    }
}
