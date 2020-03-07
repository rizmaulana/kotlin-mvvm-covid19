package id.rizmaulana.covid19.util.ext

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * rizmaulana 2020-02-24.
 */
fun Disposable.addTo(compositeDisposable: CompositeDisposable){
    compositeDisposable.add(this)
}