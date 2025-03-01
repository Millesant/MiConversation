package me.millesant.conversation.core.context;

import me.millesant.conversation.domain.ConversationContext;
import me.millesant.conversation.domain.ConversationPrefix;

/**
 * A conversation prefix that displays nothing.
 */
public class NullConversationPrefix implements ConversationPrefix {

    @Override
    public String getPrefix(ConversationContext context) {
        return "";
    }

}
