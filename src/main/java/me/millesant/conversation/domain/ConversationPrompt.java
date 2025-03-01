package me.millesant.conversation.domain;

/**
 * A prompt is a question, message, or other conversational element
 * that may receive a response from the player.
 */
public interface ConversationPrompt {

    /**
     * Special prompt value indicating the end of a conversation.
     */
    ConversationPrompt END_OF_CONVERSATION = null;

    /**
     * Gets the text to display to the player when this prompt is presented.
     *
     * @param context Context information about the conversation
     * @return The text to display
     */
    String getPromptText(ConversationContext context);

    /**
     * Processes input from the player and determines the next prompt.
     *
     * @param context Context information about the conversation
     * @param input   The input text from the player
     * @return The next prompt in the conversation or END_OF_CONVERSATION to end
     */
    ConversationPrompt acceptInput(ConversationContext context, String input);

    /**
     * Determines if this prompt awaits player input.
     *
     * @param context Context information about the conversation
     * @return true if the prompt awaits input, false to skip to the next prompt
     */
    boolean blocksForInput(ConversationContext context);

}
