package kr.genti.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.genti.data.repositoryImpl.AuthRepositoryImpl
import kr.genti.data.repositoryImpl.CreateRepositoryImpl
import kr.genti.data.repositoryImpl.FeedRepositoryImpl
import kr.genti.data.repositoryImpl.GenerateRepositoryImpl
import kr.genti.data.repositoryImpl.InfoRepositoryImpl
import kr.genti.data.repositoryImpl.UploadRepositoryImpl
import kr.genti.domain.repository.AuthRepository
import kr.genti.domain.repository.CreateRepository
import kr.genti.domain.repository.FeedRepository
import kr.genti.domain.repository.GenerateRepository
import kr.genti.domain.repository.InfoRepository
import kr.genti.domain.repository.UploadRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository = authRepositoryImpl

    @Provides
    @Singleton
    fun provideInfoRepository(infoRepositoryImpl: InfoRepositoryImpl): InfoRepository = infoRepositoryImpl

    @Provides
    @Singleton
    fun provideCreateRepository(createRepositoryImpl: CreateRepositoryImpl): CreateRepository = createRepositoryImpl

    @Provides
    @Singleton
    fun provideFeedRepository(feedRepositoryImpl: FeedRepositoryImpl): FeedRepository = feedRepositoryImpl

    @Provides
    @Singleton
    fun provideUploadRepository(uploadRepositoryImpl: UploadRepositoryImpl): UploadRepository = uploadRepositoryImpl

    @Provides
    @Singleton
    fun provideGenerateRepository(generateRepositoryImpl: GenerateRepositoryImpl): GenerateRepository = generateRepositoryImpl
}
