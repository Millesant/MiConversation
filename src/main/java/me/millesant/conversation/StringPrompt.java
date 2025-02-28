package me.millesant.conversation;

public abstract class StringPrompt implements ConversationPrompt {

    @Override
    public boolean blocksForInput(final ConversationContext context) {
        return true;
    }

}
