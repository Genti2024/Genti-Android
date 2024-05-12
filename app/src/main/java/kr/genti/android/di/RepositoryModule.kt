package kr.genti.android.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    //    @Provides
//    @Singleton
//    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository =
//        authRepositoryImpl
}
