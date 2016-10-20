package chat.ws;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import chat.model.Message;

/**
 * Jersey JAX-RS Messaging application configuration
 * 
 * @author Igor
 */
public class MessageApplication extends ResourceConfig {
    public MessageApplication() {
        packages(Message.class.getPackage().getName());        
        register(RequestContextFilter.class);
        register(JacksonFeature.class);
        register(MoxyJsonFeature.class);        
        register(MessageResource.class);
    }
}
