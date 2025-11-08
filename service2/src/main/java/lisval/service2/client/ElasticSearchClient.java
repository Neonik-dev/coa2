package lisval.service2.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

@Singleton
public class ElasticSearchClient {

    private RestClient restClient;
    private ElasticsearchClient client;

    @PostConstruct
    public void init() {
        restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
        var transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        client = new ElasticsearchClient(transport);
    }

    public ElasticsearchClient getClient() {
        return client;
    }

    @PreDestroy
    public void close() throws Exception {
        restClient.close();
    }
}
