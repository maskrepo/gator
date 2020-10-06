package fr.convergence.proddoc.service.surchargeur.aspose

import com.aspose.pdf.*
import fr.convergence.proddoc.model.ConfigurationFiligrane
import fr.convergence.proddoc.model.ConfigurationPagination
import fr.convergence.proddoc.service.SurchargeService
import org.slf4j.LoggerFactory
import java.util.function.Consumer
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class AsposeHelper {

    companion object {
        private val LOG = LoggerFactory.getLogger(SurchargeService::class.java)
    }

    fun ajouterStampSurToutesLesPages(document: Document, stamp: Stamp) {

        LOG.info("Nombre de pages du document ${document.pages.size()}")
        document.pages.forEach(Consumer { page: Page ->
            page.addStamp(stamp)
        })
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

    fun genererStampPourFiligrane(filigrane: ConfigurationFiligrane): TextStamp {

        val texteStamp = TextStamp(filigrane.texte)
        texteStamp.rotateAngle = filigrane.rotateAngle
        texteStamp.textState.font = FontRepository.findFont(filigrane.font)
        texteStamp.textState.fontSize = filigrane.fontSize
        texteStamp.textState.foregroundColor =
            Color.fromArgb(filigrane.foregroundColorR, filigrane.foregroundColorG, filigrane.foregroundColorB)

        if (filigrane.fontStyleBold) {
            texteStamp.textState.setFontStyle(FontStyles.Bold)
        }

        texteStamp.horizontalAlignment = filigrane.horizontalAlignment
        texteStamp.verticalAlignment = filigrane.verticalAlignment
        texteStamp.opacity = filigrane.opacity
        return texteStamp
    }

    fun genererStampPourPagination(pagination: ConfigurationPagination): TextStamp {

        val texteStamp = TextStamp("")
        texteStamp.xIndent = pagination.xIndent
        texteStamp.yIndent = pagination.yIndent
        texteStamp.textState.foregroundColor =
            Color.fromArgb(pagination.foregroundColorR, pagination.foregroundColorG, pagination.foregroundColorB)
        texteStamp.textState.font = FontRepository.findFont(pagination.font)
        texteStamp.textState.fontSize = pagination.fontSize
        return texteStamp
    }
}