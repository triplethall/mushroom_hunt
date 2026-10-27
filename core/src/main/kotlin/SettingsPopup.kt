package ru.triplethall.mushroomhunt
import com.badlogic.gdx.scenes.scene2d.ui.*
class SettingsPopup(skin: Skin) : Table() {
    init {
        setFillParent(true) // занять весь экран (затемнение под окошком)
        background(skin.newDrawable("white", 0f, 0f, 0f, 0.6f)) // полупрозрачная тень
// === ВИДЖЕТЫ ===
// Переключатель (чекбокс): включён/выключен. Сам ничего не делает — просто тумблер.
        val chkMute = CheckBox("  MUTE", skin)
// Ползунок громкости: Slider(мин, макс, шаг, вертикальный?, скин)
        val sldVolume = Slider(0f, 100f, 1f, false, skin)
        sldVolume.value = 70f // начальное положение ползунка
// Переключатель вибрации (для телефонов)
        val chkVibration = CheckBox("  VIBRATION", skin)
// Выпадающий список языков
        val selLanguage = SelectBox<String>(skin)
        selLanguage.setItems("RU", "EN", "DE")
// Кнопка закрытия — наш общий GameButton
        val btnClose = GameButton("CLOSE", skin)
        btnClose.onClick = { remove() } // remove() — «уйти со сцены», окошко исчезнет
// === РАСКЛАДКА ===
// Карточка настроек — тёмная коробочка по центру
        val box = Table(skin)
        box.background(skin.newDrawable("white", 0.15f, 0.12f, 0.1f, 0.95f))
// Заголовок. Label — просто текстовая надпись.
        box.add(Label("SETTINGS", skin)).pad(20f).colspan(2).row()
// Строка «VOLUME + ползунок»: слева надпись, справа виджет
        box.add(Label("VOLUME", skin)).pad(10f).left()
        box.add(sldVolume).pad(10f).width(220f).row()
// Строка «MUTE + тумблер»
        box.add(Label("MUTE", skin)).pad(10f).left()
        box.add(chkMute).pad(10f).row()
// Строка «VIBRATION + тумблер»
        box.add(Label("VIBRATION", skin)).pad(10f).left()
        box.add(chkVibration).pad(10f).row()
// Строка «LANGUAGE + выпадашка»
        box.add(Label("LANGUAGE", skin)).pad(10f).left()
        box.add(selLanguage).pad(10f).row()
// Кнопка CLOSE во всю ширину, побольше отступ сверху
        box.add(btnClose).pad(20f).colspan(2).growX().row()
// Коробочку — в центр экрана
        add(box).expand().center()
    }
}
