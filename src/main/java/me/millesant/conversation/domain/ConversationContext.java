package me.millesant.conversation.domain;

import cn.nukkit.Player;

import java.util.Map;
import java.util.Optional;

/**
 * Holds conversation state and context information.
 */
public interface ConversationContext {
    /**
     * Gets the player for whom this conversation is mediating.
     *
     * @return The player
     */
    Player participant();

    /**
     * Gets the parent conversation.
     *
     * @return The conversation
     */
    Conversation conversation();

    /**
     * Gets the conversation prefix.
     *
     * @return The prefix
     */
    ConversationPrefix prefix();

    /**
     * Gets whether this conversation is modal.
     *
     * @return true if modal, false otherwise
     */
    boolean isModal();

    /**
     * Gets an unmodifiable view of all session data.
     *
     * @return Unmodifiable map of all session data
     */
    Map<Object, Object> data();

    /**
     * Gets a specific session data value.
     *
     * @param key The key to look up
     * @param <T> The expected type of the value
     * @return Optional containing the value if found and of the expected type
     */
    <T> Optional<T> getData(Object key, Class<T> type);

    /**
     * Sets a session data value.
     *
     * @param key The key to store
     * @param value The value to store
     */
    void setData(Object key, Object value);

    /**
     * Removes a session data value.
     *
     * @param key The key to remove
     * @return true if the key was present, false otherwise
     */
    boolean removeData(Object key);

}
