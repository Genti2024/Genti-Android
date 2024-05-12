package kr.genti.android.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
//    @Provides
//    @Singleton
//    fun provideAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource =
//        authDataSourceImpl
}
