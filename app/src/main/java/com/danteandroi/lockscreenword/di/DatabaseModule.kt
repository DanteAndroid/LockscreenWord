package com.danteandroi.lockscreenword.di

import android.content.Context
import com.danteandroi.lockscreenword.data.AppDatabase
import com.danteandroi.lockscreenword.data.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Dante
 * 2020/12/11
 */
@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideDiaryDao(appDatabase: AppDatabase): WordDao {
        return appDatabase.wordDao()
    }

}