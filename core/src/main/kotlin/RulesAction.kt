// RulesAction.kt — что происходит при нажатии RULES.
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.scenes.scene2d.Stage

class RulesAction(private val game: Main, private val stage: Stage) : MenuAction {

    override fun run() {
        // ЗАДАЧА: здесь будет экран правил (список, скролл, картинки). Пока — заглушка.
        stage.addActor(RulesPopup(game.skin))
    }
}
