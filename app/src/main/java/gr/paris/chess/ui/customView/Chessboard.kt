package gr.paris.chess.ui.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import gr.paris.chess.R
import gr.paris.chess.common.extensions.getBitmapFromVectorDrawable
import gr.paris.chess.model.Tile

class Chessboard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var knightBitmap: Bitmap? = null
    private var lightTileColor: Int = Color.WHITE
    private var darkTileColor: Int = Color.BLACK
    private var boardSize: Int = DEFAULT_BOARD_DIMENSION
    private var chessBoardListener: ChessBoardListener? = null
    private var mTiles: Array<Array<Tile?>> = Array(boardSize) {
        arrayOfNulls(
            boardSize
        )
    }
    private var x0 = 0
    private var y0 = 0
    private var squareSize = 0

    /** 'true' if black is facing player.  */
    private val flipped = true

    private fun init(context: Context, attrs: AttributeSet?) {
        //load arrow as bitmap
        knightBitmap =
            context.getBitmapFromVectorDrawable(R.drawable.ic_knight)
        applyAttributes(attrs)
    }

    @SuppressLint("CustomViewStyleable", "ResourceAsColor")
    private fun applyAttributes(rawAttrs: AttributeSet?) {
        val attrs =
            context.obtainStyledAttributes(
                rawAttrs,
                R.styleable.BoardView,
                0,
                0
            )
        try {
            boardSize = attrs.getInteger(R.styleable.BoardView_dimension, DEFAULT_BOARD_DIMENSION)
            lightTileColor = attrs.getColor(R.styleable.BoardView_lightTileColor, R.color.white)
            darkTileColor = attrs.getColor(R.styleable.BoardView_darkTileColor, R.color.black)
        } finally {
            attrs.recycle()
        }
    }

    private fun buildTiles() {
        for (c in 0 until boardSize) {
            for (r in 0 until boardSize) {
                mTiles[c][r] = Tile(c, r)
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        val width = width
        val height = height
        squareSize = getSquareSizeWidth(width).coerceAtMost(getSquareSizeHeight(height))
        computeOrigins(width, height)

        for (c in 0 until boardSize) {
            for (r in 0 until boardSize) {
                val xCoord = getXCoord(c)
                val yCoord = getYCoord(r)
                val tileRect = Rect(
                    xCoord,  // left
                    yCoord,  // top
                    xCoord + squareSize,  // right
                    yCoord + squareSize // bottom
                )
                mTiles[c][r]?.setTileRect(tileRect)
                canvas?.let { mTiles[c][r]?.draw(it, lightTileColor, darkTileColor) }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        var tile: Tile?
        for (c in 0 until boardSize) {
            for (r in 0 until boardSize) {
                tile = mTiles[c][r]
                if (tile!!.isTouched(x, y)) {
                    tile.handleTouch()
                    chessBoardListener?.onTileClicked(tile)
                    return false
                }
            }
        }
        return true
    }

    private fun getSquareSizeWidth(width: Int): Int {
        return width / boardSize
    }

    private fun getSquareSizeHeight(height: Int): Int {
        return height / boardSize
    }

    private fun getXCoord(x: Int): Int {
        return x0 + squareSize * if (flipped) (boardSize - 1) - x else x
    }

    private fun getYCoord(y: Int): Int {
        return y0 + squareSize * if (flipped) y else (boardSize - 1) - y
    }

    private fun computeOrigins(width: Int, height: Int) {
        x0 = (width - squareSize * boardSize) / 2
        y0 = (height - squareSize * boardSize) / 2
    }

    fun drawTile(tile: Tile) {
        for (c in 0 until boardSize) {
            for (r in 0 until boardSize) {
                mTiles[c][r] = Tile(c, r, isSelected = c == tile.x && r == tile.y)
            }
        }
        this.invalidate()
    }

    fun reset() {
        mTiles = Array(boardSize) {
            arrayOfNulls(
                boardSize
            )
        }
        for (c in 0 until boardSize) {
            for (r in 0 until boardSize) {
                mTiles[c][r] = Tile(c, r, isSelected = false)
            }
        }
        this.invalidate()
    }

    interface ChessBoardListener {
        fun onTileClicked(tile: Tile)
    }

    fun setChessBoardListener(chessBoardListener: ChessBoardListener?) {
        this.chessBoardListener = chessBoardListener
    }

    fun updateBoardDimension(boardDimension: Int) {
        boardSize = boardDimension
    }

    companion object {
        private val TAG = Chessboard::class.java.simpleName
        private const val DEFAULT_BOARD_DIMENSION = 6
    }

    init {
        init(context, attrs)
        isFocusable = true
        buildTiles()
    }
}