package me.millesant.conversation;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Conversation {

    private final Plugin plugin;

    private final Player forWhom;

    private final Map<Object, Object> sessionData;

    private ConversationPrefix prefix;

    private final List<ConversationCanceller> cancellers;

    private boolean modal;

    private boolean localEchoEnabled;

    private ConversationContext conversationContext;

    private boolean abandoned;

    private ConversationPrompt currentPrompt;

    private boolean firstPrompt;

    private final List<ConversationAbandonedListener> abandonedListeners;

    public Conversation(final Plugin plugin,
                        final Player forWhom,
                        final ConversationPrompt firstPrompt) {
        this(plugin, forWhom, firstPrompt, new HashMap<>());
    }

    public Conversation(final Plugin plugin,
                        final Player forWhom,
                        final ConversationPrompt firstPrompt,
                        final Map<Object, Object> sessionData) {
        this.plugin = plugin;
        this.forWhom = forWhom;
        this.sessionData = sessionData;
        this.prefix = new NullConversationPrefix();
        this.modal = true;
        this.localEchoEnabled = true;
        this.cancellers = new ArrayList<>();
        this.abandonedListeners = new ArrayList<>();
        this.currentPrompt = firstPrompt;
        this.firstPrompt = true;
        this.abandoned = false;
    }

    public Conversation setModal(final boolean modal) {
        this.modal = modal;
        return this;
    }

    public Conversation setLocalEchoEnabled(final boolean localEchoEnabled) {
        this.localEchoEnabled = localEchoEnabled;
        return this;
    }

    public Conversation setConversationPrefix(final ConversationPrefix prefix) {
        this.prefix = prefix;
        return this;
    }

    public Conversation addConversationCanceller(final ConversationCanceller canceller) {
        this.getCancellers().add(canceller);
        return this;
    }

    public Conversation addConversationAbandonedListener(final ConversationAbandonedListener listener) {
        this.getAbandonedListeners().add(listener);
        return this;
    }

    public void begin() {
        if (!(Objects.isNull(this.getCurrentPrompt()))) {
            this.conversationContext = new ConversationContext(
                this.getPlugin(),
                this.getForWhom(),
                this.getSessionData(),
                this.getPrefix(),
                this.isModal()
            );

            this.getPlugin().getServer().getPluginManager().registerEvents(new ConversationListener(this), this.getPlugin());

            this.outputNextPrompt();
            return;
        }

        this.abandon(new ConversationAbandonedEvent(this, new ManuallyAbandonedConversationCanceller()));
    }

    public void acceptInput(final String input) {
        if (this.isAbandoned())
            return;

        for (final var conversationCanceller : this.getCancellers()) {
            if (conversationCanceller.cancelBasedOnInput(this.getConversationContext(), input)) {
                this.setAbandoned(true);
                this.abandon(new ConversationAbandonedEvent(this, conversationCanceller));
                return;
            }
        }

        if (this.isLocalEchoEnabled())
            this.getForWhom().sendMessage(this.getPrefix().getPrefix(conversationContext) + input);

        final ConversationPrompt nextPrompt = this.getCurrentPrompt().acceptInput(this.getConversationContext(), input);
        this.setCurrentPrompt(nextPrompt);

        this.outputNextPrompt();
    }

    public void outputNextPrompt() {
        if (Objects.isNull(this.getCurrentPrompt())) {
            this.abandon(new ConversationAbandonedEvent(this));
            return;
        }

        final String promptText = this.getCurrentPrompt().getPromptText(this.getConversationContext());

        if (Objects.nonNull(promptText) && !promptText.isEmpty())
            this.getForWhom().sendMessage(this.getPrefix().getPrefix(this.getConversationContext()) + promptText);

        this.setFirstPrompt(false);

        if (!(this.getCurrentPrompt().blocksForInput(this.getConversationContext())))
            this.acceptInput("");
    }

    public void abandon(ConversationAbandonedEvent details) {
        if (this.isAbandoned())
            return;

        this.setAbandoned(true);
        this.getAbandonedListeners().forEach(conversationAbandonedListener -> conversationAbandonedListener.conversationAbandoned(details));
    }

    public void abandon() {
        this.abandon(new ConversationAbandonedEvent(this, new ManuallyAbandonedConversationCanceller()));
    }

}
