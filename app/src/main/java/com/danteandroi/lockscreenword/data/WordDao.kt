/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.danteandroi.lockscreenword.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the [Word] class.
 */
@Dao
interface WordDao {

    @Query("SELECT * FROM `46ji`")
    fun getWords(): Flow<List<Word>>

    @Query("SELECT *  FROM `46ji` WHERE word = :word ")
    fun getWord(word: String): Flow<Word>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiaries(Word: List<Word>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(Word: Word)

    @Delete
    suspend fun deleteWord(Word: Word)

    @Query("DELETE FROM `46ji` WHERE word = :word ")
    suspend fun deleteWord(word: String)

}
