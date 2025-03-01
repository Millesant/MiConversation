package me.millesant.conversation.core.context;

import cn.nukkit.Player;

import lombok.Getter;

import me.millesant.conversation.domain.Conversation;
import me.millesant.conversation.domain.ConversationContext;
import me.millesant.conversation.domain.ConversationPrefix;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public record ConversationContextImpl(Conversation conversation,
                                      Player participant,
                                      Map<Object, Object> data,
                                      ConversationPrefix prefix) implements ConversationContext {

    /**
     * Creates a new conversation context.
     *
     * @param conversation The owning conversation
     * @param participant  The participating player
     * @param data         Initial session data
     * @param prefix       The message prefix
     */
    public ConversationContextImpl(
        Conversation conversation,
        Player participant,
        Map<Object, Object> data,
        ConversationPrefix prefix
    ) {
        this.conversation = conversation;
        this.participant = participant;
        this.data = new HashMap<>(Objects.nonNull(data) ? data : Map.of());
        this.prefix = prefix;
    }

    @Override
    public boolean isModal() {
        return this.conversation().isModal();
    }

    @Override
    public <T> Optional<T> getData(Object key, Class<T> type) {
        return Optional.ofNullable(this.data().get(key))
            .filter(type::isInstance)
            .map(type::cast);
    }

    @Override
    public void setData(Object key, Object value) {
        this.data().put(key, value);
    }

    @Override
    public boolean removeData(Object key) {
        return this.data().keySet().removeIf(key::equals);
    }

}
