package com.pico.mymusicplayer.domain.use_case

import androidx.media3.session.MediaController
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCurrentSongPositionUseCase(private val futureMediaController: ListenableFuture<MediaController>) {

    operator fun invoke(): Flow<Long> {
        val mediaController: MediaController = futureMediaController.get()
        return flow {
            while (true) {
                emit(mediaController.currentPosition)
                delay(100)
            }
        }
    }
}
