package com.qp.esdemo

import com.jillesvangurp.ktsearch.KtorRestClient
import com.jillesvangurp.ktsearch.SearchClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ElasticSearchClientConfig {
    @Bean
    fun searchClient(): SearchClient {
        return SearchClient(
            KtorRestClient()
        )
    }
}
