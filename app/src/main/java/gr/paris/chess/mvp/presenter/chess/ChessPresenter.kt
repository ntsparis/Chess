package gr.paris.chess.mvp.presenter.chess

import gr.paris.chess.model.Tile

interface ChessPresenter {

    fun onTileClicked(tile: Tile)
    fun resetChessBoard()
    fun initBoard()
    fun setParameters(maxMoves: String, chessDimension: String)
}