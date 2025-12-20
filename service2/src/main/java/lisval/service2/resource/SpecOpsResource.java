package lisval.service2.resource;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import lisval.service2.adapters.Service2Adapter;

@Path("/isu/group")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SpecOpsResource {

    private final ObjectMapper mapper = new JacksonJsonpMapper().objectMapper();

    @Inject
    Service2Adapter service2Adapter;

    @POST
    @Path("/{group-id}/expel-all")
    public Response index(@PathParam("group-id") String groupId) throws Exception {
        Map<String, Object> parsedResponse = mapper.readValue(service2Adapter.getHttpClientBean().get("http://localhost:8052/api/studygroups/" + groupId), Map.class);
        parsedResponse.put("groupAdmin", null);
        service2Adapter.getHttpClientBean().put("http://localhost:8052/api/studygroups/" + groupId, mapper.writeValueAsString(parsedResponse));
        return Response.ok().build();
    }

    @POST
    @Path("/{group-id}/change-edu-form/{new-form}")
    public Response index(@PathParam("group-id") String groupId, @PathParam("new-form") String newForm) throws Exception {
        Map<String, Object> parsedResponse = mapper.readValue(service2Adapter.getHttpClientBean().get("http://localhost:8052/api/studygroups/" + groupId), Map.class);
        parsedResponse.put("formOfEducation", newForm);
        parsedResponse.put("groupAdmin", ((Map<String, Object>) parsedResponse.get("groupAdmin")).get("id"));
        service2Adapter.getHttpClientBean().put("http://localhost:8052/api/studygroups/" + groupId, mapper.writeValueAsString(parsedResponse));
        return Response.ok().build();
    }
}
