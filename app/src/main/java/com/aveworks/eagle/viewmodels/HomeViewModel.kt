package com.aveworks.eagle.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject

class HomeViewModel : ViewModel() {
    private val disposables = CompositeDisposable()
    internal val xPubSubject = BehaviorSubject.create<String>()

    val xPubValidLiveData = MutableLiveData<Boolean>()

    init {
        xPubSubject
            .map(::validate)
            .distinctUntilChanged() // propagate only when validation changes
            .subscribe(xPubValidLiveData::postValue)
            .addTo(disposables)
    }

    fun xPubChanged(x: String) = xPubSubject.onNext(x)

    // TODO xPub validation
    private fun validate(xPub: String): Boolean = xPub.length > 1

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}