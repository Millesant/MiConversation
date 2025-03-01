package me.millesant.conversation.presentation;

import me.millesant.conversation.domain.ConversationContext;
import me.millesant.conversation.domain.ConversationPrompt;

import java.util.Objects;

/**
 * A prompt that validates player input before continuing.
 */
public abstract class ValidatingPrompt implements ConversationPrompt {

    @Override
    public final boolean blocksForInput(ConversationContext context) {
        return true;
    }

    @Override
    public final ConversationPrompt acceptInput(ConversationContext context, String input) {
        if (this.isInputValid(context, input)) {
            return this.acceptValidatedInput(context, input);
        }

        var errorMessage = this.getFailedValidationText(context, input);

        if (Objects.nonNull(errorMessage) && (!(errorMessage.isBlank()))) {
            context.participant().sendMessage(context.prefix().getPrefix(context) + errorMessage);
        }

        return this;
    }

    /**
     * Checks if the player's input is valid.
     *
     * @param context The conversation context
     * @param input The input to validate
     * @return true if valid, false otherwise
     */
    protected abstract boolean isInputValid(ConversationContext context, String input);

    /**
     * Processes valid input and determines the next prompt.
     *
     * @param context The conversation context
     * @param input The validated input
     * @return The next prompt
     */
    protected abstract ConversationPrompt acceptValidatedInput(ConversationContext context, String input);

    /**
     * Gets the error message to display for invalid input.
     *
     * @param context The conversation context
     * @param invalidInput The invalid input
     * @return The error message or null for no message
     */
    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return String.format("An invalid input was provided: %s", invalidInput);
    }

}
