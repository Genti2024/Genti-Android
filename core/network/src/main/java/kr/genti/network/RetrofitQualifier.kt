package kr.genti.network

import javax.inject.Qualifier

object RetrofitQualifier {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class JWT

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NOTOKEN
}
