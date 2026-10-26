// PlayAction.kt — что происходит при нажатии PLAY.
package ru.triplethall.mushroomhunt

import com.badlogic.gdx.scenes.scene2d.Stage

// : MenuAction — реализуем контракт. Компилятор заставит написать run().
// В конструкторе принимаем всё, что может понадобиться: игру (Main) и сцену (Stage).
class PlayAction(private val game: Main, private val stage: Stage) : MenuAction {

    override fun run() {
       game.setScreen(PlayScreen(game)) // Меню уходит в тень и мы переключаемся на сцену с игрой
    }
}
