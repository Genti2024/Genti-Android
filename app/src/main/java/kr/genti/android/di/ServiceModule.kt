package kr.genti.android.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    //    @Provides
//    @Singleton
//    fun provideAuthService(@JWT retrofit: Retrofit): AuthService =
//        retrofit.create(AuthService::class.java)
}
