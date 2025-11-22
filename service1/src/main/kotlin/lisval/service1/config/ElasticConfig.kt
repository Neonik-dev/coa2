package lisval.service1.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.elasticsearch.client.RestClient
import org.apache.http.HttpHost
import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.transport.ElasticsearchTransport
import co.elastic.clients.transport.rest_client.RestClientTransport
import co.elastic.clients.json.jackson.JacksonJsonpMapper


@Configuration
class ElasticConfig(
    @param:Value("\${spring.elasticsearch.uris}")
    private val elasticUrl: String
) {

    @Bean
    fun restClient(): RestClient {
        return RestClient.builder(HttpHost.create(elasticUrl))
            .build()
    }

    @Bean
    fun elasticsearchClient(restClient: RestClient): ElasticsearchClient {
        val transport: ElasticsearchTransport =
            RestClientTransport(restClient, JacksonJsonpMapper())
        return ElasticsearchClient(transport)
    }
}
