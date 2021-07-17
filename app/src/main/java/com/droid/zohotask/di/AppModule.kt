package com.droid.zohotask.di

import android.content.Context
import androidx.room.Room
import com.droid.zohotask.db.UserDatabase
import com.droid.zohotask.db.UserListDao
import com.droid.zohotask.main.DefaultMainRepository
import com.droid.zohotask.main.MainRepository
import com.droid.zohotask.model.UserApi
import com.droid.zohotask.utils.Constants.BASE_URL
import com.droid.zohotask.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by SARATH on 17-07-2021
 */

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUserApi() : UserApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserApi::class.java)

    @Singleton
    @Provides
    fun providesDatabase(db: UserDatabase) : UserListDao{
        return db.getUserListDao()
    }

    @Singleton
    @Provides
    fun provideMainRepository(api: UserApi) : MainRepository = DefaultMainRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers() : DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }

    @Provides
    @Singleton
    fun provideBookDao(appDatabase: UserDatabase): UserListDao {
        return appDatabase.getUserListDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): UserDatabase {
        return Room.databaseBuilder(
            appContext,
            UserDatabase::class.java,
            "user_list.db"
        ).build()
    }
}