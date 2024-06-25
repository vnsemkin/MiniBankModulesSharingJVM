package org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.account;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.vnsemkin.semkintelegrambot.application.constants.CommandToServiceMap;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountRegistrationRequest;
import org.vnsemkin.semkintelegrambot.application.dtos.AccountRegistrationResponse;
import org.vnsemkin.semkintelegrambot.application.externals.AppWebClient;
import org.vnsemkin.semkintelegrambot.application.externals.TgSenderInterface;
import org.vnsemkin.semkintelegrambot.domain.models.Result;
import org.vnsemkin.semkintelegrambot.domain.services.reply_handlers.MessageHandler;

@Service
@RequiredArgsConstructor
public class BotAccountRegistrationService implements MessageHandler {
    private final static String ACCOUNT_CREATED = "Счет успешно открыт ";
    private final static String SMT_WRONG = "Что-то пошло не так. Попробуйте позднее.";
    private final static String NEW_LINE = "\n";
    private final AppWebClient appWebClient;
    private final TgSenderInterface sender;

    @Override
    public void handle(@NonNull Message message) {
        long chatId = message.getChatId();
        Result<AccountRegistrationResponse, String> accountRegistrationResult =
            appWebClient.registerAccount(new AccountRegistrationRequest(chatId));
        String result = accountRegistrationResult.isSuccess() ?
            accountCreationSuccess(accountRegistrationResult) : accountCreationError(accountRegistrationResult);
        sender.sendText(chatId, result);
    }

    private String accountCreationSuccess(@NonNull Result<AccountRegistrationResponse, String> result) {
        return result.getData().map(account -> ACCOUNT_CREATED +
                NEW_LINE +
                account.accountName() +
                NEW_LINE +
                account.balance() +
                NEW_LINE +
                account.info())
            .orElse(SMT_WRONG);
    }

    private String accountCreationError(@NonNull Result<AccountRegistrationResponse, String> result) {
        return result.getError().orElse(SMT_WRONG);
    }

    @Override
    public String getHandlerName() {
        return CommandToServiceMap.CREATE_ACCOUNT.value;
    }
}