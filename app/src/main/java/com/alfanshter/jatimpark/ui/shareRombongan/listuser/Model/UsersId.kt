package com.alfanshter.jatimpark.ui.shareRombongan.listuser.Model

import androidx.annotation.NonNull

open class UsersId {
    var userId: String? = null
    fun <T : UsersId?> withId(@NonNull id: String?): T {
        userId = id
        return this as T
    }
}
