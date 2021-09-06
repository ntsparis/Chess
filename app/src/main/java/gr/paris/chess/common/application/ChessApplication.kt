package gr.paris.chess.common.application

import android.app.Application

class ChessApplication : Application() {

    companion object {
        private lateinit var instance: ChessApplication

        @JvmStatic
        fun get(): ChessApplication {
            return instance
        }
    }
}