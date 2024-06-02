package kr.genti.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.genti.data.repositoryImpl.AuthRepositoryImpl
import kr.genti.data.repositoryImpl.CreateRepositoryImpl
import kr.genti.data.repositoryImpl.FeedRepositoryImpl
import kr.genti.domain.repository.AuthRepository
import kr.genti.domain.repository.CreateRepository
import kr.genti.domain.repository.FeedRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository = authRepositoryImpl

    @Provides
    @Singleton
    fun provideCreateRepository(createRepositoryImpl: CreateRepositoryImpl): CreateRepository = createRepositoryImpl

    @Provides
    @Singleton
    fun provideFeedRepository(feedRepositoryImpl: FeedRepositoryImpl): FeedRepository = feedRepositoryImpl
}
