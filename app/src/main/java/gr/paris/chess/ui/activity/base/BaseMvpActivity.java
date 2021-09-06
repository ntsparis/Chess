package gr.paris.chess.ui.activity.base;

import androidx.appcompat.app.AppCompatActivity;

import gr.paris.chess.mvp.presenter.base.BasePresenterImpl;
import gr.paris.chess.mvp.view.base.BaseView;

public class BaseMvpActivity<T extends BasePresenterImpl> extends AppCompatActivity implements BaseView {
    private T presenter;

    @Override
    public boolean isAttached() {
        return !isFinishing();
    }

    protected void setPresenter(T presenter) {
        this.presenter = presenter;
    }

    protected T getPresenter() {
        return this.presenter;
    }
}