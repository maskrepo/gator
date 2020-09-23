package fr.convergence.proddoc.model

import com.aspose.pdf.HorizontalAlignment
import com.aspose.pdf.VerticalAlignment
import kotlinx.serialization.Serializable

@Serializable
class ConfigurationFiligrane(
    val texte: String,
    val rotateAngle: Double = 45.0,
    val opacity: Double = 0.5,
    val font: String = "Times-Roman",
    val fontSize: Float = 100f,
    val foregroundColorR: Int = 211,
    val foregroundColorG: Int = 211,
    val foregroundColorB: Int = 211,
    val fontStyleBold: Boolean = true,
    val horizontalAlignment: Int = HorizontalAlignment.Center,
    val verticalAlignment: Int = VerticalAlignment.Center
) {

    override fun toString(): String {
        return "ConfigurationFiligrane(texte='$texte', rotateAngle=$rotateAngle, opacity=$opacity, font='$font', fontSize=$fontSize, foregroundColorR=$foregroundColorR, foregroundColorG=$foregroundColorG, foregroundColorB=$foregroundColorB, fontStyleBold=$fontStyleBold, horizontalAlignment=$horizontalAlignment, verticalAlignment=$verticalAlignment)"
    }

}