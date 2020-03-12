package com.alfanshter.jatimpark.ui.shareRombongan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareRombonganViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Share Rombongan"
    }
    val text: LiveData<String> = _text

}
