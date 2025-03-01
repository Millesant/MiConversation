package me.millesant.conversation.presentation;

import lombok.Getter;

import me.millesant.conversation.domain.ConversationContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import java.util.stream.Collectors;

@Getter
public abstract class FixedSetPrompt extends ValidatingPrompt {

    private final Set<String> options;

    private final boolean caseSensitive;

    /**
     * Creates a new case-insensitive fixed set prompt.
     *
     * @param options The valid options
     */
    public FixedSetPrompt(String... options) {
        this(false, options);
    }

    /**
     * Creates a new fixed set prompt.
     *
     * @param caseSensitive Whether the options are case-sensitive
     * @param options The valid options
     */
    public FixedSetPrompt(boolean caseSensitive, String... options) {
        this.caseSensitive = caseSensitive;
        this.options = this.isCaseSensitive() ?
            new HashSet<>(Arrays.asList(options)) :
            Arrays.stream(options)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return this.isCaseSensitive() ?
            this.getOptions().contains(input) :
            this.getOptions().contains(input.toLowerCase());
    }

    @Override
    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return String.format("%s is not a valid option. Valid options are: [%s]", invalidInput, this.formatOptions());
    }

    /**
     * Formats the options into a readable list.
     *
     * @return A comma-separated list of options
     */
    protected String formatOptions() {
        return String.join(", ", this.getOptions());
    }

}
