package me.millesant.conversation;

import java.util.Objects;

public abstract class ValidatingPrompt implements ConversationPrompt {

    @Override
    public boolean blocksForInput(final ConversationContext context) {
        return true;
    }

    @Override
    public ConversationPrompt acceptInput(final ConversationContext context,
                                          final String input) {
        if (!(this.isInputValid(context, input))) {
            final var failedValidationText = this.getFailedValidationText(context, input);
            if (Objects.nonNull(failedValidationText)) {
                context.getForWhom().sendMessage(context.getConversationPrefix().getPrefix(context) + failedValidationText);
            }
            return this;
        }
        return this.acceptValidatedInput(context, input);
    }

    protected abstract boolean isInputValid(final ConversationContext context, final String input);

    protected abstract ConversationPrompt acceptValidatedInput(final ConversationContext context, final String input);

    protected String getFailedValidationText(final ConversationContext context, final String input) {
        return String.format("An invalid input was provided: %s. Please try again.", input);
    }

}
