package com.mrrezik.iEmoji;

import net.md_5.bungee.api.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    // Паттерн для поиска &#RRGGBB
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String colorize(String message) {
        if (message == null) return "";

        // Сначала обрабатываем HEX (для версий 1.16+)
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            try {
                String hexCode = matcher.group(1);
                matcher.appendReplacement(buffer, ChatColor.of("#" + hexCode).toString());
            } catch (NoSuchMethodError e) {
                // Если сервер старый (до 1.16) и не поддерживает ChatColor.of,
                // просто убираем hex код или заменяем на пустоту
                matcher.appendReplacement(buffer, "");
            }
        }
        matcher.appendTail(buffer);

        // Затем обрабатываем стандартные цвета &
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
}