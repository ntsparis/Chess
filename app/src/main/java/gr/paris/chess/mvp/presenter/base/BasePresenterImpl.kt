package gr.paris.chess.mvp.presenter.base

import gr.paris.chess.mvp.view.base.BaseView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import java.lang.ref.WeakReference

open class BasePresenterImpl<out V : BaseView>(view: V) : BasePresenter<V> {

    private var viewRef: WeakReference<V>? = null

    private var job = Job()
    protected val uiScope = CoroutineScope(Dispatchers.Main + job)

    init {
        viewRef = WeakReference(view)
    }

    override fun getView(): V? {
        return viewRef?.get()
    }

    override fun isViewAttached(): Boolean {
        return viewRef != null && viewRef!!.get() != null && viewRef!!.get()!!.isAttached()
    }

    override fun detach() {
        uiScope.coroutineContext.cancelChildren()
        viewRef?.clear()
    }
}