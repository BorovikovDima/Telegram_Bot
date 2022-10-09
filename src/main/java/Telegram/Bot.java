package Telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public static void main( String[] args ) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try{// регистрация Бота
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {// Обработка исключений
            throw new RuntimeException(e);
        }
    }
    public void sendMsg(Message message, String text){// Метод в котором описывается что будет отвечать бот
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());// Установка ID чтобы бот понимал кому отвечать
        sendMessage.setReplyToMessageId(message.getMessageId());// На какое конкретное сообщение бот должен ответить
        sendMessage.setText(text);
        try{// Отправка сообщения
            setButtons(sendMessage);
            execute(sendMessage);

        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public String getBotUsername() {
        return "TestBot";
    }

    public String getBotToken() {
        //String botToken = System.getenv("TELEGRAM_BOT_TOKEN");
        return "5668598602:AAGi8BTgqW26fczSl3zGpopQayqAueYNHNo";
    }

    public void onUpdateReceived(Update update) {// Метод для приема сообщений
        Message message = update.getMessage();// Описываем что делать при получении сообщения
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    sendMsg(message,"Привет я бот, который покажет вам какие мероприятия есть в городе");
                    sendMsg(message,"Пока что мой функционал беден и доступна только одна команда:/help");
                break;
                case "/help":
                    sendMsg(message, "Я умею искать доступные мероприятия в городе по выбранной дате");
                    break;
                default:
                    sendMsg(message, "Я не понимаю что вы отправили, чтобы начать работу со мной пожалуйста воспользуйтесь командой /start");
            }
        }
    }
    public void setButtons(SendMessage sendMessage){ //Реализация клавиатуры(чтобы та отображалась ввиде кнопочек снизу)
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}
