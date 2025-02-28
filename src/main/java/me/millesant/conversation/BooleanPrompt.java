package me.millesant.conversation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BooleanPrompt extends ValidatingPrompt {

    protected static final List<String> TRUE_INPUTS = new ArrayList<String>(Arrays.asList(new String[]{"true", "yes", "y", "on"}));

    protected static final List<String> FALSE_INPUTS = new ArrayList<String>(Arrays.asList(new String[]{"false", "no", "n", "off"}));

    @Override
    protected boolean isInputValid(final ConversationContext context, final String input) {
        return TRUE_INPUTS.contains(input.toLowerCase()) || FALSE_INPUTS.contains(input.toLowerCase());
    }

    @Override
    protected ConversationPrompt acceptValidatedInput(final ConversationContext context, final String input) {
        return this.acceptValidatedInput(context, TRUE_INPUTS.contains(input.toLowerCase()));
    }

    protected abstract ConversationPrompt acceptValidatedInput(final ConversationContext context, final boolean input);

}
