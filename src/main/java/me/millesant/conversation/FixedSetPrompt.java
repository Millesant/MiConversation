package me.millesant.conversation;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class FixedSetPrompt extends ValidatingPrompt {

    private final Set<String> fixedSet;

    public FixedSetPrompt(final String... fixedSet) {
        this.fixedSet = new HashSet<>(Arrays.asList(fixedSet));
    }

    @Override
    protected boolean isInputValid(final ConversationContext context, final String input) {
        return this.getFixedSet().contains(input);
    }

    @Override
    public String getFailedValidationText(ConversationContext context, String invalidInput) {
        return "Please enter one of the provided choices.";
    }

    protected String formatFixedSet() {
        final var formatted = new StringBuilder();
        this.getFixedSet().forEach(choice -> {
            if (!(formatted.isEmpty()))
                formatted.append(", ");

            formatted.append(choice);
        });
        return formatted.toString();
    }

}
