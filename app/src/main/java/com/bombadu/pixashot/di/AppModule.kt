package com.bombadu.pixashot.di

import android.content.Context
import androidx.room.Room
import com.bombadu.pixashot.util.Constants.BASE_URL
import com.bombadu.pixashot.util.Constants.DATABASE_NAME
import com.bombadu.pixashot.repository.DefaultImageRepository
import com.bombadu.pixashot.repository.ImageRepository
import com.bombadu.pixashot.network.PixabayAPI
import com.bombadu.pixashot.local.LocalDao
import com.bombadu.pixashot.local.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideDefaultImagePostRepository(
        dao: LocalDao,
        api: PixabayAPI
    ) = DefaultImageRepository(dao, api) as ImageRepository

    @Singleton
    @Provides
    fun provideLocalDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, LocalDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideLocalDao(
        database: LocalDatabase
    ) = database.localDao()


}