package com.itmo.blps.bankiru.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class DadataIntegrationUtils(
    @Value("\${integration.dadata.token}") private val apiToken: String,
    private val restTemplate: RestTemplate
) {

    fun checkExistCompany(companyName: String): Boolean {
        val headers: MultiValueMap<String, String> = LinkedMultiValueMap()
        headers["Authorization"] = "Token $apiToken"
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"

        val requestBody: HttpEntity<String> = HttpEntity(ObjectMapper().writeValueAsString(mapOf(Pair("query", companyName), Pair("count", 1))), headers)
        val suggestions: List<Any> = restTemplate.postForEntity(
            "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/party",
            requestBody,
            Map::class.java
        ).body!!["suggestions"] as List<Any>
        return suggestions.isNotEmpty()
    }
}