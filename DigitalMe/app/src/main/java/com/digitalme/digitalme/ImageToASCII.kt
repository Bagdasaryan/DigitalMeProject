package com.digitalme.digitalme

import android.graphics.Bitmap
import android.graphics.Color


class ImageToASCII {
    private val BLACK = "@"
    private val CHARCOAL = "#"
    private val DARKGRAY = "8"
    private val MEDIUMGRAY = "&"
    private val MEDIUM = "o"
    private val GRAY = ":"
    private val SLATEGRAY = "*"
    private val LIGHTGRAY = "."
    private val WHITE = "`"

    fun GrayscaleImageToASCII(bmp: Bitmap): String? {
        val html = StringBuilder()
        return try {
            val asciiArr = Array(258, {Array(258){0} })

            var indexY = 0
            var countOfYIterations = 0
            for (y in 0 until bmp.height) {
                var indexX = 0
                var countOfXIterations = 0
                var color = 0
                for (x in 0 until bmp.width) {
                    val c = bmp.getPixel(x, y)

                    val r: Int = Color.red(c)
                    val g: Int = Color.green(c)
                    val b: Int = Color.blue(c)

                    val rValue = (r + g + b) / 3

                    color += rValue

                    if(countOfXIterations == 4) {
                        asciiArr[indexX][indexY] += color/4
                        color = 0
                        countOfXIterations = 0
                        indexX++
                    }

                    countOfXIterations++
                }

                if(countOfYIterations == 4) {
                    indexY++
                    countOfYIterations = 0
                }
                countOfYIterations++
            }

            for(j in 0 until asciiArr.size) {
                for(i in 0 until asciiArr[j].size) {
                    html.append(getGrayShade(asciiArr[i][j]/4))
                }
                html.append("\n")
            }

            html.toString()
        } catch (exc: Exception) {
            exc.toString()
        } finally {
            bmp.recycle()
        }
    }

    private fun getGrayShade(redValue: Int): String? {
        var asciival: String? = "`"
        if (redValue >= 230) {
            asciival = WHITE
        } else if (redValue >= 200) {
            asciival = LIGHTGRAY
        } else if (redValue >= 180) {
            asciival = SLATEGRAY
        } else if (redValue >= 160) {
            asciival = GRAY
        } else if (redValue >= 130) {
            asciival = MEDIUM
        } else if (redValue >= 100) {
            asciival = MEDIUMGRAY
        } else if (redValue >= 70) {
            asciival = DARKGRAY
        } else if (redValue >= 50) {
            asciival = CHARCOAL
        } else {
            asciival = BLACK
        }
        return asciival
    }
}
