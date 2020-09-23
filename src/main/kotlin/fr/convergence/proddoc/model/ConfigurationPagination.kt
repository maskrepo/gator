package fr.convergence.proddoc.model

import kotlinx.serialization.Serializable

@Serializable
class ConfigurationPagination(
    val xIndent: Double = 275.0,
    val yIndent: Double = 20.0,
    val font: String = "Calibri",
    val fontSize: Float = 10f,
    val foregroundColorR: Int = 0,
    val foregroundColorG: Int = 0,
    val foregroundColorB: Int = 0,
) {

    override fun toString(): String {
        return "ConfigurationPagination(xIndent=$xIndent, yIndent=$yIndent, font='$font', fontSize=$fontSize, foregroundColorR=$foregroundColorR, foregroundColorG=$foregroundColorG, foregroundColorB=$foregroundColorB)"
    }
}