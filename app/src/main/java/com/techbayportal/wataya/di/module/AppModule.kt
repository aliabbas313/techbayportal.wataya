package com.techbayportal.wataya.di.module

import android.app.Application
import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.techbayportal.wataya.BuildConfig
import com.techbayportal.wataya.helper.Constant
import com.techbayportal.wataya.mvp.data.DataManager
import com.techbayportal.wataya.mvp.data.local.PreferencesHelper
import com.techbayportal.wataya.mvp.data.remote.ApiService
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [PresenterBuildersModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val spec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA).build()

        val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build()

        val csslv3 = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.SSL_3_0)
                .build()

        val specs = arrayListOf(spec, cs, csslv3, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT)

        return OkHttpClient.Builder()
                .connectionSpecs(specs)
                .readTimeout(Constant.READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Constant.CONN_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor).addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    requestBuilder.header(Constant.headerName, Constant.headerValue)
                    requestBuilder.header("Accept", Constant.headerValue)
//                    .header("custom_header", "pre_prod")
                    chain.proceed(requestBuilder.build())
                }
                .addInterceptor(ConnectivityInterceptor(com.techbayportal.wataya.Application.applicationContext()))
                .build()
    }

    @Singleton
    @Provides
    fun provideApiInterface(client: OkHttpClient): ApiService {

        return Retrofit.Builder()
                .baseUrl(BuildConfig.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providePrefs(): PreferencesHelper {
        return PreferencesHelper(com.techbayportal.wataya.Application.applicationContext())
    }

    @Singleton
    @Provides
    fun provideDataManager(preferencesHelper: PreferencesHelper, apiService: ApiService): DataManager {
        return DataManager(preferencesHelper, apiService)
    }

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Provides
    internal fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    internal fun provideSchedulerProvider(): SchedulerProvider = SchedulerProvider()


}