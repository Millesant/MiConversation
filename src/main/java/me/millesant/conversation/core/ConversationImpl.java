package me.millesant.conversation.core;

import cn.nukkit.Player;

import cn.nukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;

import me.millesant.conversation.core.canceller.InactivityConversationCanceller;
import me.millesant.conversation.core.canceller.ManuallyAbandonedConversationCanceller;

import me.millesant.conversation.core.context.ConversationContextImpl;

import me.millesant.conversation.domain.*;

import java.util.*;

import java.util.concurrent.CopyOnWriteArrayList;

import java.util.function.Consumer;

/**
 * Implementation of a conversation between a player and the server.
 */
@Getter
@Setter
public class ConversationImpl implements Conversation {

    private final UUID id;

    private final Plugin plugin;

    private final Player participant;

    private final ConversationPrefix prefix;

    private final List<ConversationCanceller> cancellers;

    private final List<Consumer<ConversationAbandonedEvent>> abandonedCallbacks;

    private final ConversationContext context;

    private ConversationPrompt currentPrompt;

    private boolean modal;

    private boolean localEchoEnabled;

    private boolean abandoned;

    private boolean firstPrompt;

    public ConversationImpl(
        Plugin plugin,
        Player participant,
        ConversationPrompt firstPrompt,
        Map<Object, Object> initialData,
        ConversationPrefix prefix,
        boolean modal,
        boolean localEchoEnabled
    ) {
        this.id = UUID.randomUUID();

        this.plugin = plugin;
        this.participant = participant;
        this.prefix = prefix;
        this.currentPrompt = firstPrompt;
        this.modal = modal;
        this.localEchoEnabled = localEchoEnabled;

        this.cancellers = new ArrayList<>();
        this.abandonedCallbacks = new CopyOnWriteArrayList<>();

        this.abandoned = false;
        this.firstPrompt = true;

        // Create the conversation context
        this.context = new ConversationContextImpl(
            this,
            this.getParticipant(),
            initialData,
            this.getPrefix()
        );
    }

    @Override
    public void begin() {
        if (Objects.isNull(this.getCurrentPrompt())) {
            this.abandon(ConversationAbandonedEvent.gracefulExit(this));
            return;
        }
        this.outputNextPrompt();
    }

    @Override
    public void acceptInput(String input) {
        if (this.isAbandoned()) {
            return;
        }
        for (final var conversationCanceller : this.getCancellers()) {
            if (conversationCanceller.cancelBasedOnInput(this.getContext(), input)) {
                this.setAbandoned(true);
                this.abandon(ConversationAbandonedEvent.cancelled(this, conversationCanceller));
                return;
            }
        }
        if (this.isLocalEchoEnabled()) {
            this.getParticipant().sendMessage(this.getPrefix().getPrefix(context) + input);
        }

        this.setCurrentPrompt(this.getCurrentPrompt().acceptInput(this.getContext(), input));
        this.outputNextPrompt();
    }

    @Override
    public void abandon() {
        this.abandon(ConversationAbandonedEvent.cancelled(this, new ManuallyAbandonedConversationCanceller()));
    }

    /**
     * Abandons this conversation with event details.
     *
     * @param event The abandonment event details
     */
    private void abandon(ConversationAbandonedEvent event) {
        if (!(this.isAbandoned())) {
            this.setAbandoned(true);

            // Notify all callbacks
            this.getAbandonedCallbacks().forEach(callback -> callback.accept(event));
        }
    }

    /**
     * Outputs the next prompt to the player.
     */
    private void outputNextPrompt() {
        // End conversation if we've reached the end
        if (Objects.isNull(this.getCurrentPrompt())) {
            this.abandon(ConversationAbandonedEvent.gracefulExit(this));
            return;
        }

        // Get and send the prompt text
        var promptText = this.getCurrentPrompt().getPromptText(this.getContext());

        if (Objects.nonNull(promptText) && (!(promptText.isBlank()))) {
            this.getParticipant().sendMessage(this.getPrefix().getPrefix(this.getContext()) + promptText);
        }

        // Skip prompts that don't block for input
        if (!(this.getCurrentPrompt().blocksForInput(this.getContext()))) {
            this.setFirstPrompt(false);
            this.acceptInput("");
            return;
        }

        this.setFirstPrompt(false);
    }

    /**
     * Adds a conversation canceller.
     *
     * @param canceller The canceller to add
     */
    public void addCanceller(ConversationCanceller canceller) {
        canceller.attach(this);
        this.getCancellers().add(canceller);
    }

    /**
     * Adds a callback for when the conversation is abandoned.
     *
     * @param callback The callback to add
     */
    public void addAbandonedCallback(Consumer<ConversationAbandonedEvent> callback) {
        this.getAbandonedCallbacks().add(callback);
    }

}
