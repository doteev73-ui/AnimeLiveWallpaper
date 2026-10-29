package com.example.animelivewallpaper

import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import kotlin.math.sin

class AnimeWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return AnimeEngine()
    }

    inner class AnimeEngine : Engine() {

        private val handler = Handler(Looper.getMainLooper())
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private var running = false
        private var frame = 0

        private val drawRunnable = object : Runnable {
            override fun run() {
                drawFrame()
                if (running) {
                    handler.postDelayed(this, 50)
                }
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            running = visible
            handler.removeCallbacks(drawRunnable)
            if (visible) handler.post(drawRunnable)
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
            drawFrame()
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            running = false
            handler.removeCallbacks(drawRunnable)
            super.onSurfaceDestroyed(holder)
        }

        private fun drawFrame() {
            val holder = surfaceHolder
            var canvas: Canvas? = null

            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    canvas.drawColor(Color.rgb(12, 8, 35))

                    paint.color = Color.rgb(90, 30, 180)
                    paint.style = Paint.Style.FILL

                    val centerX = canvas.width / 2f
                    val centerY = canvas.height / 2f
                    val offset = (sin(frame * 0.08) * 25).toFloat()

                    canvas.drawCircle(
                        centerX + offset,
                        centerY,
                        canvas.width * 0.25f,
                        paint
                    )

                    paint.color = Color.CYAN
                    paint.textSize = 48f
                    paint.textAlign = Paint.Align.CENTER

                    canvas.drawText(
                        "ANIME LIVE",
                        centerX,
                        centerY + canvas.height * 0.32f,
                        paint
                    )

                    frame++
                }
            } catch (_: Exception) {
                // Surface may temporarily be unavailable.
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }
}
