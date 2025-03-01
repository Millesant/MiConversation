package me.millesant.conversation.core.canceller;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import me.millesant.conversation.domain.Conversation;
import me.millesant.conversation.domain.ConversationCanceller;
import me.millesant.conversation.domain.ConversationContext;

/**
 * Cancels a conversation when the player inputs an exact matching string.
 */
@Getter
@Setter
public class ExactMatchConversationCanceller implements ConversationCanceller {

    private final String escapeSequence;

    protected String exitMessage;

    private Conversation conversation;

    public ExactMatchConversationCanceller(String escapeSequence) {
        this(escapeSequence, "You have cancelled the conversation.");
    }

    /**
     * Creates a new exact match canceller.
     *
     * @param escapeSequence The input text that triggers cancellation
     * @param exitMessage The message sent to the player upon cancellation
     */
    public ExactMatchConversationCanceller(String escapeSequence, String exitMessage) {
        this.escapeSequence = escapeSequence;
        this.exitMessage = exitMessage;
    }

    @Override
    public void attach(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public boolean cancelBasedOnInput(ConversationContext context, String input) {
        if (this.getEscapeSequence().equalsIgnoreCase(input)) {
            this.getConversation().getParticipant().sendMessage(this.getConversation().getPrefix().getPrefix(context) + this.getExitMessage());
            return true;
        }
        return false;
    }

    @Override
    @SneakyThrows
    public ConversationCanceller clone() {
        if (super.clone() instanceof ExactMatchConversationCanceller canceller) {
            canceller.setConversation(this.getConversation());
            return canceller;
        }
        throw new IllegalStateException("Failed to clone canceller");
    }

}
