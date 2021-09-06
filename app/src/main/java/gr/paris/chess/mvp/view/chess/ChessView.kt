package gr.paris.chess.mvp.view.chess

import gr.paris.chess.model.Tile
import gr.paris.chess.mvp.view.base.BaseView

interface ChessView : BaseView{
    fun showInvalidSteps(count: Int)
    fun drawTile(tile: Tile)
    fun resetChessBoard()
    fun showLoading(showLoadingView: Boolean)
    fun showErrorMessage(errorMessage: Int, count: Int? = null)
    fun updateBoardDimension(boardDimension: Int)
    fun setMaxMoves(maxMoves: String)
    fun setChessDimension(chessDimension: String)
}