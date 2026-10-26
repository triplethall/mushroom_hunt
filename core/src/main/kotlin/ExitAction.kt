package ru.triplethall.mushroomhunt
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
class ExitAction(private val game: Main, private val stage: Stage) : MenuAction {
    override fun run() {
// Просим libGDX завершить приложение корректно:
        // на десктопе закроется окно, на Android приложение свернётся с finish().
        Gdx.app.exit()
    }
}

