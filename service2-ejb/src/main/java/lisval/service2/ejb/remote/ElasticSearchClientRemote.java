package lisval.service2.ejb.remote;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import jakarta.ejb.Remote;

@Remote
public interface ElasticSearchClientRemote {
    ElasticsearchClient getClient();
}
