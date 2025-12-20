package lisval.service2.ejb.stateless;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Stateless;
import lisval.service2.ejb.remote.ElasticSearchClientRemote;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

@Stateless
public class ElasticSearchClient implements ElasticSearchClientRemote {

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
