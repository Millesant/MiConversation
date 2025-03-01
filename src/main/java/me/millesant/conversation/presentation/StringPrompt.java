package me.millesant.conversation.presentation;

import me.millesant.conversation.domain.ConversationContext;
import me.millesant.conversation.domain.ConversationPrompt;

/**
 * A basic prompt that accepts any string input.
 */
public abstract class StringPrompt implements ConversationPrompt {

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }

}
