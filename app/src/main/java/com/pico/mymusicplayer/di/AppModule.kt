package com.pico.mymusicplayer.di

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.pico.mymusicplayer.data.data_source.ContentResolverHelper
import com.pico.mymusicplayer.data.repository.SongRepositoryImpl
import com.pico.mymusicplayer.domain.repository.SongRepository
import com.pico.mymusicplayer.domain.use_case.GetSongsUseCase
import com.pico.mymusicplayer.media.PlaybackService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Duration
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
    fun provideContentResolverHelper(@ApplicationContext context: Context): ContentResolverHelper{
        return ContentResolverHelper(context)
    }

    @Provides
    @Singleton
    fun provideSongRepository(contentResolverHelper: ContentResolverHelper): SongRepository{
        return SongRepositoryImpl(contentResolverHelper)
    }

    @Provides
    @Singleton
    fun provideGetSongsUseCase(songRepository: SongRepository): GetSongsUseCase{
        return GetSongsUseCase(songRepository)
    }
}