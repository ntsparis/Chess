package gr.paris.chess.ui.activity.chess

import android.os.Bundle
import android.view.View
import android.widget.Toast
import gr.paris.chess.R
import gr.paris.chess.common.extensions.closeSoftKeyboard
import gr.paris.chess.model.Tile
import gr.paris.chess.mvp.presenter.chess.ChessPresenterImpl
import gr.paris.chess.mvp.view.chess.ChessView
import gr.paris.chess.ui.activity.base.BaseMvpActivity
import gr.paris.chess.ui.customView.Chessboard
import kotlinx.android.synthetic.main.activity_chess.*

class ChessActivity : BaseMvpActivity<ChessPresenterImpl>(), ChessView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chess)
        presenter = ChessPresenterImpl(this)
        presenter?.initBoard()
        initLayout()
    }

    private fun initLayout() {
        applyButton?.setOnClickListener {
            presenter?.setParameters(
                maxMovesEditText.text.toString(),
                chessDimensionEditText.text.toString()
            )
            closeSoftKeyboard(this)
        }
        resetButton?.setOnClickListener {
            presenter?.resetChessBoard()
        }
        chessBoard?.setChessBoardListener(object : Chessboard.ChessBoardListener {
            override fun onTileClicked(tile: Tile) {
                presenter?.onTileClicked(tile)
            }
        })
    }

    override fun setMaxMoves(maxMoves: String) {
        maxMovesEditText?.setText(maxMoves)
    }

    override fun setChessDimension(chessDimension: String) {
        chessDimensionEditText?.setText(chessDimension)
    }

    override fun updateBoardDimension(boardDimension: Int) {
        chessBoard?.updateBoardDimension(boardDimension)
    }

    override fun drawTile(tile: Tile) {
        chessBoard?.drawTile(tile)
    }

    override fun resetChessBoard() {
        chessBoard?.reset()
    }

    override fun showInvalidSteps(count: Int) {
        Toast.makeText(this, getString(R.string.invalid_steps, count), Toast.LENGTH_SHORT).show()
    }

    override fun showLoading(showLoadingView: Boolean) {
        loadingView?.visibility = if (showLoadingView) View.VISIBLE else View.GONE
    }

    override fun showErrorMessage(errorMessage: Int, count: Int?) {
        Toast.makeText(this, getString(errorMessage, count), Toast.LENGTH_SHORT).show()
    }
}
