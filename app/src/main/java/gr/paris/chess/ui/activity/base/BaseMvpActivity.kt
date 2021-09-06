package gr.paris.chess.ui.activity.base

import androidx.appcompat.app.AppCompatActivity
import gr.paris.chess.mvp.presenter.base.BasePresenterImpl
import gr.paris.chess.mvp.view.base.BaseView

open class BaseMvpActivity<T : BasePresenterImpl<*>?> : AppCompatActivity(),
    BaseView {
    protected var presenter: T? = null

    override fun isAttached(): Boolean {
        return !isFinishing
    }
}