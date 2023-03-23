package com.pico.mymusicplayer.presentation.player.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun ImageBehindSnowfall(
    image: ImageBitmap,
    modifier: Modifier = Modifier,
    flakesSpeedUp: Float = 0.3f,
    flakesCount: Int = 100
) {

    fun randomSnowflake(): Snowflake {
        val random = Random.Default
        val x = random.nextInt(0, 1080).toFloat()
        val y = random.nextInt(0, 1920).toFloat()
        val radius = random.nextInt(1, 3).toFloat()
        val speed = random.nextInt(5, 20).toFloat()
        return Snowflake(x, y, radius, speed)
    }


    var snowflakes by remember {
        mutableStateOf((0..flakesCount).map { randomSnowflake() })
    }
    var size by remember {
        mutableStateOf(Size(0f, 0f))
    }
    LaunchedEffect(flakesSpeedUp) {
        while (true) {
            snowflakes = snowflakes.map { snowflake ->
                if (snowflake.y > size.height) {
                    snowflake.copy(y = -snowflake.radius)
                } else {
                    snowflake.copy(y = snowflake.y + snowflake.speed * flakesSpeedUp)
                }
            }
            delay(16) // 60 fps
        }
    }
    Box(modifier = modifier.fillMaxWidth()) {
        Image(bitmap = image, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds, colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }))
        Canvas(modifier = modifier.fillMaxSize()) {
            size = this.size
//            drawImage(
//                image = image,
//                dstSize = IntSize(size.width.toInt(), size.height.toInt()),
//                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
//            )
            snowflakes.forEach { snowflake ->
                drawCircle(
                    Color.White.copy(alpha = 0.5f),
                    radius = snowflake.radius,
                    center = Offset(snowflake.x, snowflake.y)
                )
            }
        }
    }
}

data class Snowflake(
    var x: Float,
    var y: Float,
    var radius: Float,
    var speed: Float
)


