package com.example.ensihub.mainClasses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val imageUrl = MutableLiveData<String>()
}