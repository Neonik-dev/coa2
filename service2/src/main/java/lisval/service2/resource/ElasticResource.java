package lisval.service2.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import lisval.service2.client.ElasticSearchClient;

@Path("/es")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ElasticResource {

    @Inject
    ElasticSearchClient es;

    @POST
    @Path("/{index}/{id}")
    public Response index(@PathParam("index") String index,
                          @PathParam("id") String id,
                          Map<String, Object> doc) throws Exception {
        es.getClient().index(i -> i.index(index).id(id).document(doc));
        return Response.ok().build();
    }

    @GET
    @Path("/{index}")
    public List<Map<String,Object>> find(@PathParam("index") String index,
                                         @QueryParam("q") String q) throws Exception {
        if (q == null || q.isBlank()) {
            return es.getClient().search(s -> s
                    .index(index)
                    .query(qb -> qb.matchAll(ma -> ma)), Map.class
            ).hits().hits().stream().map(h -> (Map<String, Object>) h.source()).toList();
        }

        // Проверяем, есть ли спецсимволы для не-полнотекстового поиска
        boolean isSpecialQuery = q.contains("*") || q.contains("?") || q.contains("\"") || q.contains("~");

        return es.getClient().search(s -> s
                .index(index)
                .query(qb -> {
                    if (isSpecialQuery) {
                        // wildcard / точное совпадение
                        return qb.wildcard(w -> w
                                .field("*")
                                .value(q)
                        );
                    } else {
                        // обычный полнотекстовый поиск
                        return qb.multiMatch(m -> m
                                .query(q)
                                .fields(List.of("*"))
                                .fuzziness("AUTO") // опционально, чтобы учитывать опечатки
                        );
                    }
                }), Map.class
        ).hits().hits().stream().map(h -> (Map<String, Object>) h.source()).toList();
    }
}
