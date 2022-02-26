package com.danteandroi.lockscreenword.source

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Du Wenyu
 * 2022/2/13
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    fun allWords() = repository.allWords()

}