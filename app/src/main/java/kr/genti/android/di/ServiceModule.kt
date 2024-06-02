package kr.genti.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.genti.data.service.AuthService
import kr.genti.data.service.CreateService
import kr.genti.data.service.FeedService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideAuthService(
        @RetrofitQualifier.REISSUE retrofit: Retrofit,
    ): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideCreateService(
        @RetrofitQualifier.JWT retrofit: Retrofit,
    ): CreateService = retrofit.create(CreateService::class.java)

    @Provides
    @Singleton
    fun provideFeedService(
        @RetrofitQualifier.JWT retrofit: Retrofit,
    ): FeedService = retrofit.create(FeedService::class.java)
}
