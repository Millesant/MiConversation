package me.millesant.conversation.infrastructure.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;

import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

import me.millesant.conversation.api.ConversationService;

public record ConversationListener(
    ConversationService conversationService
) implements Listener {

    /**
     * Handles player chat events to route them to active conversations.
     *
     * @param event The player chat event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void on(PlayerChatEvent event) {
        final var player = event.getPlayer();

        this.conversationService().getActiveConversation(player).ifPresent(conversation -> {
            if (!(player.equals(conversation.getParticipant()))) {
                return;
            }

            if (conversation.isAbandoned()) {
                // Remove abandoned conversation from active list
                this.conversationService().abandonConversations(player);
                return;
            }

            // If the conversation is modal, cancel the chat event
            if (conversation.isModal()) {
                event.setCancelled();
            }

            // Pass the input to the conversation
            conversation.acceptInput(event.getMessage());
        });
    }

    /**
     * Handles player quit events to clean up conversations.
     *
     * @param event The player quit event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void on(PlayerQuitEvent event) {
        this.conversationService().abandonConversations(event.getPlayer());
    }

}
