package com.aveworks.eagle.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject

class HomeViewModel : ViewModel() {
    private val disposables = CompositeDisposable()
    private val xPubSubject = BehaviorSubject.createDefault("")
    private val xPubValidationLiveData = MutableLiveData(false)

    init {
        xPubSubject
            .map(::validate)
            .distinctUntilChanged() // propagate only when validation changes
            .subscribe(xPubValidationLiveData::postValue)
            .addTo(disposables)
    }

    fun getXPub() = xPubSubject.value ?: ""
    fun setXPub(x: String) = xPubSubject.onNext(x)

    fun getXPubValidation() = xPubValidationLiveData

    // TODO xPub validation
    private fun validate(xPub: String): Boolean = xPub.length >= 3

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}