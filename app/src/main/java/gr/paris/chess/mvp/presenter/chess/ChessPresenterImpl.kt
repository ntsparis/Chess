package gr.paris.chess.mvp.presenter.chess

import android.util.Log
import gr.paris.chess.R
import gr.paris.chess.common.helper.ChessHelper
import gr.paris.chess.model.Tile
import gr.paris.chess.mvp.presenter.base.BasePresenterImpl
import gr.paris.chess.mvp.view.chess.ChessView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ChessPresenterImpl(view: ChessView) : BasePresenterImpl<ChessView>(view), ChessPresenter {

    companion object {
        private const val DEFAULT_MAX_MOVES = 3
        private const val DEFAULT_BOARD_DIMENSION = 6
        private const val MAX_BOARD_SIZE = 16
    }

    private var boardSize: Int = DEFAULT_BOARD_DIMENSION
    private var maxMoves: Int = DEFAULT_MAX_MOVES

    private var startTile: Tile? = null
    private var destinationTile: Tile? = null
    private var queue: Queue<Tile> = LinkedList()
    private var isNotReachable: Boolean = true
    private var chessboard = Array(boardSize) { arrayOfNulls<Tile>(boardSize) }

    override fun initBoard() {
        if (!isViewAttached()) {
            return
        }
        getView()?.setMaxMoves(DEFAULT_MAX_MOVES.toString())
        getView()?.setChessDimension(DEFAULT_BOARD_DIMENSION.toString())
        resetBoard()
    }

    override fun setParameters(maxMoves: String, chessDimension: String) {
        if (!isViewAttached() || maxMoves.isEmpty() || chessDimension.isEmpty()) {
            return
        }

        val possibleDimension = chessDimension.toInt()
        if (possibleDimension < DEFAULT_BOARD_DIMENSION || possibleDimension > MAX_BOARD_SIZE) {
            getView()?.showErrorMessage(R.string.not_acceptable_dimensions)
            return
        }

        this.maxMoves = maxMoves.toInt()
        this.boardSize = possibleDimension
        getView()?.updateBoardDimension(boardSize)
        resetChessBoard()
    }

    override fun onTileClicked(tile: Tile) {
        if (!isViewAttached() || tile === startTile)
            return

        if (startTile == null) {
            startTile = tile
            getView()?.drawTile(tile)
            return
        } else if (destinationTile == null) {
            destinationTile = tile
            calculateTilePath()
            return
        }
    }

    private fun calculateTilePath() {
        if (!isViewAttached()) {
            return
        }
        val startTile = Tile(startTile!!.x, startTile!!.y, 0, isVisited = true)
        val endTile = Tile(destinationTile!!.x, destinationTile!!.y)

        chessboard[startTile.x][startTile.y] = startTile
        queue.add(startTile)
        var currentTile: Tile
        uiScope.launch {
            getView()?.showLoading(true)

            // loop till queue is empty
            while (queue.size != 0) {
                currentTile = queue.poll()

                if (endTile.isEqual(currentTile)) {
                    isNotReachable = false
                    Log.d("Steps->", currentTile.depth.toString())
                    if (currentTile.depth > maxMoves) {
                        getView()?.let {
                            clearChess()
                            it.showLoading(false)
                            it.showErrorMessage(R.string.invalid_steps, maxMoves)
                        }
                        return@launch
                    }
                    val knightPath = withContext(Dispatchers.Default) {
                        ChessHelper.findPath(
                            startTile,
                            endTile,
                            chessboard
                        )
                    }
                    if (!isViewAttached()) {
                        return@launch
                    }
                    getView()?.let {
                        it.showLoading(false)
                        showPath(knightPath)
                        return@launch
                    }
                } else {
                    withContext(Dispatchers.Default) {
                        ChessHelper.calculateMoves(
                            queue,
                            currentTile,
                            ++currentTile.depth,
                            chessboard,
                            boardSize
                        )
                    }
                }
            }
            if (isNotReachable) {
                if (!isViewAttached()) {
                    return@launch
                }
                clearChess()
                getView()?.showLoading(false)
                getView()?.showErrorMessage(R.string.not_reachable)
            }
        }
    }

    private fun showPath(knightPath: ArrayList<Tile>) {
        uiScope.launch {
            knightPath.reversed().forEach { tile ->
                getView()?.drawTile(tile)
                delay(500)
            }
        }
    }

    private fun clearChess() {
        startTile = null
        destinationTile = null
        chessboard = Array(boardSize) { arrayOfNulls(boardSize) }
        queue = LinkedList()
        isNotReachable = true
        resetBoard()
        getView()?.resetChessBoard()
    }

    override fun resetChessBoard() {
        if (!isViewAttached()) {
            return
        }
        clearChess()
    }

    private fun resetBoard() {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                chessboard[i][j] = Tile(
                    x = Integer.MAX_VALUE,
                    y = Integer.MAX_VALUE,
                    depth = Integer.MAX_VALUE
                )
            }
        }
    }
}