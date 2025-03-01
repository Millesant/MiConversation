package me.millesant;

import cn.nukkit.Player;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import cn.nukkit.plugin.PluginBase;

import cn.nukkit.plugin.service.ServicePriority;

import lombok.Getter;

import me.millesant.conversation.api.ConversationService;

import me.millesant.conversation.core.ConversationServiceImpl;

import me.millesant.conversation.domain.ConversationContext;
import me.millesant.conversation.domain.ConversationPrompt;

@Getter
public final class MiConversation extends PluginBase {

    private ConversationService conversationService;

    @Override
    public void onLoad() {
        this.getServer().getServiceManager().register(ConversationService.class, new ConversationServiceImpl(this), this, ServicePriority.NORMAL);
    }

    @Override
    public void onEnable() {
        this.conversationService = this.getServer().getServiceManager().getProvider(ConversationService.class).getProvider();
        this.getConversationService().registerEvents();
    }

}
