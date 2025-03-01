package me.millesant.conversation.core.builder;

import cn.nukkit.Player;

import cn.nukkit.plugin.Plugin;

import lombok.Getter;

import me.millesant.conversation.api.ConversationBuilder;
import me.millesant.conversation.api.ConversationService;

import me.millesant.conversation.core.ConversationImpl;

import me.millesant.conversation.core.canceller.ExactMatchConversationCanceller;
import me.millesant.conversation.core.canceller.InactivityConversationCanceller;

import me.millesant.conversation.core.context.NullConversationPrefix;

import me.millesant.conversation.domain.Conversation;
import me.millesant.conversation.domain.ConversationCanceller;
import me.millesant.conversation.domain.ConversationPrefix;
import me.millesant.conversation.domain.ConversationPrompt;

import java.util.*;

import java.util.function.Consumer;

@Getter
public class DefaultConversationBuilder implements ConversationBuilder {

    private final Plugin plugin;

    private final ConversationService conversationService;

    private boolean modal = true;

    private boolean localEchoEnabled = true;

    private ConversationPrefix prefix = new NullConversationPrefix();

    private ConversationPrompt firstPrompt = ConversationPrompt.END_OF_CONVERSATION;

    private final Map<Object, Object> data = new HashMap<>();

    private final List<ConversationCanceller> cancellers = new ArrayList<>();

    private final List<Consumer<Conversation>> abandonedCallbacks = new ArrayList<>();

    /**
     * Creates a new conversation builder.
     *
     * @param plugin The owning plugin
     * @param conversationService The conversation service
     */
    public DefaultConversationBuilder(Plugin plugin,
                                      ConversationService conversationService) {
        this.plugin = plugin;
        this.conversationService = conversationService;
    }

    @Override
    public ConversationBuilder modal(boolean modal) {
        this.modal = modal;
        return this;
    }

    @Override
    public ConversationBuilder localEcho(boolean enabled) {
        this.localEchoEnabled = enabled;
        return this;
    }

    @Override
    public ConversationBuilder prefix(ConversationPrefix prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public ConversationBuilder firstPrompt(ConversationPrompt prompt) {
        this.firstPrompt = prompt;
        return this;
    }

    @Override
    public ConversationBuilder withData(Object key, Object value) {
        this.getData().put(key, value);
        return this;
    }

    @Override
    public ConversationBuilder withData(Map<Object, Object> data) {
        this.getData().clear();
        if (Objects.nonNull(data)) {
            this.getData().putAll(data);
        }
        return this;
    }

    @Override
    public ConversationBuilder addCanceller(ConversationCanceller canceller) {
        this.getCancellers().add(canceller);
        return this;
    }

    @Override
    public ConversationBuilder withTimeout(int seconds) {
        return this.addCanceller(new InactivityConversationCanceller(this.getPlugin(), seconds));
    }

    @Override
    public ConversationBuilder withTimeout(int seconds, String timeoutMessage) {
        return this.addCanceller(new InactivityConversationCanceller(this.getPlugin(), seconds, timeoutMessage));
    }

    @Override
    public ConversationBuilder withEscapeSequence(String escapeSequence) {
        return this.addCanceller(new ExactMatchConversationCanceller(escapeSequence));
    }

    @Override
    public ConversationBuilder withEscapeSequence(String escapeSequence, String exitMessage) {
        return this.addCanceller(new ExactMatchConversationCanceller(escapeSequence, exitMessage));
    }

    @Override
    public ConversationBuilder onAbandoned(Consumer<Conversation> callback) {
        this.getAbandonedCallbacks().add(callback);
        return this;
    }

    @Override
    public Conversation build(Player player) {
        // Create the conversation
        var conversation = new ConversationImpl(
            this.getPlugin(),
            player,
            this.getFirstPrompt(),
            new HashMap<>(this.getData()),
            this.getPrefix(),
            this.isModal(),
            this.isLocalEchoEnabled()
        );

        // Add cancellers
        this.getCancellers().forEach(conversationCanceller -> {
            conversation.addCanceller(conversationCanceller.clone());
        });

        // Add abandoned callbacks
        this.getAbandonedCallbacks().forEach(conversationConsumer -> {
            conversation.addAbandonedCallback(event -> conversationConsumer.accept(event.conversation()));
        });

        return conversation;
    }

    @Override
    public Conversation beginConversation(Player player) {
        var conversation = build(player);

        conversationService.trackConversation(conversation);

        // Begin the conversation
        conversation.begin();

        return conversation;
    }

}
