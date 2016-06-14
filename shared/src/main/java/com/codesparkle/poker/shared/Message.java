package com.codesparkle.poker.shared;

import com.codesparkle.poker.shared.cards.*;

import java.util.*;

public class Message {

    private Map<MessageFormat, String> parts = new HashMap<>();

    public static Message parse(String rawMessage) {
        Message message = new Message();
        for (String pair : rawMessage.split(";")) {
            String[] keyValuePair = pair.split(":");
            MessageFormat key = MessageFormat.fromString(keyValuePair[0]);
            String value = keyValuePair[1];
            message.add(key, value);
        }
        return message;
    }

    public boolean has(MessageFormat key) {
        return parts.containsKey(key);
    }

    public String get(MessageFormat key) {
        return parts.get(key);
    }

    public String[] getMultiple(MessageFormat key) {
        return parts.get(key).split(",");
    }

    public int getInt(MessageFormat key) {
        return Integer.parseInt(get(key));
    }

    public <T extends Enum<T>> T getEnum(MessageFormat key, Class<T> enumClass) {
        return Enum.valueOf(enumClass, get(key));
    }



    public Chips getChips(MessageFormat key) {
        return new Chips(getInt(key));
    }

    public void add(MessageFormat key, Object... values) {
        String joined = SharedHelpers.join(values, ",");
        parts.put(key, joined);
    }

    @Override
    public String toString() {
        String message = "";
        for (MessageFormat key : parts.keySet()) {
            message += String.format("%s:%s;", key, parts.get(key));
        }
        return message;
    }

}
