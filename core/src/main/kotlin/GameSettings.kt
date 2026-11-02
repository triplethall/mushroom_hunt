// GameSettings.kt — хранилище игровых настроек (громкость, mute, вибрация).
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.Gdx

/**
 * МОДУЛЬ НАСТРОЕК.
 *
 * Зачем это нужно:
 *   Игрок крутит ползунок громкости или щёлкает тумблеры в настройках —
 *   и ожидает, что выбор ПЕРЕЖИВЁТ перезапуск игры. libGDX даёт для этого
 *   Preferences: на десктопе это файлик в ~/.prefs, на Android —
 *   SharedPreferences. Мы храним три значения: громкость (0..100),
 *   mute (без звука) и vibration (вибрация на телефонах).
 *
 * Почему object (синглтон):
 *   Настройки нужны всем экранам сразу, а хранилище одно на всю игру.
 *   Kotlin `object` = «один экземпляр на всё приложение». Читаем из любого
 *   места просто: GameSettings.volume — без передачи ссылок через экраны.
 *
 * Как добавить новую настройку:
 *   1. Завести ключ-константу (PREF_KEY_...) и дефолт рядом.
 *   2. Завести var: дефолт в инициализаторе, а в setter'е — запись в prefs
 *      (putXxx + flush). Теперь ЛЮБОЕ присваивание = автосохранение.
 *   3. Прочитать сохранённое значение в load().
 *   Больше ничего: остальной код не трогаем.
 *
 * Важно:
 *   Здесь ТОЛЬКО хранилище. Реального звука/вибрации в игре пока нет —
 *   когда аудиомодуль появится, он будет просто читать GameSettings.volume,
 *   не зная ничего про экран настроек. Mute и громкость НЕ связаны логикой:
 *   это два независимых ключа, «mute обнуляет volume» не изобретаем.
 */
object GameSettings {

    /**
     * Имя файла настроек — ТО ЖЕ, что в Localization.kt.
     * Один prefs-файл на все настройки игры: язык, громкость, тумблеры.
     * Опечатка в имени = «настройки не переживают перезапуск».
     */
    private const val PREFS_NAME = "mushroom-hunt-settings"

    // Ключи внутри файла. Если ключа нет (первый запуск) — вернётся дефолт.
    private const val PREF_KEY_VOLUME = "volume"       // Float, 0..100
    private const val PREF_KEY_MUTE = "mute"           // Boolean
    private const val PREF_KEY_VIBRATION = "vibration" // Boolean

    // Дефолты: с них начинаем, пока игрок ничего не менял.
    private const val DEFAULT_VOLUME = 70f      // 70% громкости
    private const val DEFAULT_MUTE = false      // звук не выключен
    private const val DEFAULT_VIBRATION = true  // вибрация включена

    /**
     * Громкость, 0..100. Присвоил — сохранилось:
     * setter сам пишет значение в Preferences и делает flush() (сброс
     * на диск). Диапазон зажимаем coerceIn — страховка от кривых значений.
     */
    var volume: Float = DEFAULT_VOLUME
        set(value) {
            field = value.coerceIn(0f, 100f)
            Gdx.app.getPreferences(PREFS_NAME).putFloat(PREF_KEY_VOLUME, field).flush()
        }

    /** Тихий режим (без звука). Присвоил — сохранилось, как у volume. */
    var mute: Boolean = DEFAULT_MUTE
        set(value) {
            field = value
            Gdx.app.getPreferences(PREFS_NAME).putBoolean(PREF_KEY_MUTE, field).flush()
        }

    /** Вибрация (для телефонов). Присвоил — сохранилось, как у volume. */
    var vibration: Boolean = DEFAULT_VIBRATION
        set(value) {
            field = value
            Gdx.app.getPreferences(PREFS_NAME).putBoolean(PREF_KEY_VIBRATION, field).flush()
        }

    /**
     * Загрузка сохранённых значений. Вызывается ОДИН раз при старте игры
     * (Main.create(), сразу после Localization.load()).
     *
     * Присваивания идут через setter'ы — то есть заодно записывают
     * прочитанное обратно в prefs. Это безвредно (пишем то, что только что
     * прочитали), зато код простой: один путь записи на все случаи.
     *
     * Без вызова load() объект тоже работает — просто живёт на дефолтах
     * до первого присваивания.
     */
    fun load() {
        val prefs = Gdx.app.getPreferences(PREFS_NAME)
        // Второй аргумент — дефолт: вернётся, если ключа ещё нет в файле.
        volume = prefs.getFloat(PREF_KEY_VOLUME, DEFAULT_VOLUME)
        mute = prefs.getBoolean(PREF_KEY_MUTE, DEFAULT_MUTE)
        vibration = prefs.getBoolean(PREF_KEY_VIBRATION, DEFAULT_VIBRATION)
    }
}
