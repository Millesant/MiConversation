package me.millesant.conversation.domain;


/**
 * Event raised when a conversation is abandoned.
 *
 * @param conversation The conversation that was abandoned
 * @param canceller The canceller that caused the abandonment, or null if abandoned gracefully
 * @param gracefulExit Whether the conversation ended normally rather than being cancelled
 */
public record ConversationAbandonedEvent(
    Conversation conversation,
    ConversationCanceller canceller,
    boolean gracefulExit
) {

    /**
     * Creates an event for a conversation that ended gracefully.
     *
     * @param conversation The conversation
     * @return The event
     */
    public static ConversationAbandonedEvent gracefulExit(Conversation conversation) {
        return new ConversationAbandonedEvent(conversation, null, true);
    }

    /**
     * Creates an event for a conversation that was cancelled.
     *
     * @param conversation The conversation
     * @param canceller The canceller that triggered the abandonment
     * @return The event
     */
    public static ConversationAbandonedEvent cancelled(Conversation conversation, ConversationCanceller canceller) {
        return new ConversationAbandonedEvent(conversation, canceller, false);
    }

}
