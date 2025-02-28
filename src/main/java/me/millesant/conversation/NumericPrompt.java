package me.millesant.conversation;

import java.text.NumberFormat;
import java.text.ParseException;

public abstract class NumericPrompt extends ValidatingPrompt {

    @Override
    protected boolean isInputValid(final ConversationContext context, final String input) {
        return input.matches("^\\d+(\\.\\d+)?$");
    }

    @Override
    protected ConversationPrompt acceptValidatedInput(final ConversationContext context, final String input) {
        try {
            return this.acceptValidatedInput(context, NumberFormat.getInstance().parse(input));
        } catch (ParseException e) {
            return this.acceptValidatedInput(context, 0);
        }
    }

    protected abstract ConversationPrompt acceptValidatedInput(final ConversationContext context, final Number input);

}
