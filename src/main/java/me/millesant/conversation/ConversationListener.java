package me.millesant.conversation;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;

public record ConversationListener(
    Conversation conversation
) implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(final PlayerChatEvent event) {
        final var player = event.getPlayer();

        if (!(player.equals(conversation.getForWhom())))
            return;

        if (conversation.isAbandoned())
            return;

        if (conversation.isModal())
            event.setCancelled();

        conversation.acceptInput(event.getMessage());
    }

}
