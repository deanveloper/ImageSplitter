package com.deanveloper.imagesplitter.gui.miscwindows

import javafx.scene.Parent
import javafx.scene.layout.HBox
import tornadofx.*
import kotlin.system.exitProcess

/**
 * @author Dean
 */
class RetryWindow(reason: String) : Fragment() {
    override val root: Parent
        get() = HBox()

    init {
        label(reason)
        label("Retry:?")

        hbox {
            button("Yes") {
                isDefaultButton = true
            }
            button("No") {
                isCancelButton = true
                setOnAction { exitProcess(1) }
            }
        }
    }
}

class RetryException(reason: String) : RuntimeException(reason)

inline fun retryWrap(op: () -> Unit) {
    try {
        op()
    } catch (e: RetryException) {
        find(RetryWindow::class).openModal()
    }
}