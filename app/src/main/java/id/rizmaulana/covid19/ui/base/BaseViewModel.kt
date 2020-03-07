package id.rizmaulana.covid19.ui.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * rizmaulana 2020-02-24.
 */
abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    protected val TAG = javaClass.simpleName
    protected val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }


    override fun onCleared() {
        with(compositeDisposable) {
            clear()
            dispose()
        }
        super.onCleared()
    }
}