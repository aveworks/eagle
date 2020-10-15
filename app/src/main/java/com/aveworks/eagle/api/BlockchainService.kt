package com.aveworks.eagle.api


import com.aveworks.eagle.data.MultiAddressResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.reactivex.rxjava3.core.Observable
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import java.net.SocketTimeoutException


interface BlockchainService {

    @GET("multiaddr")
    fun multiAddress(@Query("active") address: String): Call<MultiAddressResponse>

    @GET("multiaddr")
    fun multiAddressObservable(
        @Query("active") address: String, @Query("n") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): Observable<MultiAddressResponse>

    companion object {
        private const val BASE_URL = "https://blockchain.info/"

        private val logger =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        private val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        fun create(): BlockchainService {
            return create(BASE_URL.toHttpUrl())
        }

        /**
         * To my knowledge currently only Jackson w/ Kotlin module supports the null safety of kotlin data classes
         */
        fun create(url: HttpUrl): BlockchainService {
            return Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(
                    JacksonConverterFactory.create(
                        ObjectMapper().registerModule(
                            KotlinModule()
                        )
                    )
                )
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(BlockchainService::class.java)
        }
    }
}

/**
 * TODO
 *  - move errors to string resource
 *  - Return message for HTTP 429 - Too many requests
 */
fun getBlockchainError(e: Throwable): String? {
    when (e) {
        is HttpException -> {
            e.response()?.errorBody()?.string().let {
                return it
            }
        }
        is MissingKotlinParameterException -> {
            return "Couldn't parse data. Blockchain API maybe have changed?"
        }
        is SocketTimeoutException -> {
            return "Socket Timeout"
        }
        is IOException -> {
            return "Network Error"
        }
    }

    return e.message
}