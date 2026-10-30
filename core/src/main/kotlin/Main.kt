// Main.kt — директор игры.
// Game — базовый класс libGDX: он решает, КАКОЙ экран сейчас живёт и кто получает render().
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.ui.Skin

//Новые импорты для красивой кнопки:
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

class Main : Game() {

    // lateinit = «поле будет, но заполню позже».
    // Кожа (skin) — пакет стилей: шрифты, текстуры кнопок, отступы.
    // Живёт всю игру, поэтому храним её тут, а не в каждом экране.

    //Жуть какая...
    lateinit var skin: Skin

    //Дежмим кнопку в переменнной, что бы потом её убить :)
    lateinit var buttonTexture: Texture

    // create() вызывается ОДИН раз при старте приложения.
    // Здесь готовим ресурсы и показываем первый экран.
    override fun create() {
        // Gdx.files.internal(...) — файл из папки ассетов (android/assets/).
        skin = Skin(Gdx.files.internal("ui/uiskin.json"))

        // Берём текстурку кнопки из папки
        buttonTexture = Texture(Gdx.files.internal("buttons/menubutton.png"))
        // Собираем стиль
        val menuStyle = TextButton.TextButtonStyle()
        // Обычный вид
        menuStyle.up = TextureRegionDrawable(buttonTexture)
        //Когда нажали на кнопку, меняем её цвет на секунду
        menuStyle.down = TextureRegionDrawable(buttonTexture).tint(Color(0.65f, 0.65f, 0.65f, 1f))
        // тут просто меняем цвет когда наводимся мышкой, что бы показать, что кнопка кликабельна
        menuStyle.over = TextureRegionDrawable(buttonTexture).tint(Color(0.85f, 0.85f, 0.85f, 1f))
        // Загружаем шрифт
        menuStyle.font = skin.getFont("default-font")
        // Цвет текста
        menuStyle.fontColor = Color.WHITE
        // Ииии... добавляем как скин для кнопок через вызов
        skin.add("menu", menuStyle)

        // setScreen = переключить активный экран. Игра начинается с меню.
        setScreen(MainMenuScreen(this))
    }

    // render() вызывается КАЖДЫЙ КАДР (60 раз в секунду и чаще).
    // Наш override рисует фон, затем super.render() отдаёт кадр текущему экрану.
    override fun render() {
        // glClearColor(r,g,b,a) — ЗАЛИТЬ буфер цветом перед рисованием.
        // Числа 0..1: 0.06/0.07/0.08 — почти чёрный, ночной лес. 1f = непрозрачно.
        Gdx.gl.glClearColor(0.06f, 0.07f, 0.08f, 1f)

        // glClear — реально очистить экран этим цветом. Без этого будет «грязь» из прошлого кадра.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // super.render() — теперь рисует текущий экран (у нас — MainMenuScreen).
        super.render()
    }

    // dispose() вызывается при выходе из игры. Освобождаем видеопамять.
    // Правило: кто создал ресурс (Texture, Skin, Batch) — тот и dispose().
    override fun dispose() {
        skin.dispose()
        buttonTexture.dispose()
    }
}
