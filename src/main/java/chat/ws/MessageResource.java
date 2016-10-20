package chat.ws;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import chat.dao.MessageDao;
import chat.model.Message;
import javax.ws.rs.core.GenericEntity;

/**
 * Messaging REST service
 * 
 * @author Igor
 */
@Path("messages")
public class MessageResource {

    private static final Logger log = LoggerFactory.getLogger(MessageResource.class);

    @Inject
    private MessageDao messageDao;
    
    /**
     * Create error response
     * @param message text message
     * @return response
     */
    private Response errorResponse( String message ) {
        Map err = new HashMap();
        err.put("success", false);
        err.put("message", message);
        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity( err )
            .build();
    }
    
    /**
     * Create messages list response
     * @param list messages
     * @return response
     */
    private Response responseMessages( List<Message> list ) {
        return Response
                .ok( new GenericEntity<List<Message>>( list, List.class) )
                .build();
    }

    /**
     * Get pending messages starting from last message id
     * 
     * @param fromMessageId last message id, default 0
     * @return response with messages
     */
    @GET    
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPendingMessages( 
            @QueryParam("lastMessageId")Integer fromMessageId ) {
        log.debug("Enter getMessage from message id {}", fromMessageId);
        fromMessageId = fromMessageId != null
                ? fromMessageId
                : 0;
        List<Message> messages = messageDao.listMessagesRecent( fromMessageId );
        log.debug("Find messages count {}", messages.size());
        return responseMessages(messages);
    }

    /**
     * Get message from archive by time interval
     * 
     * @param timeFrom start time
     * @param timeTo end time
     * @return response {@link Response}
     */
    @GET    
    @Path("archive")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArchiveMessages(
            @QueryParam("timeFrom")Long timeFrom,
            @QueryParam("timeTo")Long timeTo) {
        log.debug("Get archive message from {} to {}", timeFrom, timeTo);
        if (timeFrom == null || timeTo == null) {
            return errorResponse( "Dates <from> or <to> not defined" );
        }
        if (timeFrom > timeTo) {
            return errorResponse( "Date <from> can't be after than <to>" );
        }
        List<Message> messages = messageDao.listMessages( 
                new Date( timeFrom ), new Date( timeTo ) );
        log.debug("Find messages count {}", messages.size());
        return responseMessages(messages);
    }

    /**
     * Publish message to all
     * @param message message
     * @return response {@link Response.Status#OK} with message 
     * or {@link Response.Status#BAD_REQUEST} if message user or text is empty
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postMessage( Message message ) {
        log.debug("Enter postMessage {}", message);
        if (message.getText() == null || message.getUser() == null) {
            log.warn("Message contains empty user or text");
            return errorResponse("Message should have <user> and <text> fields");
        }
        messageDao.saveMessage(message);
        log.debug("Message saved {}", message);
        return Response
                .ok( message )
                .build();
    }
}