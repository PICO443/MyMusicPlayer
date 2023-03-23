package com.pico.mymusicplayer.di

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.pico.mymusicplayer.data.data_source.AudioMediaStore
import com.pico.mymusicplayer.data.repository.SongRepositoryImpl
import com.pico.mymusicplayer.domain.repository.SongRepository
import com.pico.mymusicplayer.domain.use_case.GetCurrentSongPositionUseCase
import com.pico.mymusicplayer.domain.use_case.GetSongsUseCase
import com.pico.mymusicplayer.domain.use_case.GetWaveformUseCase
import com.pico.mymusicplayer.domain.use_case.TogglePlaySongUseCase
import com.pico.mymusicplayer.media.PlaybackService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import linc.com.amplituda.Amplituda
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideMediaController(@ApplicationContext context: Context): ListenableFuture<MediaController> {
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
       return MediaController.Builder(context, sessionToken).buildAsync()

    }
    @Provides
    @Singleton
    fun provideContentResolverHelper(@ApplicationContext context: Context): AudioMediaStore{
        return AudioMediaStore(context)
    }

    @Provides
    @Singleton
    fun provideSongRepository(audioMediaStore: AudioMediaStore): SongRepository{
        return SongRepositoryImpl(audioMediaStore)
    }

    @Provides
    @Singleton
    fun provideGetSongsUseCase(songRepository: SongRepository): GetSongsUseCase{
        return GetSongsUseCase(songRepository)
    }

    @Provides
    @Singleton
    fun provideToggleSongsUseCase(): TogglePlaySongUseCase{
        return TogglePlaySongUseCase()
    }

    @Provides
    @Singleton
    fun provideAmplituda(@ApplicationContext ctx: Context): Amplituda{
        return Amplituda(ctx)
    }

    @Provides
    @Singleton
    fun provideGetWaveformUseCase(amplituda: Amplituda): GetWaveformUseCase{
        return GetWaveformUseCase(amplituda)
    }

    @Provides
    @Singleton
    fun provideGetCurrentPositionUseCase(futureMediaController: ListenableFuture<MediaController>): GetCurrentSongPositionUseCase{
        return GetCurrentSongPositionUseCase(futureMediaController)
    }
}