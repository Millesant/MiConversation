package me.millesant.conversation.domain;

/**
 * Provides a prefix for conversation messages.
 */
public interface ConversationPrefix {
    /**
     * Gets the prefix for a conversation message.
     *
     * @param context Context information about the conversation
     * @return The prefix text
     */
    String getPrefix(ConversationContext context);

}
