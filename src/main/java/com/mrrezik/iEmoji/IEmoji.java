package com.mrrezik.iEmoji;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class IEmoji extends JavaPlugin implements CommandExecutor {

    private FileConfiguration langConfig;
    private List<String> emojiList;
    private int pageSize;
    private String selectedLang;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        saveResource("languages/en.yml", false);
        saveResource("languages/ru.yml", false);
        saveResource("languages/ua.yml", false);

        loadConfigValues();

        getCommand("emoji").setExecutor(this);
        getLogger().info("IEmoji has been enabled (Blue Theme)!");
    }

    public void loadConfigValues() {
        reloadConfig();
        FileConfiguration config = getConfig();

        // По умолчанию en
        this.selectedLang = config.getString("language", "en");
        this.pageSize = config.getInt("page-size", 60);
        this.emojiList = config.getStringList("emojis");

        File langFile = new File(getDataFolder(), "languages/" + selectedLang + ".yml");
        if (!langFile.exists()) {
            langFile = new File(getDataFolder(), "languages/en.yml");
        }
        this.langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    private String getMsg(String key) {
        String msg = langConfig.getString(key, "Missing key: " + key);
        return ColorUtils.colorize(msg);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (player.hasPermission("iemoji.reload")) {
                loadConfigValues();
                player.sendMessage(getMsg("reload-success"));
            } else {
                player.sendMessage(getMsg("no-permission"));
            }
            return true;
        }

        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                // Если не число, просто открываем 1 страницу
                page = 1;
            }
        }
        if (page < 1) page = 1;

        int totalEmojis = emojiList.size();
        int totalPages = (int) Math.ceil((double) totalEmojis / pageSize);

        if (page > totalPages && totalPages != 0) {
            player.sendMessage(getMsg("page-not-found").replace("%max%", String.valueOf(totalPages)));
            return true;
        }

        // --- ВИЗУАЛ ---
        FileConfiguration cfg = getConfig();
        String headerLine = ColorUtils.colorize(cfg.getString("style.header-line", "&3&m----------------"));
        String title = ColorUtils.colorize(cfg.getString("style.title", "IEmoji %page%/%total%")
                .replace("%page%", String.valueOf(page))
                .replace("%total%", String.valueOf(totalPages)));

        // Отправляем верхнюю линию и заголовок
        player.sendMessage(headerLine);
        player.sendMessage(title);
        player.sendMessage(""); // Пустая строка для воздуха

        // Собираем смайлики
        TextComponent message = new TextComponent("");
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalEmojis);

        String separator = ColorUtils.colorize(cfg.getString("style.separator", " "));
        String hoverText = getMsg("hover-text");

        for (int i = startIndex; i < endIndex; i++) {
            String emoji = emojiList.get(i);

            TextComponent emojiComponent = new TextComponent(ColorUtils.colorize(emoji));
            emojiComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, emoji));
            emojiComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));

            message.addExtra(emojiComponent);
            message.addExtra(separator);
        }

        player.spigot().sendMessage(message);
        player.sendMessage(""); // Отступ снизу

        // --- НАВИГАЦИЯ ---
        String arrowColor = ColorUtils.colorize(cfg.getString("style.arrow-color", "&b"));
        String bracketColor = ColorUtils.colorize(cfg.getString("style.bracket-color", "&3"));

        TextComponent nav = new TextComponent("");

        // Кнопка Назад
        if (page > 1) {
            TextComponent prev = new TextComponent(bracketColor + "[ " + arrowColor + getMsg("prev-page") + bracketColor + " ]");
            prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/emoji " + (page - 1)));
            prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMsg("prev-hover")).create()));
            nav.addExtra(prev);
        } else {
            // Заглушка, чтобы центрировать визуально, если первой кнопки нет (опционально)
            nav.addExtra(new TextComponent(""));
        }

        // Пробел между кнопками
        if (page > 1 && page < totalPages) {
            nav.addExtra("   ");
        }

        // Кнопка Вперед
        if (page < totalPages) {
            TextComponent next = new TextComponent(bracketColor + "[ " + arrowColor + getMsg("next-page") + bracketColor + " ]");
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/emoji " + (page + 1)));
            next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMsg("next-hover")).create()));
            nav.addExtra(next);
        }

        if (nav.getExtra() != null && !nav.getExtra().isEmpty()) {
            player.spigot().sendMessage(nav);
        }

        // Нижняя линия
        player.sendMessage(headerLine);

        return true;
    }
}