package me.millesant.conversation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExactMatchConversationCanceller implements ConversationCanceller {

    protected String escapeSequence;

    protected String exitMessage;

    protected Conversation conversation;

    public ExactMatchConversationCanceller(final String escapeSequence) {
        this(escapeSequence, "Conversation ended.");
    }

    public ExactMatchConversationCanceller(final String escapeSequence,
                                           final String exitMessage) {
        this.escapeSequence = escapeSequence;
        this.exitMessage = exitMessage;
    }

    @Override
    public boolean cancelBasedOnInput(final ConversationContext context,
                                      final String input) {
        if (this.getEscapeSequence().equalsIgnoreCase(input)) {
            context.getForWhom().sendMessage(context.getConversationPrefix().getPrefix(context) + this.getExitMessage());
            return true;
        }
        return false;
    }

    @Override
    public ConversationCanceller clone() {
        return new ExactMatchConversationCanceller(this.getEscapeSequence());
    }

}
