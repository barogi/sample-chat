package chat.ws;

import chat.model.Message;
import chat.ws.MessageApplication;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.persistence.jaxb.BeanValidationMode;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Igor
 */
public class MessageResourceTest extends JerseyTest {    
    
    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new MessageApplication();
    }

    @Override
    protected void configureClient(final ClientConfig config) {
        super.configureClient(config);
        config.register(JacksonFeature.class);        
        config.register(MoxyJsonFeature.class);
        // Turn off BV otherwise the entities on client would be validated as well.
        config.register(new MoxyJsonConfig()
                .property(MarshallerProperties.BEAN_VALIDATION_MODE, BeanValidationMode.NONE)
                .resolver());
    }    
    
    @Test
    public void testGetPendingMessages() {
        WebTarget target = target()
                .path("messages")
                .queryParam("fromMessageId", 1);
        Response response = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertEquals(200, response.getStatus());
        List<Message> messages = response.readEntity( List.class );        
        assertNotNull(messages);
        assertEquals(messages.size(), 3);
    }
    
    @Test
    public void testPostMessage() {
        HashMap<String, String> msg = new HashMap();
        msg.put("user", "testUser");
        msg.put("text", "testUser message");
        
        WebTarget target = target().path("messages");
        Response response = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(msg, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        Message msg0 = response.readEntity(Message.class);

        assertEquals(200, response.getStatus());
        assertNotEquals(msg0.getId(), 0);
    }
    
    @Test
    public void testPostMessage_Anonimous() {
        HashMap<String, String> msg = new HashMap();        
        msg.put("text", "testUser message");        
        WebTarget target = target().path("messages");
        Response response = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(msg, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testPostMessage_Empty() {
        HashMap<String, String> msg = new HashMap();        
        msg.put("user", "testUser");
        WebTarget target = target().path("messages");
        Response response = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(msg, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }    
    
    @Test
    public void testGetArchiveMessages_toDateUndefined() {
        //test toDate undefined
        WebTarget target = target()
                .path("messages/archive")
                .queryParam("fromDate", System.currentTimeMillis());
        Response response = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        Map map = response.readEntity(HashMap.class);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }  
    
    @Test
    public void testGetArchiveMessages_fromDateUndefined() {
        //test toDate undefined
        WebTarget target = target()
                .path("messages/archive")
                .queryParam("toDate", System.currentTimeMillis());
        Response response = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();        
        Map map = response.readEntity(HashMap.class);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }      
    
    @Test
    public void testGetArchiveMessages_dateFrom_Bigger_dateTo() {        
        WebTarget target = target()
                .path("messages/archive")
                .queryParam("fromDate", System.currentTimeMillis()+10000)
                .queryParam("toDate", System.currentTimeMillis());
        Response response = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();        
        Map map = response.readEntity(HashMap.class);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
