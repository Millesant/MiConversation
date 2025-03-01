package me.millesant.conversation.core.canceller;

import lombok.SneakyThrows;

import me.millesant.conversation.domain.Conversation;
import me.millesant.conversation.domain.ConversationCanceller;
import me.millesant.conversation.domain.ConversationContext;

/**
 * A canceller for conversations that are manually abandoned.
 */
public class ManuallyAbandonedConversationCanceller implements ConversationCanceller {

    @Override
    public void attach(Conversation conversation) {
        // No action needed
    }

    @Override
    public boolean cancelBasedOnInput(ConversationContext context, String input) {
        return false; // Never cancels based on input
    }

    @Override
    @SneakyThrows
    public ConversationCanceller clone() {
        if (super.clone() instanceof ManuallyAbandonedConversationCanceller canceller) {
            return canceller;
        }
        throw new IllegalStateException("Failed to clone canceller");
    }

}
