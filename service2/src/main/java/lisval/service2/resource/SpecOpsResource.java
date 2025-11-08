package lisval.service2.resource;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import lisval.service2.client.HttpClientBean;

@Path("/isu/group")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SpecOpsResource {

    private final ObjectMapper mapper = new JacksonJsonpMapper().objectMapper();

    @Inject
    HttpClientBean httpClient;

    @POST
    @Path("/{group-id}/expel-all")
    public Response index(@PathParam("group-id") String groupId) throws Exception {
        Map<String, Object> parsedResponse = mapper.readValue(httpClient.get("http://localhost:8081/api/studygroups/" + groupId), Map.class);
        parsedResponse.put("groupAdmin", null);
        httpClient.put("http://localhost:8081/api/studygroups/" + groupId, mapper.writeValueAsString(parsedResponse));
        return Response.ok().build();
    }

    @POST
    @Path("/{group-id}/change-edu-form/{new-form}")
    public Response index(@PathParam("group-id") String groupId, @PathParam("new-form") String newForm) throws Exception {
        Map<String, Object> parsedResponse = mapper.readValue(httpClient.get("http://localhost:8081/api/studygroups/" + groupId), Map.class);
        parsedResponse.put("formOfEducation", newForm);
        parsedResponse.put("groupAdmin", ((Map<String, Object>) parsedResponse.get("groupAdmin")).get("id"));
        httpClient.put("http://localhost:8081/api/studygroups/" + groupId, mapper.writeValueAsString(parsedResponse));
        return Response.ok().build();
    }
}
