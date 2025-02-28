package me.millesant.conversation;

public interface ConversationCanceller extends Cloneable {

    void setConversation(final Conversation conversation);

    boolean cancelBasedOnInput(final ConversationContext context, final String input);

    ConversationCanceller clone();

}
