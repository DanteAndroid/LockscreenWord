package com.danteandroi.lockscreenword.source

import com.danteandroi.lockscreenword.data.Word
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Du Wenyu
 * 2022/2/13
 */
@DelicateCoroutinesApi
@Singleton
class WordsViewModel @Inject constructor(private val repository: MainRepository) {

    fun allWords() = repository.allWords()

    suspend fun updateWord(word: Word?) {
        word?.let {
            repository.updateWord(it)
        }
    }

}