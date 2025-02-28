package me.millesant.conversation;

import cn.nukkit.Player;

import cn.nukkit.plugin.Plugin;

import lombok.Getter;

import java.util.*;

@Getter
public class ConversationBuilder {

    private final Plugin plugin;

    private boolean modal = true;

    private boolean localEchoEnabled = true;

    private ConversationPrefix prefix = new NullConversationPrefix();

    private ConversationPrompt firstPrompt = ConversationPrompt.END_OF_CONVERSATION;

    private final Map<Object, Object> sessionData = new HashMap<>();

    private final List<ConversationCanceller> cancellers = new ArrayList<>();

    private final List<ConversationAbandonedListener> abandonedListeners = new ArrayList<>();

    public ConversationBuilder(final Plugin plugin) {
        this.plugin = plugin;
    }

    public ConversationBuilder modal(final boolean modal) {
        this.modal = modal;
        return this;
    }

    public ConversationBuilder localEcho(final boolean enabled) {
        this.localEchoEnabled = enabled;
        return this;
    }

    public ConversationBuilder prefix(final ConversationPrefix prefix) {
        this.prefix = prefix;
        return this;
    }

    public ConversationBuilder firstPrompt(final ConversationPrompt prompt) {
        this.firstPrompt = prompt;
        return this;
    }

    public ConversationBuilder addSessionData(final Object key, Object value) {
        this.getSessionData().put(key, value);
        return this;
    }

    public ConversationBuilder withSessionData(final Map<Object, Object> sessionData) {
        this.getSessionData().clear();
        if (Objects.nonNull(sessionData)) {
            this.getSessionData().putAll(sessionData);
        }
        return this;
    }

    public ConversationBuilder addCanceller(final ConversationCanceller canceller) {
        this.getCancellers().add(canceller);
        return this;
    }

    public ConversationBuilder timeout(final int seconds) {
        return this.addCanceller(new InactivityConversationCanceller(plugin, seconds));
    }

    public ConversationBuilder timeout(final int seconds, final String timeoutMessage) {
        return this.addCanceller(new InactivityConversationCanceller(plugin, seconds, timeoutMessage));
    }

    public ConversationBuilder escapeSequence(final String escapeSequence) {
        return this.addCanceller(new ExactMatchConversationCanceller(escapeSequence));
    }

    public ConversationBuilder escapeSequence(final String escapeSequence, final String exitMessage) {
        return this.addCanceller(new ExactMatchConversationCanceller(escapeSequence, exitMessage));
    }

    public ConversationBuilder abandonListener(final ConversationAbandonedListener listener) {
        this.getAbandonedListeners().add(listener);
        return this;
    }

    public Conversation build(final Player player) {
        final var conversation = new Conversation(
            this.getPlugin(),
            player,
            this.getFirstPrompt(),
            this.getSessionData()
        );

        this.getCancellers().forEach(conversation::addConversationCanceller);

        this.getAbandonedListeners().forEach(conversation::addConversationAbandonedListener);

        return conversation;
    }

    public Conversation beginConversation(final Player player) {
        final var conversation = this.build(player);
        conversation.begin();
        return conversation;
    }

}
