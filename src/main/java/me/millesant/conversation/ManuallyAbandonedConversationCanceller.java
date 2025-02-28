package me.millesant.conversation;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManuallyAbandonedConversationCanceller implements ConversationCanceller {

    private Conversation conversation;

    @Override
    public boolean cancelBasedOnInput(final ConversationContext context,
                                      final String input) {
        return false;
    }

    @Override
    public ConversationCanceller clone() {
        return new ManuallyAbandonedConversationCanceller();
    }

}
