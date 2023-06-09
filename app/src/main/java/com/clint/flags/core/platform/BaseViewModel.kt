package com.clint.flags.core.platform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clint.flags.core.exception.Failure

abstract class BaseViewModel : ViewModel() {

    private val _failure: MutableLiveData<Failure> = MutableLiveData()
    val failure = _failure

    protected fun handleFailure(failure: Failure) {
        _failure.value = failure
    }
}