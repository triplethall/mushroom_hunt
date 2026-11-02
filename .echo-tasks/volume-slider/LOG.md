# LOG: volume-slider

## Разведка 2026-11-02

**Кто писал:** субагент-разведчик (ход diрижёра echo). Репо: /mnt/ssd_data/repos/mushroom_hunt, ветка echo/work (проверено: git branch = echo/work, HEAD 937b5ed «Фикс: заглушка bundle_en.properties...»).

### Что читал
1. `.echo-tasks/volume-slider/TASK.md` — ТЗ задачи (живые настройки, хранилище+UI, без аудио).
2. `core/src/main/kotlin/SettingsPopup.kt` (80 строк) — попап настроек.
3. `core/src/main/kotlin/Localization.kt` (96 строк) — образец работы с Preferences.
4. `core/src/main/kotlin/Main.kt` (113 строк) — точка входа, create().
5. `core/src/main/kotlin/GameButton.kt` (33 строки) — своя кнопка (onClick-лямбда), для понимания стиля.
6. `core/src/main/kotlin/SettingsAction.kt` — как попап вызывается (stage.addActor(SettingsPopup(game.skin))).
7. `ls core/src/main/kotlin/` — полный список файлов проекта (15 шт., см. ниже).
8. `grep GameSettings|getPreferences` по всему core — кроме Localization.kt ничего не нашлось (GameSettings в проекте НЕ существует, конфликтов имён нет).

### Находки

**Структура SettingsPopup.kt:**
- `class SettingsPopup(skin: Skin) : Table()`, всё в `init {}`
- `setFillParent(true)` + полупрозрачный фон — модальное затемнение
- Контролы-декорации (сейчас ни к чему не подключены):
  - `val chkMute = CheckBox("  " + Localization["settings.mute"], skin)` — чекбокс MUTE
  - `val sldVolume = Slider(0f, 100f, 1f, false, skin)`, `sldVolume.value = 70f` — хардкод дефолта
  - `val chkVibration = CheckBox(...)` — чекбокс VIBRATION
- Живой образец listener'а — SelectBox языка (строки 31–51): `selLanguage.addListener(object : ChangeListener() { override fun changed(event: ChangeEvent?, actor: Actor?) {...} })`, внутри — вызов модуля + пересоздание попапа (remove + st.addActor(SettingsPopup(skin))) для перевода надписей на лету
- Раскладка: box Table с background, строки «Label + виджет» через `box.add(...).pad(10f)`, слайдеру `.width(220f)`
- Кнопка CLOSE — `GameButton`, `btnClose.onClick = { remove() }`

**Механизм Preferences в Localization.kt:**
- `private const val PREFS_NAME = "mushroom-hunt-settings"` — ИМЯ ФАЙЛА, его же использует GameSettings
- Чтение: `Gdx.app.getPreferences(PREFS_NAME).getString(PREF_KEY_LANG, null)`
- Запись: `Gdx.app.getPreferences(PREFS_NAME).putString(key, value).flush()` (одной цепочкой)
- Загрузка при старте: `Localization.load()` вызывается в `Main.create()` ПЕРВОЙ строкой (до создания skin и экранов) — туда же встаёт `GameSettings.load()`
- Паттерн: `object`-синглтон + `load()` из create() + setter'ы с flush. Это и есть образец для GameSettings.

**Main.kt:** `create()` → Localization.load() → skin → freetype-шрифт с кириллицей («default-font») → кнопочный стиль → setScreen(MainMenuScreen(this)). Точка вставки GameSettings.load() — рядом с Localization.load(), строка ~43.

**Полный список kt-файлов проекта:** ExitAction, GameButton, InfoPopup, Localization, Main, MainMenuScreen, MenuAction, PlayAction, PlayScreen, RulesAction, RulesPopup, SettingsAction, SettingsPopup (+ PlayAction отдельно). Все — package ru.triplethall.mushroomhunt, плоская структура, без подкаталогов.

### Выводы
1. Задача чисто механическая: копируем паттерн Localization (object + load + setter'ы с flush) под три ключа volume/mute/vibration в ТОТ ЖЕ prefs-файл. Никаких новых зависимостей, только Gdx.app.getPreferences.
2. UI-подключение — по образцу selLanguage: ChangeListener на каждый контрол, запись через setter GameSettings. Пересоздание попапа при смене языка уже есть и бесплатно подхватит новые контролы (они перечитают значения при новом init).
3. Аудио/вибрации в проекте нет вообще (grep confirming: только Localization трогает prefs) — «подводных» интеграций ноль, чистое хранилище.
4. Риски минимальны: опечатка в PREFS_NAME, холостой выстрел ChangeEvent при программной установке slider.value (безвреден — пишет то же значение). Подробности — в PLAN.md, раздел «Подводные камни».

План: см. PLAN.md (3 пункта: GameSettings.kt → подключение контролов в SettingsPopup + Main.create → сборка core:compileKotlin).
