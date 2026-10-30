// MainMenuScreen.kt — экран главного меню.
// ScreenAdapter — «экран» libGDX с пустыми методами по умолчанию:
// переопределяем только то, что нужно (show/render/dispose...).
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.FitViewport

class MainMenuScreen(private val game: Main) : ScreenAdapter() {

    // Camera — «глаз» сцены: решает, какую часть мира видим.
    private val camera = OrthographicCamera()

    // Stage — сцена: держит актёров (кнопки, попапы), ловит ввод, анимирует.
    // FitViewport(900, 450) — виртуальный размер экрана 900x450.
    // Реальный экран может быть любым: viewport сам масштабирует картинку,
    // сохраняя пропорции (лишнее — чёрные полосы). Игра всегда в координатах 900x450.
    private val stage = Stage(FitViewport(900f, 450f, camera))

    // root — корневая таблица, раскладывает кнопки по центру экрана.
    private val root = Table()

    // SpriteBatch — «пушистый художник»: умеет быстро рисовать текстуры на экран.
    // Нужен для фона (фон — обычная картинка, не актёр сцены).
    private val batch = SpriteBatch()

    // Фоновая картинка из ассетов. Texture занимает видеопамять — не забыть dispose!
    private val menuBackgroundImg = Texture(Gdx.files.internal("main-menu1.png"))

    // show() вызывается ОДИН раз при появлении экрана. Строим меню здесь.
    override fun show() {
        // ГЛАВНАЯ ГРАБЛЯ libGDX: сцена сама ввод НЕ получает.
        // Пока мы не сказали «ввод идёт в stage» — кнопки мёртвые.
        Gdx.input.inputProcessor = stage

        // setFillParent(true) — таблица занимает весь экран.
        root.setFillParent(true)
        stage.addActor(root) // и добавляем её на сцену первой (фон для кнопок)

        // Четыре кнопки: наш класс GameButton (текст + скин-стиль).
        val btnPlay = GameButton("PLAY", game.skin, "menu")
        val btnRules = GameButton("RULES", game.skin, "menu")
        val btnSettings = GameButton("SETTINGS", game.skin, "menu")
        val btnExit = GameButton("EXIT", game.skin, "menu")

        // Каждой кнопке — СВОЙ класс-действие (отдельный файл *Action.kt).
        // Меню только знакомит кнопку с действием; сама логика живёт в тех файлах.
        // Хочешь поменять поведение кнопки — иди в ЕЁ файл, сюда не лезь.
        btnPlay.onClick = { PlayAction(game, stage).run() }
        btnRules.onClick = { RulesAction(game, stage).run() }
        btnSettings.onClick = { SettingsAction(game, stage).run() }
        btnExit.onClick = { ExitAction(game, stage).run() }

        // Раскладка: add(...) кладёт кнопку в ячейку, pad — отступы вокруг,
        // row() — перенос строки (следующая кнопка ниже), итого вертикальный столбик.

        val column = Table()

        column.add(btnPlay).width(250f).height(60f).pad(12f).row()
        column.add(btnRules).width(250f).height(60f).pad(12f).row()
        column.add(btnSettings).width(250f).height(60f).pad(12f).row()
        column.add(btnExit).width(250f).height(60f).pad(12f).row()

        root.add(column).expand().left().padLeft(80f)
    }

    // render(delta) вызывается КАЖДЫЙ КАДР. delta = секунд с прошлого кадра.
    // Порядок рисования = порядок вызовов: сначала фон, потом сцена поверх.
    override fun render(delta: Float) {
        batch.begin()                // begin/end — рамка: между ними только рисование
        // draw(текстура, x, y, ширина, высота) — растянуть картинку на весь экран.
        // Gdx.graphics.width/height — РЕАЛЬНЫЙ размер окна в пикселях,
        // поэтому фон тянется на всё окно, а не на виртуальные 900x450.
        // Опционально: можно передать srcX/srcW (кусок атласа), поворот, flip —
        // для фона не нужно.
        batch.draw(menuBackgroundImg, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.end()

        stage.act(delta) // act: сцена проживает кадр — анимации, таймеры, действия (fadeIn и т.п.)
        stage.draw()     // draw: сцена рисует всех своих актёров (кнопки, попапы)
    }

    // resize вызывается при изменении размера окна. Пересчитываем viewport,
    // true = центрировать камеру (иначе картинка съедет).
    override fun resize(width: Int, height: Int) = stage.viewport.update(width, height, true)

    // Пустые, но обязаны быть у контракта Screen (ScreenAdapter закрыл их за нас).
    override fun hide() {}
    override fun pause() {}   // сворачивание приложения (мобильные)
    override fun resume() {}  // возврат из свёрнутого

    // dispose = прибраться при уходе с экрана. Кто создал ресурс — тот удаляет.
    override fun dispose() {
        stage.dispose()
        menuBackgroundImg.dispose()
        batch.dispose()
    }
}
