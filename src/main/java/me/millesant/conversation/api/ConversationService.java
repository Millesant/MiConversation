package me.millesant.conversation.api;

import cn.nukkit.Player;

import me.millesant.conversation.domain.Conversation;

import java.util.Optional;

/**
 * Main service interface for managing conversations with players.
 * Provides entry points for creating, managing, and tracking conversations.
 */
public interface ConversationService {

    /**
     * Creates a new conversation builder for configuring conversations.
     *
     * @return A fresh conversation builder instance.
     */
    ConversationBuilder createConversationBuilder();

    /**
     * Retrieves any active conversation for a player.
     *
     * @param player The player to check
     * @return The active conversation or empty if none exists
     */
    Optional<Conversation> getActiveConversation(Player player);

    /**
     * Abandons all active conversations for a player.
     *
     * @param player The player whose conversations should be abandoned
     */
    void abandonConversations(Player player);

    /**
     * Checks if a player is currently engaged in a conversation.
     *
     * @param player The player to check
     * @return true if the player has an active conversation, false otherwise
     */
    boolean isConversing(Player player);

    /**
     * Register for the plugin's events automatically.
     * This should be called during plugin startup.
     */
    void registerEvents();

    /**
     * Registers a conversation as active.
     *
     * @param conversation The conversation to register
     */
    public void trackConversation(Conversation conversation);

}
