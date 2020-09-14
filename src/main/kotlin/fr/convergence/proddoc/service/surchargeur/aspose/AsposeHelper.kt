package fr.convergence.proddoc.service.surchargeur.aspose

import com.aspose.pdf.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.function.Consumer
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class AsposeHelper {

    fun ajouterStampSurToutesLesPages(fichier: ByteArray, stamp: Stamp): ByteArray {
        val document = Document(ByteArrayInputStream(fichier))
        try {
            document.pages.forEach(Consumer { page: Page ->
                page.addStamp(stamp)
            })

            ByteArrayOutputStream().use { byteArrayOutputStream ->
                document.save(byteArrayOutputStream)
                return byteArrayOutputStream.toByteArray()
            }
        } finally {
            document.close()
        }
    }

    fun genererStampPourCopieConforme(): TextStamp {

        val texteStamp = TextStamp("Copie certifiée conforme")
        texteStamp.xIndent = 20.0
        texteStamp.yIndent = 25.0
        texteStamp.textState.foregroundColor = Color.fromArgb(0, 0, 0)
        texteStamp.textState.font = FontRepository.findFont("Calibri")
        texteStamp.textState.fontSize = 6f
        return texteStamp
    }

    fun genererStampPourFiligrane(texte: String?): TextStamp {

        val texteStamp = TextStamp(texte)
        texteStamp.rotateAngle = 45.0
        texteStamp.textState.font = FontRepository.findFont("Times-Roman")
        texteStamp.textState.fontSize = 100f
        texteStamp.textState.foregroundColor = Color.getLightGray()
        texteStamp.textState.setFontStyle(FontStyles.Bold)
        texteStamp.horizontalAlignment = HorizontalAlignment.Center
        texteStamp.verticalAlignment = VerticalAlignment.Center
        texteStamp.opacity = 0.5
        return texteStamp
    }
}