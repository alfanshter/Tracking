package com.alfanshter.jatimpark.ui.generate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GenerateCodeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Generate"
    }
    val text: LiveData<String> = _text
}
