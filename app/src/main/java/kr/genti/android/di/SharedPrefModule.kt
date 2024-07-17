package kr.genti.android.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.genti.data.local.UserSharedPref
import kr.genti.data.local.UserSharedPrefImpl
import kr.genti.data.repositoryImpl.UserRepositoryImpl
import kr.genti.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideSharedPref(sharedPrefImpl: UserSharedPrefImpl): UserSharedPref = sharedPrefImpl

    @Provides
    @Singleton
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository = userRepositoryImpl
}
