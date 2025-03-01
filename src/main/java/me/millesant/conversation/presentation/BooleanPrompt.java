package me.millesant.conversation.presentation;

import me.millesant.conversation.domain.ConversationContext;
import me.millesant.conversation.domain.ConversationPrompt;

import java.util.Set;

/**
 * A prompt that requires a yes/no response from the player.
 */
public abstract class BooleanPrompt extends ValidatingPrompt {

    private static final Set<String> TRUE_INPUTS = Set.of("true", "yes", "y", "on", "1");

    private static final Set<String> FALSE_INPUTS = Set.of("false", "no", "n", "off", "0");

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return TRUE_INPUTS.contains(input.toLowerCase()) || FALSE_INPUTS.contains(input.toLowerCase());
    }

    @Override
    protected ConversationPrompt acceptValidatedInput(ConversationContext context, String input) {
        return this.acceptValidatedInput(context, TRUE_INPUTS.contains(input.toLowerCase()));
    }

    /**
     * Processes the boolean input value.
     *
     * @param context The conversation context
     * @param input The parsed boolean input
     * @return The next prompt
     */
    protected abstract ConversationPrompt acceptValidatedInput(ConversationContext context, boolean input);

    @Override
    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return String.format("%s is not a valid boolean. Valid inputs are: [%s] and [%s]", invalidInput, String.join(", ", TRUE_INPUTS), FALSE_INPUTS);
    }
}
