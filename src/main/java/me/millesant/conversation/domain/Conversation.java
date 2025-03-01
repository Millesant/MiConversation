package me.millesant.conversation.domain;

import cn.nukkit.Player;

import java.util.UUID;

/**
 * Core conversation interface representing an interactive dialog with a player.
 */
public interface Conversation {

    /**
     * Gets the unique identifier for this conversation.
     *
     * @return Conversation UUID
     */
    UUID getId();

    /**
     * Gets the player participating in this conversation.
     *
     * @return The player
     */
    Player getParticipant();

    /**
     * Gets the prefix for this conversation.
     *
     * @return The plugin
     */
    ConversationPrefix getPrefix();

    /**
     * Gets the conversation context holding state data.
     *
     * @return The conversation context
     */
    ConversationContext getContext();

    /**
     * Begins this conversation.
     */
    void begin();

    /**
     * Processes input from the player.
     *
     * @param input The text input from the player
     */
    void acceptInput(String input);

    /**
     * Abandons this conversation.
     */
    void abandon();

    /**
     * Checks if this conversation has been abandoned.
     *
     * @return true if abandoned, false otherwise
     */
    boolean isAbandoned();

    /**
     * Checks if this conversation is modal.
     *
     * @return true if modal, false otherwise
     */
    boolean isModal();

    /**
     * Gets the current prompt in the conversation.
     *
     * @return The current prompt
     */
    ConversationPrompt getCurrentPrompt();

    /**
     * Checks if this conversation has local echo enabled.
     *
     * @return true if local echo is enabled, false otherwise
     */
    boolean isLocalEchoEnabled();

}
