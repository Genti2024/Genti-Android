package kr.genti.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.genti.data.dataSource.AuthDataSource
import kr.genti.data.dataSource.CreateDataSource
import kr.genti.data.dataSourceImpl.AuthDataSourceImpl
import kr.genti.data.dataSourceImpl.CreateDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource = authDataSourceImpl

    @Provides
    @Singleton
    fun provideCreateDataSource(createDataSourceImpl: CreateDataSourceImpl): CreateDataSource = createDataSourceImpl
}
