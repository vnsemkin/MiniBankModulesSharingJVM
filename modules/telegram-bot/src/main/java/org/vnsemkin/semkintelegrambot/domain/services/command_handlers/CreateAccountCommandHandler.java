package org.vnsemkin.semkintelegrambot.domain.services.command_handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.account.BotAccountRegistrationService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreateAccountCommandHandler implements CommandHandler {
    private final BotAccountRegistrationService botAccountRegistrationService;
    private final Map<Long, String> messageHandlerServiceMap;

    @Override
    public void handle(@NonNull Message message) {
        messageHandlerServiceMap.remove(message.getChatId());
        botAccountRegistrationService.handle(message);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.CREATE_ACCOUNT.value;
    }
}
