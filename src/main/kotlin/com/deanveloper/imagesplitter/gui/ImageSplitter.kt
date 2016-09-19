package com.deanveloper.imagesplitter.gui

import com.deanveloper.imagesplitter.gui.miscwindows.RetryException
import com.deanveloper.imagesplitter.gui.miscwindows.retryWrap
import javafx.scene.control.ScrollPane
import javafx.stage.FileChooser
import tornadofx.*
import javax.imageio.ImageIO

/**
 * @author Dean
 */
class ImageSplitter : View("Image Slitter by Deanveloper") {
    override val root = ScrollPane()

    init {
        retryWrap {
            val file = chooseFile(
                    title = "Find original picture",
                    filters = arrayOf(FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")),
                    mode = FileChooserMode.Single,
                    owner = this.primaryStage
            ).firstOrNull()

            if(file === null) {
                throw RetryException("No image found")
            }

            val image = ImageIO.read(file)
        }
    }
}