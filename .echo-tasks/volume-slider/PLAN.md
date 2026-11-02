# PLAN: Живые настройки VOLUME / MUTE / VIBRATION

Ветка: echo/work. Коммитит дирижёр (echo), исполнитель — НЕ коммитит.
Комментарии в стиле методички, по-русски (образец — Localization.kt).
Формат ключей (утверждён в TASK.md):
- `volume` — Float, 0..100
- `mute` — Boolean
- `vibration` — Boolean
- prefs-файл: `mushroom-hunt-settings` (тот же, что в Localization.kt)

---

## 1. Создать GameSettings.kt (хранилище-синглтон)

**Файл:** `core/src/main/kotlin/GameSettings.kt` (новый)

**Что делать:** объект-синглтон `object GameSettings` по образцу Localization.kt:
- `private const val PREFS_NAME = "mushroom-hunt-settings"` (то же имя!)
- ключи: `volume` / `mute` / `vibration`
- `var volume: Float` с дефолтом 70f — setter пишет в prefs (`putFloat` + `flush()`)
- `var mute: Boolean` с дефолтом false — аналогично (`putBoolean`)
- `var vibration: Boolean` с дефолтом true — аналогично
- `fun load()` — читает сохранённые значения из prefs (вызывается один раз из Main.create()); вызовы без load() должны работать и так (Kotlin-свойства читают prefs лениво или дефолты), но для единообразия с Localization — явный load()
- `fun setVolumeSilently(v: Float)` ИЛИ приватный хелпер: выставить значение в память БЕЗ записи в prefs — нужно, чтобы(SettingsPopup) мог выставить сохранённое значение слайдеру без ложного срабатывания listener'а записи (см. п.2). Альтернатива без хелпера: в SettingsPopup выставлять slider.value ПОСЛЕ навешивания listener — тогда хелпер не нужен, listener при программной установке не стреляет (libGDX setValue(programmatic change) НЕ кидает ChangeEvent — но это надо проверить на практике: clamped/анулирующие установки могут стрелять). Проще: порядок «сначала listener, потом slider.value = GameSettings.volume» — если ChangeEvent выстрелит, он просто запишет то же значение в prefs, это безвредно.
- setter'ы НЕ дергают аудио/вибрацию — их в игре нет, только хранилище (по ТЗ)

**Критерий готовности:** файл компилируется, объект доступен из любого модуля как `GameSettings.volume` без передачи ссылок, значения переживают flush.

## 2. Подключить контролы в SettingsPopup.kt

**Файл:** `core/src/main/kotlin/SettingsPopup.kt` (правка)

**Что делать:**
- `sldVolume.value = 70f` заменить на `sldVolume.value = GameSettings.volume`
- `chkMute.isChecked = GameSettings.mute` (после создания чекбокса)
- `chkVibration.isChecked = GameSettings.vibration`
- на `sldVolume` навесить `ChangeListener` (по образцу selLanguage, строки 37–51): в `changed()` — `GameSettings.volume = sldVolume.value` (setter сам пишет prefs+flush)
- на `chkMute` / `chkVibration` — такие же ChangeListener'ы: `GameSettings.mute = chkMute.isChecked` и т.д.
- порядок: значение подгружаем ДО или ПОСЛЕ listener — если после, возможен холостой выстрел ChangeEvent с записью того же значения (безвредно); оставить просто и понятно, как в методичке
- В Main.kt ДОБАВИТЬ `GameSettings.load()` рядом с `Localization.load()` в `create()` (строка ~43), с комментарием «настройки живут в Preferences, грузим до экранов»

**Критерий готовности:** открыл настройки — контролы показывают сохранённые значения; подвигал/потыкал — значения в prefs (проверка: уметь прочитать prefs после перезапуска десктоп-раннера).

## 3. Сборка-проверка

**Что делать:**
```
cd /mnt/ssd_data/repos/mushroom_hunt && ./gradlew core:compileKotlin --no-daemon -i > /tmp/mh_build.log 2>&1; echo EXIT=$?
```
stdout у градла на d1337 тихий — смотреть EXIT-код и хвост лога (grep 'BUILD\|error:' /tmp/mh_build.log).
Затем — ручная проверка критериев TASK.md (1–5): запустить desktop-раннер если доступен (`./gradlew desktop:run` — уточнить наличие модуля desktop), открыть настройки, поменять, закрыть, перезапустить, убедиться что на месте. Если запуск раннера в этой среде невозможен (нет дисплея) — проверить prefs-файл десктопа напрямую (на Linux libGDX prefs = `~/.prefs/mushroom-hunt-settings`... на самом деле `~/.prefs/` — это Mac; на Linux `~/.prefs/` тоже может быть, точный путь смотрим через strace или просто верим flush()). Минимум — зелёный core:compileKotlin + code review дирижёра.

**Критерий готовности:** EXIT=0, BUILD SUCCESSFUL в логе, критерии 1–3 TASK.md подтверждены (или явно указано, почему runtime-проверка недоступна).

---

## Подводные камни (для исполнителя)
1. **Slider.setValue программно** может кинуть ChangeEvent (если значение clamped/меняется) — при выставлении сохранённого значения listener записи может выстрелить холостым. Безвредно (пишет то же значение), но знать надо.
2. **Имя prefs-файла** — строго `mushroom-hunt-settings`, как в Localization.kt (PREFS_NAME). Опечатка = «настройки не переживают перезапуск».
3. **Мьют и слайдер НЕ связаны логикой** — аудио нет, mute просто хранится. Не изобретать «mute обнуляет volume».
4. **Клавиша volume — Float**, в Slider тоже Float (0f..100f), конверсий нет. Не округлять до Int без нужды.
5. **Localization перезагружает попап** (remove + новый SettingsPopup) при смене языка — новые контролы и listener'ы создадутся заново и перечитают GameSettings. Это бесплатно работает, ничего дополнительно делать не надо, но не ломать этот механизм.
6. **flush() на каждый чих** — ок для этой задачи (записей мало), не «оптимизировать» в debounce.
7. **Коммитить нельзя** — только правки файлов, коммитит дирижёр.
