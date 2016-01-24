package pro.anton.averin.networking.testrest.presenters

import android.os.Bundle

public abstract class BasePresenter<B : BaseView> {

    public lateinit var view: B
    protected var visible: Boolean = true

    open public fun onCreate(savedInstanceState: Bundle?) {

    }

    open public fun onStart() {
    }

    open public fun onResume() {
    }

    open public fun onPause() {
    }

    open public fun onSaveInstanceState(outState: Bundle) {
    }

    open public fun onStop() {
    }

    open public fun onDestroy() {
    }

    open public fun onVisible() {
        visible = true
    }

    open public fun onHidden() {
        visible = false
    }
}