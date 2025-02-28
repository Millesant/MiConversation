package me.millesant.conversation;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.TaskHandler;

import lombok.Getter;

import java.util.Objects;

@Getter
public class InactivityConversationCanceller implements ConversationCanceller {

    protected Plugin plugin;

    protected int timeoutSeconds;

    protected Conversation conversation;

    protected TaskHandler taskHandler;

    public InactivityConversationCanceller(final Plugin plugin,
                                           final int timeoutSeconds) {
        this.plugin = plugin;
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    public void setConversation(final Conversation conversation) {
        this.conversation = conversation;
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
        return new InactivityConversationCanceller(plugin, timeoutSeconds);
    }

    public void startTimer() {
        this.taskHandler = this.getPlugin().getServer().getScheduler().scheduleDelayedTask(this.getPlugin(), () -> {
            if (Objects.isNull(this.getConversation()))
                return;

            this.getConversation().abandon(new ConversationAbandonedEvent(this.getConversation(), this));
        }, timeoutSeconds * 20);
    }

    public void stopTimer() {
        if (Objects.isNull(this.getTaskHandler()))
            return;

        this.getTaskHandler().cancel();
    }

}
