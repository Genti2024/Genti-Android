package kr.genti.network

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import kr.genti.domain.repository.AuthRepository
import kr.genti.domain.repository.UserRepository
import kr.genti.domain.usecase.auth.ReissueOldTokensUseCase
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
    private val reissueOldTokensUseCase: ReissueOldTokensUseCase
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authRequest = createAuthRequest(originalRequest)
        var response = chain.proceed(authRequest)
        if (response.code == CODE_TOKEN_EXPIRED) {
            response = handleTokenExpiration(chain, authRequest) ?: response
        }
        return response
    }

    private fun createAuthRequest(originalRequest: Request): Request {
        return if (userRepository.getAccessToken().isNotBlank()) {
            originalRequest.newBuilder().newAuthBuilder().build()
        } else {
            originalRequest
        }
    }

    private fun handleTokenExpiration(chain: Interceptor.Chain, authRequest: Request): Response? {
        reissueTokenAndProceed(chain, authRequest)?.let { return it }
        userRepository.clearInfo()
        notifyTokenExpired()
        return null
    }

    private fun reissueTokenAndProceed(chain: Interceptor.Chain, authRequest: Request): Response? {
        return try {
            runBlocking {
                reissueOldTokensUseCase()
            }.onSuccess {
                chain.call().cancel()
                val newRequest = authRequest.newBuilder()
                    .removeHeader(AUTHORIZATION)
                    .newAuthBuilder()
                    .build()
                return chain.proceed(newRequest)
            }
            null
        } catch (t: Throwable) {
            Timber.d(t.message)
            null
        }
    }

    private fun notifyTokenExpired() {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, TOKEN_EXPIRED_ERROR, Toast.LENGTH_LONG).show()
            ProcessPhoenix.triggerRebirth(context)
        }
    }

    private fun Request.Builder.newAuthBuilder() =
        this.addHeader(AUTHORIZATION, userRepository.getAccessToken())

    companion object {
        private const val CODE_TOKEN_EXPIRED = 401
        private const val TOKEN_EXPIRED_ERROR = "토큰이 만료되었어요\n다시 로그인 해주세요"
        private const val AUTHORIZATION = "Authorization"
    }
}
