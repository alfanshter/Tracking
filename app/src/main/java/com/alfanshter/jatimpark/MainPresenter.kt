package com.alfanshter.jatimpark

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class MainPresenter {

    fun changeFragment(manager: FragmentManager, fragment: Fragment, id:Int){
        var transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(id,fragment).commit()
    }
}