package com.alfanshter.jatimpark.Model

import com.alfanshter.jatimpark.Utils.Contants

//modelnya register
class userModel(val name :String, val email : String, val uid : String, val photourl:String) {
    constructor() : this("undefined","undefined","undefined",Contants.PHOTO)
}
