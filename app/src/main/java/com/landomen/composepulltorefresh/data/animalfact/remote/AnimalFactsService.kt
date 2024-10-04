package com.landomen.composepulltorefresh.data.animalfact.remote

import com.landomen.composepulltorefresh.data.animalfact.model.AnimalFact
import com.landomen.composepulltorefresh.data.animalfact.remote.model.AnimalFactsApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.request

class AnimalFactsService(private val httpClient: HttpClient) : AnimalFactsRemote {

    override suspend fun getAnimalFacts(): List<AnimalFact> {
        val facts: AnimalFactsApiResponse = httpClient.get("$BASE_URL$FACTS_ENDPOINT").body()
        return facts.facts.mapIndexed { id, fact ->
            AnimalFact(id + 1, fact)
        }
    }

    companion object {
        // https://kinduff.github.io/dog-api/
        private const val BASE_URL = "https://dog-api.kinduff.com/api"
        private const val FACTS_ENDPOINT = "/facts?number=10"
    }
}