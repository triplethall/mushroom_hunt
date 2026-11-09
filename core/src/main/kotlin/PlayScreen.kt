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

    init {
        game.gameState.ingame = true
    }


//    Это у нас задник, который в главном меню
    private val bg = Texture(Gdx.files.internal("main-menu1.png"))

    private val opponentes = Texture("opponent.png") // просто вставляем заглушку
    private val BagOfMushroomsTexture = Texture("badpic.png") // просто вставляем заглушку


    // функция показа
    override fun show() {
        Gdx.input.inputProcessor = stage // Оживляем кнопку

        // Тут мы делаем оппонента (но пока пусть это будет обычный квадрат)
        val opponentView = com.badlogic.gdx.scenes.scene2d.ui.Image(opponentes)
        opponentView.setSize(10f, 10f)
        opponentView.setScaling(com.badlogic.gdx.utils.Scaling.fit)

        //Тут щас будет отображать мешочек
        val BagOfMushrooms = com.badlogic.gdx.scenes.scene2d.ui.ImageButton(com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(com.badlogic.gdx.graphics.g2d.TextureRegion(BagOfMushroomsTexture)))
        BagOfMushrooms.setSize(10f, 10f)
        BagOfMushrooms.image.setScaling(com.badlogic.gdx.utils.Scaling.fit)

        // Действие при клеке тут пишите


        //Кнопка выхода
        val btnBack = GameButton(Localization["game.back"], game.skin)
        btnBack.onClick = {
            // переключаем
            game.setScreen(MainMenuScreen(game))
            // после за собой нужно убрать
            dispose()
        }



        // ТУТ ВСЯ КОМПАНОВКА, ТОЛЬКО ТУТ! Соблюдайте чистоту кода!
        val root = Table()
        root.setFillParent(true)
        root.pad(20f)

        // 1. Пустая ячейка слева (чтобы отодвинуть кнопку от края, если нужно)
        // На самом деле, кнопку мы ставим в эту же ячейку
        root.add(btnBack)
            .left() // Прижимаем влево
            .top() // Прижимаем к верху
            .width(200f) // Фиксируем ширину левой колонки (подбери под размер кнопки + отступ)

        // 2. Оппонент (ЦЕНТР)
        root.add(opponentView)
            .size(80f, 80f)
            .top()
            .expandX()

        // 3. Пустая ячейка справа (ровно такой же ширины, как левая)
        root.add()
            .width(200f) // <--- Фиксируем такую же ширину, как слева

        root.row()

        // 3. Мешочек (по центру)
        root.add(BagOfMushrooms)
            .size(100f, 100f)
            .expandY() // Растягивает ячейку по высоте, чтобы мешочек был ровно по центру
            .colspan(3) // Занимает все 3 колонки

        // И в конце добавляем всё на экран
        stage.addActor(root)



        // СТАРЫЙ НЕ АКТУАЛЬНЫЙ КОММЕНТ:
        // Тут делаем дейстаие при нажатие, в этом слцчае возвращаем в меню.
        // Текст кнопки — из словаря локализации (bundle*.properties),
        // заодно ушла старая опечатка «Bacl» :)

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
        opponentes.dispose()
        BagOfMushroomsTexture.dispose()
    }

}
