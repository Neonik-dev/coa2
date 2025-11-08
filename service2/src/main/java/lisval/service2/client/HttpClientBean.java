package lisval.service2.client;

import jakarta.ejb.Stateless;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

@Stateless
public class HttpClientBean {

    private Client createClient() {
        return ClientBuilder.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public String get(String url) {
        try (Client client = createClient();
             Response response = client.target(url)
                     .request(MediaType.APPLICATION_JSON)
                     .get()) {

            if (response.getStatus() == 200) {
                return response.readEntity(String.class);
            } else {
                throw new RuntimeException("HTTP error: " + response.getStatus());
            }
        }
    }

    public String post(String url, String jsonBody) {
        try (Client client = createClient();
             Response response = client.target(url)
                     .request(MediaType.APPLICATION_JSON)
                     .post(Entity.entity(jsonBody, MediaType.APPLICATION_JSON))) {

            if (response.getStatus() == 200 || response.getStatus() == 201) {
                return response.readEntity(String.class);
            } else {
                throw new RuntimeException("HTTP error: " + response.getStatus());
            }
        }
    }

    public String put(String url, String jsonBody) {
        try (Client client = createClient();
             Response response = client.target(url)
                     .request(MediaType.APPLICATION_JSON)
                     .put(Entity.entity(jsonBody, MediaType.APPLICATION_JSON))) {

            if (response.getStatus() == 200) {
                return response.readEntity(String.class);
            } else {
                throw new RuntimeException("HTTP error: " + response.getStatus());
            }
        }
    }

    public boolean delete(String url) {
        try (Client client = createClient();
             Response response = client.target(url)
                     .request()
                     .delete()) {

            return response.getStatus() == 200 || response.getStatus() == 204;
        }
    }
}