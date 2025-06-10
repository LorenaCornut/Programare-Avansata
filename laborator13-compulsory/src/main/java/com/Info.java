package com;

import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class Info {
    private ResourceBundle messages;

    public Info(ResourceBundle messages) {
        this.messages = messages;
    }

    public void execute(Locale locale) {
        System.out.println(
            MessageFormat.format(messages.getString("info"), locale.getDisplayName())
        );
        
        // Country information
        System.out.println("Country: " + locale.getDisplayCountry() + 
                         " (" + locale.getDisplayCountry(locale) + ")");
        
        // Language information
        System.out.println("Language: " + locale.getDisplayLanguage() + 
                         " (" + locale.getDisplayLanguage(locale) + ")");
        
        // Currency information
        try {
            Currency currency = Currency.getInstance(locale);
            System.out.println("Currency: " + currency.getCurrencyCode() + 
                             " (" + currency.getDisplayName(locale) + ")");
        } catch (IllegalArgumentException e) {
            System.out.println("Currency: Not available for this locale");
        }
        
        // Week days
        DateFormatSymbols dfs = new DateFormatSymbols(locale);
        String[] weekdays = dfs.getWeekdays();
        System.out.print("Week Days: ");
        for (int i = 1; i < weekdays.length; i++) {
            System.out.print(weekdays[i]);
            if (i < weekdays.length - 1) System.out.print(", ");
        }
        System.out.println();
        
        // Months (corectat - ignoră ultima intrare dacă e goală)
        String[] months = dfs.getMonths();
        System.out.print("Months: ");
        for (int i = 0; i < months.length - 1; i++) {
            if (!months[i].isEmpty()) {
                System.out.print(months[i]);
                if (i < months.length - 2) System.out.print(", ");
            }
        }
        System.out.println();
        
        // Today's date
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", locale);
        System.out.println("Today: " + today.format(DateTimeFormatter.ISO_DATE) + 
                         " (" + today.format(formatter) + ")");
    }
}