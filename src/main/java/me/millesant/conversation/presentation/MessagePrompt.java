package me.millesant.conversation.presentation;

import me.millesant.conversation.domain.ConversationContext;
import me.millesant.conversation.domain.ConversationPrompt;

/**
 * A prompt that displays a message and automatically advances to the next prompt.
 */
public abstract class MessagePrompt implements ConversationPrompt {

    @Override
    public final boolean blocksForInput(ConversationContext context) {
        return false;
    }

    @Override
    public final ConversationPrompt acceptInput(ConversationContext context, String input) {
        return this.getNextPrompt(context);
    }

    /**
     * Gets the next prompt to display after this message.
     *
     * @param context The conversation context
     * @return The next prompt
     */
    protected abstract ConversationPrompt getNextPrompt(ConversationContext context);

}
