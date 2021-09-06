package gr.paris.chess.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log

data class Tile(
    val x: Int,
    val y: Int,
    var depth: Int = Integer.MAX_VALUE,
    var isSelected: Boolean = false,
    var isVisited: Boolean = false
) {

    private val squareColor: Paint = Paint()
    private var tileRect: Rect? = null
    fun draw(canvas: Canvas, lightTileColor: Int, darkTileColor: Int) {
        squareColor.color = if (isSelected) Color.RED else {
            if (isDark) darkTileColor else lightTileColor
        }
        tileRect?.let { canvas.drawRect(it, squareColor) }
    }

    // To get the actual col, add 1 since 'row' is 0 indexed.
    private val columnString: String
        get() =// To get the actual col, add 1 since 'col' is 0 indexed.
            (x + 1).toString()

    private val rowString: String
        get() =
            (y + 1).toString()

    fun handleTouch() {
        Log.i(TAG, toString())
    }

    private val isDark: Boolean
        get() = (x + y) % 2 == 0

    fun isTouched(x: Int, y: Int): Boolean {
        return tileRect?.contains(x, y) ?: false
    }

    fun setTileRect(tileRect: Rect?) {
        this.tileRect = tileRect
    }

    override fun toString(): String {
        val column = columnString
        val row = rowString
        return "<Tile $column,$row>"
    }

    fun isEqual(other: Tile): Boolean {
        return this.x == other.x && this.y == other.y
    }

    companion object {
        private val TAG = Tile::class.java.simpleName
    }

}
