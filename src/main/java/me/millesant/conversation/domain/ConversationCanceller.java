package me.millesant.conversation.domain;

/**
 * A mechanism that can cancel a conversation based on certain conditions.
 */
public interface ConversationCanceller extends Cloneable {

    /**
     * Sets the conversation this canceller is associated with.
     *
     * @param conversation The conversation
     */
    void attach(Conversation conversation);

    /**
     * Determines if the conversation should be cancelled based on input.
     *
     * @param context The conversation context
     * @param input The player's input
     * @return true to cancel the conversation, false otherwise
     */
    boolean cancelBasedOnInput(ConversationContext context, String input);

    /**
     * Creates a clone of this canceller for a new conversation.
     *
     * @return A clone of this canceller
     */
    ConversationCanceller clone();

}
