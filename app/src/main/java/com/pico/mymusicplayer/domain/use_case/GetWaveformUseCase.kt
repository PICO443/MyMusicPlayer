package com.pico.mymusicplayer.domain.use_case

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import linc.com.amplituda.Amplituda
import linc.com.amplituda.Cache
import linc.com.amplituda.Compress

class GetWaveformUseCase(val amplituda: Amplituda) {
    suspend operator fun invoke(path: String): List<Int> = withContext(Dispatchers.IO){
        return@withContext amplituda.processAudio(path, Cache.withParams(Cache.REUSE)).get().amplitudesAsList().map { it!! }
    }
}