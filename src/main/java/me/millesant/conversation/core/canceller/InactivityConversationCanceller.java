package me.millesant.conversation.core.canceller;

import cn.nukkit.plugin.Plugin;

import cn.nukkit.scheduler.TaskHandler;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import me.millesant.conversation.domain.Conversation;
import me.millesant.conversation.domain.ConversationCanceller;
import me.millesant.conversation.domain.ConversationContext;

import java.util.Objects;

/**
 * Cancels a conversation when the player is inactive for too long.
 */
@Getter
@Setter
public class InactivityConversationCanceller implements ConversationCanceller {

    private final Plugin plugin;

    private final int timeoutSeconds;

    private Conversation conversation;

    private TaskHandler taskHandler;

    private String timeoutMessage;

    /**
     * Creates a new inactivity canceller.
     *
     * @param plugin The owning plugin
     * @param timeoutSeconds The timeout in seconds
     */
    public InactivityConversationCanceller(Plugin plugin, int timeoutSeconds) {
        this(plugin, timeoutSeconds, "You have been inactive for too long. The conversation has been cancelled.");
    }

    public InactivityConversationCanceller(Plugin plugin, int timeoutSeconds, String timeoutMessage) {
        this.plugin = plugin;
        this.timeoutSeconds = timeoutSeconds;
        this.timeoutMessage = timeoutMessage;
    }

    @Override
    public void attach(Conversation conversation) {
        this.conversation = conversation;

        if (Objects.nonNull(conversation))
            this.startTimer();
    }

    @Override
    public boolean cancelBasedOnInput(ConversationContext context, String input) {
        this.resetTimer();
        return false;
    }

    @Override
    @SneakyThrows
    public ConversationCanceller clone() {
        if (super.clone() instanceof InactivityConversationCanceller canceller) {
            canceller.setConversation(this.getConversation());
            return canceller;
        }
        throw new IllegalStateException("Failed to clone canceller");
    }

    /**
     * Starts the inactivity timer.
     */
    public void startTimer() {
        if (Objects.isNull(this.getConversation()) || this.getConversation().isAbandoned())
            return;

        this.taskHandler = this.getPlugin().getServer().getScheduler().scheduleDelayedTask(this.getPlugin(), () -> {
            if (Objects.isNull(this.getConversation()) || this.getConversation().isAbandoned())
                return;

            this.getConversation().getParticipant().sendMessage(this.getConversation().getPrefix().getPrefix(this.getConversation().getContext()) + this.getTimeoutMessage());
            this.getConversation().abandon();

        }, this.getTimeoutSeconds() * 20);
    }

    /**
     * Resets the inactivity timer.
     */
    private void resetTimer() {
        if (Objects.isNull(this.getTaskHandler()))
            return;

        this.getTaskHandler().cancel();
        this.setTaskHandler(null);

        this.startTimer();
    }

}
