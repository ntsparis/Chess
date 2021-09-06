package gr.paris.chess.mvp.presenter.base

import gr.paris.chess.mvp.view.base.BaseView

interface BasePresenter<out V : BaseView> {

    fun detach()
    fun getView(): V?
    fun isViewAttached(): Boolean
}