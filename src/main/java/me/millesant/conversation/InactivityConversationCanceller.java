package me.millesant.conversation;

import cn.nukkit.plugin.Plugin;

import cn.nukkit.scheduler.TaskHandler;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class InactivityConversationCanceller implements ConversationCanceller {

    protected Plugin plugin;

    protected int timeoutSeconds;

    protected Conversation conversation;

    @Setter
    protected TaskHandler taskHandler;

    protected String timeoutMessage;

    public InactivityConversationCanceller(final Plugin plugin,
                                           final int timeoutSeconds) {
        this(plugin, timeoutSeconds, String.format("Conversation timed out after %s seconds of inactivity.", timeoutSeconds));
    }

    public InactivityConversationCanceller(final Plugin plugin,
                                           final int timeoutSeconds,
                                           final String timeoutMessage) {
        this.plugin = plugin;
        this.timeoutSeconds = timeoutSeconds;
        this.timeoutMessage = timeoutMessage;
    }

    @Override
    public void setConversation(final Conversation conversation) {
        this.conversation = conversation;

        if (Objects.nonNull(conversation))
            this.startTimer();
    }

    @Override
    public boolean cancelBasedOnInput(final ConversationContext context,
                                      final String input) {
        this.stopTimer();
        this.startTimer();
        return false;
    }

    @Override
    public ConversationCanceller clone() {
        return new InactivityConversationCanceller(plugin, timeoutSeconds, timeoutMessage);
    }

    public void startTimer() {
        if (Objects.isNull(this.getConversation()) || this.getConversation().isAbandoned())
            return;

        this.stopTimer();

        this.taskHandler = this.getPlugin().getServer().getScheduler().scheduleDelayedTask(this.getPlugin(), () -> {
            if (Objects.isNull(this.getConversation()) || this.getConversation().isAbandoned())
                return;

            this.getConversation().getForWhom().sendMessage(
                this.getConversation().getPrefix().getPrefix(this.getConversation().getConversationContext()) +
                    this.getTimeoutMessage()
            );

            this.getConversation().abandon(new ConversationAbandonedEvent(this.getConversation(), this));
        }, this.getTimeoutSeconds() * 20);
    }

    public void stopTimer() {
        if (Objects.isNull(this.getTaskHandler()))
            return;

        this.getTaskHandler().cancel();
        this.setTaskHandler(null);
    }

}
