package me.millesant.conversation;

public interface ConversationPrompt {

    ConversationPrompt END_OF_CONVERSATION = null;

    String getPromptText(final ConversationContext context);

    ConversationPrompt acceptInput(final ConversationContext context, String input);

    boolean blocksForInput(final ConversationContext context);

}
