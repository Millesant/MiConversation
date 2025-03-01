package me.millesant.conversation.presentation;

import lombok.SneakyThrows;

import me.millesant.conversation.domain.ConversationContext;
import me.millesant.conversation.domain.ConversationPrompt;

import java.text.NumberFormat;

/**
 * A prompt that requires a numeric response from the player.
 */
public abstract class NumericPrompt extends ValidatingPrompt {

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return input.matches("^\\d+(\\.\\d+)?$");
    }

    @Override
    @SneakyThrows
    protected ConversationPrompt acceptValidatedInput(ConversationContext context, String input) {
        return this.acceptValidatedInput(context, NumberFormat.getInstance().parse(input));
    }

    /**
     * Processes the numeric input value.
     *
     * @param context The conversation context
     * @param input The parsed numeric input
     * @return The next prompt
     */
    protected abstract ConversationPrompt acceptValidatedInput(ConversationContext context, Number input);

    @Override
    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return String.format("%s is not a valid number.", invalidInput);
    }

}
