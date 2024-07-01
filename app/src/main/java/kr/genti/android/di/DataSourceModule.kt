package kr.genti.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.genti.data.dataSource.AuthDataSource
import kr.genti.data.dataSource.CreateDataSource
import kr.genti.data.dataSource.FeedDataSource
import kr.genti.data.dataSource.GenerateDataSource
import kr.genti.data.dataSourceImpl.AuthDataSourceImpl
import kr.genti.data.dataSourceImpl.CreateDataSourceImpl
import kr.genti.data.dataSourceImpl.FeedDataSourceImpl
import kr.genti.data.dataSourceImpl.GenerateDataSourceImpl
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

    @Provides
    @Singleton
    fun provideFeedDataSource(feedDataSourceImpl: FeedDataSourceImpl): FeedDataSource = feedDataSourceImpl

    @Provides
    @Singleton
    fun provideGenerateDataSource(generateDataSourceImpl: GenerateDataSourceImpl): GenerateDataSource = generateDataSourceImpl
}
