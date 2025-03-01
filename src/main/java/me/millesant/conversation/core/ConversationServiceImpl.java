package me.millesant.conversation.core;

import cn.nukkit.Player;

import cn.nukkit.plugin.Plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import me.millesant.conversation.api.ConversationBuilder;
import me.millesant.conversation.api.ConversationService;

import me.millesant.conversation.core.builder.DefaultConversationBuilder;

import me.millesant.conversation.domain.Conversation;

import me.millesant.conversation.infrastructure.listener.ConversationListener;

import java.util.Map;

import java.util.Objects;
import java.util.Optional;

import java.util.concurrent.ConcurrentHashMap;

@Getter
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final Plugin plugin;

    private final Map<Player, Conversation> activeConversations = new ConcurrentHashMap<>();

    @Override
    public ConversationBuilder createConversationBuilder() {
        return new DefaultConversationBuilder(
            this.getPlugin(),
            this
        );
    }

    @Override
    public Optional<Conversation> getActiveConversation(Player player) {
        return Optional.ofNullable(this.getActiveConversations().get(player));
    }

    @Override
    public void abandonConversations(Player player) {
        var conversation = this.getActiveConversations().get(player);
        if (Objects.nonNull(conversation)) {
            conversation.abandon();
            this.getActiveConversations().remove(player);
        }
    }

    @Override
    public boolean isConversing(Player player) {
        return this.getActiveConversations().containsKey(player);
    }

    @Override
    public void registerEvents() {
        this.getPlugin().getServer().getPluginManager().registerEvents(new ConversationListener(this), plugin);
    }

    @Override
    public void trackConversation(Conversation conversation) {
        this.getActiveConversations().put(conversation.getParticipant(), conversation);

        // Add callback to clean up when the conversation ends
        if (conversation instanceof ConversationImpl impl) {
            impl.addAbandonedCallback(event -> {
                this.abandonConversations(impl.getParticipant());
            });
        }
    }

}
