package com;

import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;

public class SetLocale {
    private ResourceBundle messages;

    public SetLocale(ResourceBundle messages) {
        this.messages = messages;
    }

    public void execute(String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        Locale.setDefault(locale);
        System.out.println(
            MessageFormat.format(messages.getString("locale.set"), locale.getDisplayName())
        );
    }
}

    