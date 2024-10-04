package com.landomen

import android.app.Application
import android.util.Log
import com.landomen.composepulltorefresh.data.animalfact.AnimalFactsRemoteRepository
import com.landomen.composepulltorefresh.data.animalfact.AnimalFactsRepository
import com.landomen.composepulltorefresh.data.animalfact.remote.AnimalFactsRemote
import com.landomen.composepulltorefresh.data.animalfact.remote.AnimalFactsService
import com.landomen.composepulltorefresh.ui.home.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.observer.ResponseObserver
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApp : Application() {

    private val networkModule = module {
        factory<AnimalFactsRemote> { AnimalFactsService(get()) }

        single {
            HttpClient {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = true
                    })
                }

                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Log.d("Ktor", message)
                        }
                    }
                }

                install(ResponseObserver) {
                    onResponse { response ->
                        Log.d("Ktor", "${response.status.value}")
                    }
                }
            }
        }
    }

    private val repositoryModule = module {
        factory<AnimalFactsRepository> {
            AnimalFactsRemoteRepository(get())
        }
    }

    private val androidModule = module {
        viewModel { HomeViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApp)
            modules(networkModule, repositoryModule, androidModule)
        }
    }
}