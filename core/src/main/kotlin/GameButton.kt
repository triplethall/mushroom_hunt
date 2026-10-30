// GameButton.kt — НАША кнопка.
// Наследуемся от TextButton (кнопка с текстом из libGDX) и добавляем своё: onClick.
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

// : TextButton — наследование. GameButton УМЕЕТ всё, что умеет TextButton, плюс своё.
class GameButton(text: String, skin: Skin, styleName: String = "default") : TextButton(text, skin, styleName) {


    // var onClick: () -> Unit — свойство-лямбда («кусок кода в переменной»).
    // По умолчанию пустое {}. Снаружи можно записать: btn.onClick = { ...что угодно... }
    // Так кнопка НЕ ЗНАЕТ, что она делает, — тот, кто её создал, решает сам.
    var onClick: () -> Unit = {}

    // init-блок выполняется при создании каждого объекта.
    init {
        // addListener — подписаться на события актёра (мышь/тач).
        // object : ClickListener() — анонимный класс: создаём слушателя «на месте»,
        // не объявляя отдельный класс в файле.
        addListener(object : ClickListener() {

            // clicked(event, x, y) — libGDX сам вызовет это при клике.
            // x, y — где случился клик относительно кнопки (могут пригодиться).
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                onClick() // выполняем то, что нам положили в onClick
            }
        })
    }
}
