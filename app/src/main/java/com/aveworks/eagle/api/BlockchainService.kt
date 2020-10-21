package com.aveworks.eagle.api


import com.aveworks.eagle.data.MultiAddressResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
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

            val contentType = "application/json".toMediaType()
            val json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            }

            return Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(json.asConverterFactory(contentType))
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
        is SerializationException -> {
            return "Couldn't parse data"
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