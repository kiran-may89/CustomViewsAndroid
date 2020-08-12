package com.methods.customviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TestViewModel :ViewModel(){
    private val firstLiveData:MutableLiveData<String> = MutableLiveData();
}