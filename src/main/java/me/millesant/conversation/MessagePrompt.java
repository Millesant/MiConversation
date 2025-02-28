package me.millesant.conversation;

public abstract class MessagePrompt implements ConversationPrompt {

    @Override
    public final boolean blocksForInput(final ConversationContext context) {
        return false;
    }

    @Override
    public final ConversationPrompt acceptInput(final ConversationContext context,
                                                final String input) {
        return this.getNextPrompt(context);
    }

    protected abstract ConversationPrompt getNextPrompt(final ConversationContext context);

}
