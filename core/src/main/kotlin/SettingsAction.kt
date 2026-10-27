// SettingsAction.kt — что происходит при нажатии SETTINGS.
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.scenes.scene2d.Stage

class SettingsAction(private val game: Main, private val stage: Stage) : MenuAction {

    override fun run() {
        // ЗАДАЧА: экран настроек — звук, музыка, язык. Пока — заглушка.
        stage.addActor(SettingsPopup(game.skin))
    }
}
