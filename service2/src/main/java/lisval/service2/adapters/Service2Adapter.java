package lisval.service2.adapters;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import lisval.service2.ejb.remote.ElasticSearchClientRemote;
import lisval.service2.ejb.remote.HttpClientBeanRemote;

@ApplicationScoped
public class Service2Adapter {

    @EJB(lookup = "java:global/service2-1.0-SNAPSHOT/ElasticSearchClient!lisval.service2.ejb.remote.ElasticSearchClientRemote")
    private ElasticSearchClientRemote elasticSearchClient;

    @EJB(lookup = "java:global/service2-1.0-SNAPSHOT/HttpClientBean!lisval.service2.ejb.remote.HttpClientBeanRemote")
    private HttpClientBeanRemote httpClientBean;

    public ElasticsearchClient getClient() {
        return elasticSearchClient.getClient();
    }

    public HttpClientBeanRemote getHttpClientBean() {
        return httpClientBean;
    }
}
