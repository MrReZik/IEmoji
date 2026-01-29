<div align="center">

# 😃 IEmoji Plugin

[![Version](https://img.shields.io/badge/Version-1.0-blue.svg)](https://github.com/MrReZik/IEmoji)
[![API](https://img.shields.io/badge/API-1.16+-green.svg)](https://www.spigotmc.org/)
[![Author](https://img.shields.io/badge/Author-MrReZik-red.svg)](https://github.com/MrReZik)

**A lightweight, chat-based emoji picker for Minecraft servers.** Give your players access to over 200+ UTF-8 symbols with a simple click-to-copy interface.

</div>

---

## ✨ Features

* **Chat-Based UI:** No clumsy inventory GUIs. Everything happens directly in the chat window.
* **Click-to-Copy:** Players simply click on a symbol to copy it to their clipboard.
* **RGB Color Support:** Full support for Hex colors (`&#RRGGBB`) and Legacy colors (`&a`).
* **Pagination:** Smart navigation system with `[Back]` and `[Next]` buttons.
* **Multi-Language:** Built-in support for **English (en)**, **Russian (ru)**, and **Ukrainian (ua)**.
* **Highly Configurable:** Customize colors, messages, page size, and the emoji list itself.

---

## 📥 Installation

1.  Download the `IEmoji-1.0.jar`.
2.  Place the jar file into your server's `plugins` folder.
3.  Restart your server.
4.  The `config.yml` and `languages/` folder will be generated automatically.

---

## 🎮 Commands & Permissions

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/emoji` | Opens the first page of emojis. | `iemoji.use` |
| `/emoji <page>` | Opens a specific page (e.g., `/emoji 2`). | `iemoji.use` |
| `/emoji reload` | Reloads the configuration and language files. | `iemoji.reload` |

---

## ⚙️ Configuration

You can customize the plugin in `config.yml`.

### **Style & Settings**
```yaml
# Language selection: en, ru, ua
language: en

# Number of emojis per page
page-size: 60

style:
  # Supports Hex Colors (&#RRGGBB) for 1.16+
  header-line: "&#009688&m----------------------------------------"
  title: " &#33D9FF&lIEmoji &#009688| &#E0E0E0Page &#33D9FF%page%&#009688/&#33D9FF%total%"
  separator: " "
  arrow-color: "&#33D9FF"
  bracket-color: "&#009688"
