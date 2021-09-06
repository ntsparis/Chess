package gr.paris.chess.common.helper

import gr.paris.chess.model.Tile
import java.util.*

object ChessHelper {

    // All possible positions for a knight
    private val rowMoves = intArrayOf(2, 1, -1, -2, -2, -1, 1, 2)
    private val columnMoves = intArrayOf(1, 2, 2, 1, -1, -2, -2, -1)

    fun findPath(
        start: Tile,
        end: Tile,
        chessboard: Array<Array<Tile?>>
    ): ArrayList<Tile> {
        val tilePath = mutableListOf<Tile>()
        val knightFinalPath: ArrayList<Tile> = arrayListOf()

        var currentPath = chessboard[end.x][end.y]
        while (currentPath?.isEqual(start) == false) {
            tilePath.add(currentPath)
            currentPath = chessboard[currentPath.x][currentPath.y]
        }
        knightFinalPath.add(Tile(end.x, end.y))

        tilePath.add(Tile(start.x, start.y, 0))
        tilePath.forEach {
            knightFinalPath.add(Tile(it.x, it.y))
        }
        return knightFinalPath
    }

    fun calculateMoves(
        queue: Queue<Tile>,
        current: Tile,
        depth: Int,
        chessboard: Array<Array<Tile?>>,
        boardSize: Int
    ): Queue<Tile> {

        // check for all 8 possible movements for a knight
        // and enqueue each valid movement into the queue
        for (i in rowMoves.indices) {
            // New valid position from current
            val x = current.x + rowMoves[i]
            val y = current.y + columnMoves[i]

            if (inRange(x, y, boardSize) && !isNotVisited(x, y, chessboard)) {
                chessboard[x][y] = Tile(current.x, current.y, depth, isVisited = true)
                queue.add(Tile(x, y, depth))
            }
        }
        return queue
    }

    //check if tile is visited
    private fun isNotVisited(x: Int, y: Int, chessboard: Array<Array<Tile?>>): Boolean {
        val tile = chessboard[x][y]
        return tile?.isVisited ?: false
    }

    // Check if is valid coordinates
    private fun inRange(x: Int, y: Int, boardSize: Int): Boolean {
        return x in 0 until boardSize - 1 && 0 <= y && y < boardSize
    }
}