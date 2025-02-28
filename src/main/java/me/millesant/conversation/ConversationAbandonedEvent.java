package me.millesant.conversation;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ConversationAbandonedEvent {

    private final Conversation conversation;

    private final ConversationCanceller canceller;

    public ConversationAbandonedEvent(final Conversation conversation) {
        this(conversation, null);
    }

    public ConversationAbandonedEvent(final Conversation conversation,
                                      final ConversationCanceller canceller) {
        this.conversation = conversation;
        this.canceller = canceller;
    }

    public boolean gracefulExit() {
        return Objects.isNull(this.getCanceller());
    }

    public ConversationContext getContext() {
        return this.getConversation().getConversationContext();
    }

}
