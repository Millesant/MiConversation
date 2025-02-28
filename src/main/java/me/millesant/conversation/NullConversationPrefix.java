package me.millesant.conversation;

public class NullConversationPrefix implements ConversationPrefix {

    @Override
    public String getPrefix(final ConversationContext context) {
        return "";
    }

}
