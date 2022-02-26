package com.danteandroi.lockscreenword.source

import androidx.lifecycle.LiveData
import com.danteandroi.lockscreenword.data.Word
import com.danteandroi.lockscreenword.data.WordDao
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Du Wenyu
 * 2020/12/19
 */
@Singleton
class MainRepository @Inject constructor(
    private val wordDao: WordDao
) {

     fun allWords(): Flow<List<Word>> {
        return wordDao.getWords()
    }

    suspend fun updateWord(word: Word){
        wordDao.insertWord(word)
    }

}

