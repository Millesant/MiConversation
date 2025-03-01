package me.millesant.conversation.api;

import cn.nukkit.Player;

import me.millesant.conversation.domain.Conversation;
import me.millesant.conversation.domain.ConversationCanceller;
import me.millesant.conversation.domain.ConversationPrefix;
import me.millesant.conversation.domain.ConversationPrompt;

import java.util.Map;

import java.util.function.Consumer;

/**
 * Builder interface for creating conversation instances with a fluent API.
 */
public interface ConversationBuilder {

    /**
     * Sets whether the conversation is modal, meaning it blocks other input.
     *
     * @param modal true to make the conversation modal, false otherwise
     * @return this builder instance
     */
    ConversationBuilder modal(boolean modal);

    /**
     * Sets whether player input is echoed back to them.
     *
     * @param enabled true to enable local echo, false otherwise
     * @return this builder instance
     */
    ConversationBuilder localEcho(boolean enabled);

    /**
     * Sets the prefix for all messages in the conversation.
     *
     * @param prefix the conversation prefix to use
     * @return this builder instance
     */
    ConversationBuilder prefix(ConversationPrefix prefix);

    /**
     * Sets the first prompt that starts the conversation.
     *
     * @param prompt the first prompt to show
     * @return this builder instance
     */
    ConversationBuilder firstPrompt(ConversationPrompt prompt);

    /**
     * Adds a key-value pair to the initial session data.
     *
     * @param key the key for the data
     * @param value the value for the data
     * @return this builder instance
     */
    ConversationBuilder withData(Object key, Object value);

    /**
     * Sets all initial session data at once.
     *
     * @param sessionData map containing all session data
     * @return this builder instance
     */
    ConversationBuilder withData(Map<Object, Object> sessionData);

    /**
     * Adds a conversation canceller.
     *
     * @param canceller the canceller to add
     * @return this builder instance
     */
    ConversationBuilder addCanceller(ConversationCanceller canceller);

    /**
     * Adds a timeout canceller to automatically end the conversation after inactivity.
     *
     * @param seconds the timeout in seconds
     * @return this builder instance
     */
    ConversationBuilder withTimeout(int seconds);

    /**
     * Adds a timeout canceller with a custom message.
     *
     * @param seconds the timeout in seconds
     * @param timeoutMessage the message to send when the conversation times out
     * @return this builder instance
     */
    ConversationBuilder withTimeout(int seconds, String timeoutMessage);

    /**
     * Adds an escape sequence that lets the user exit the conversation.
     *
     * @param escapeSequence the text that will cancel the conversation
     * @return this builder instance
     */
    ConversationBuilder withEscapeSequence(String escapeSequence);

    /**
     * Adds an escape sequence with a custom exit message.
     *
     * @param escapeSequence the text that will cancel the conversation
     * @param exitMessage the message to send when the conversation is cancelled
     * @return this builder instance
     */
    ConversationBuilder withEscapeSequence(String escapeSequence, String exitMessage);

    /**
     * Adds a callback for when the conversation is abandoned.
     *
     * @param callback the callback to invoke when the conversation ends
     * @return this builder instance
     */
    ConversationBuilder onAbandoned(Consumer<Conversation> callback);

    /**
     * Builds the conversation for the specified player.
     *
     * @param player the player to start a conversation with
     * @return a new Conversation instance
     */
    Conversation build(Player player);

    /**
     * Builds and immediately begins the conversation with the specified player.
     *
     * @param player the player to start a conversation with
     * @return the started Conversation instance
     */
    Conversation beginConversation(Player player);

}
