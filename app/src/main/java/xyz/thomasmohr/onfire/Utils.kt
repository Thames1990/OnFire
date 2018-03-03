package xyz.thomasmohr.onfire

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun ViewGroup.inflate(
    layoutRes: Int
): View = LayoutInflater.from(context).inflate(layoutRes, this, false)

operator fun CompositeDisposable.plus(disposable: Disposable): CompositeDisposable {
    add(disposable)
    return this
}