package me.millesant.conversation;

import cn.nukkit.Player;
import cn.nukkit.plugin.Plugin;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ConversationContext {

    private final Plugin plugin;

    private final Player forWhom;

    private final Map<Object, Object> sessionData;

    private final ConversationPrefix conversationPrefix;

    private final boolean isModal;

    public ConversationContext(final Plugin plugin,
                               final Player forWhom,
                               final Map<Object, Object> sessionData,
                               final ConversationPrefix conversationPrefix,
                               final boolean isModal) {
        this.plugin = plugin;
        this.forWhom = forWhom;
        this.sessionData = (sessionData != null) ? sessionData : new HashMap<>();
        this.conversationPrefix = conversationPrefix;
        this.isModal = isModal;
    }

    public Object getSessionData(final Object key) {
        return this.getSessionData().get(key);
    }

    public void setSessionData(final Object key,
                               final Object value) {
        this.getSessionData().put(key, value);
    }

}
