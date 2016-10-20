package chat.dao;

import chat.model.Message;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Message DAO
 *
 * @author Igor
 */
public interface MessageDao {

    /**
     * Get message list by time interval
     * @param timeFrom start time 
     * @param timeTo end time
     * @return message list
     */
    List<Message> listMessages(Date timeFrom, Date timeTo);

    /**
     * Get recent message list starting from last message id
     * @param fromMessageId message id to list from
     * @return message list
     */    
    List<Message> listMessagesRecent(int fromMessageId);

    /**
     * Save message
     * @param message message to save
     */
    void saveMessage(Message message);
}
