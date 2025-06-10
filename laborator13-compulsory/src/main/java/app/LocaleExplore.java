package app;

import com.DisplayLocales;
import com.Info;
import com.SetLocale;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LocaleExplore {
    public static void main(String[] args) {
        Locale currentLocale = Locale.getDefault();
        ResourceBundle messages = ResourceBundle.getBundle("Messages", currentLocale);
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print(messages.getString("prompt"));
            String command = scanner.nextLine().trim();
            
            if (command.equalsIgnoreCase("exit")) {
                break;
            }
            
            String[] parts = command.split("\\s+");
            String action = parts[0].toLowerCase();
            
            switch (action) {
                case "displaylocales":
                    new DisplayLocales(messages).execute();
                    break;
                case "setlocale":
                    if (parts.length > 1) {
                        new SetLocale(messages).execute(parts[1]);
                        messages = ResourceBundle.getBundle("Messages", Locale.getDefault());
                    } else {
                        System.out.println("Usage: setlocale <language_tag>");
                    }
                    break;
                case "info":
                    if (parts.length > 1) {
                        try {
                            Locale locale = Locale.forLanguageTag(parts[1]);
                            new Info(messages).execute(locale);
                        } catch (Exception e) {
                            System.out.println("Invalid locale tag");
                        }
                    } else {
                        new Info(messages).execute(Locale.getDefault());
                    }
                    break;
                default:
                    System.out.println(messages.getString("invalid"));
                    break;
            }
        }
        
        scanner.close();
    }
}
