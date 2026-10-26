package ru.triplethall.mushroomhunt

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.FitViewport


class PlayScreen(private val game: Main) : ScreenAdapter() {
    private val camera = OrthographicCamera()
    private val stage = Stage(FitViewport(900f, 450f, camera))
    private val batch = SpriteBatch()

//    Это у нас задник, который в главном меню
    private val bg = Texture(Gdx.files.internal("main-menu1.png"))


    // функция показа
    override fun show() {
        Gdx.input.inputProcessor = stage // Оживляем кнопку

        val root = Table()
        root.setFillParent(true)
        // -------------
        root.top().left()
        root.pad(16f) // внешний отступ от слова padded
        // Тут мы делаем положение и отступ кнопки возврата в меню
        stage.addActor(root) // Добавили кноаку полностью


        // Тут делаем дейстаие при нажатие, в этом слцчае возвращаем в меню
        val btnBack = GameButton("Bacl to menu", game.skin)
        btnBack.onClick = {
            // переключаем
            game.setScreen(MainMenuScreen(game))
            // после за собой нужно убрать
            dispose()
        }
        root.add(btnBack) // Ии... у нас есть кнопка!
    }

    // тут я пока сам не понимаю, что такое, но по названию скорее всего рендер картинки
    override fun render(delta: Float) {
        batch.begin()
        batch.draw(bg, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.end()

        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) = stage.viewport.update(width, height, true)

    override fun dispose() {
        stage.dispose()
        bg.dispose()
        batch.dispose()
    }

}
