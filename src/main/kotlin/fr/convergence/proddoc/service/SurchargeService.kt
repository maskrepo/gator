package fr.convergence.proddoc.service

import com.aspose.pdf.*
import com.aspose.pdf.facades.PdfFileEditor
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.function.Consumer
import javax.enterprise.context.RequestScoped

@RequestScoped
class SurchargeService {

    companion object {
        private val LOG = LoggerFactory.getLogger(SurchargeService::class.java)
    }

    fun genererFiligrane(fichier: ByteArray, texteFiligrane: String?): ByteArray {

        LOG.info("Ajout en filigrane du texte $texteFiligrane")

        val textePagination = TextStamp(texteFiligrane)
        textePagination.rotateAngle = 45.0
        textePagination.textState.font = FontRepository.findFont("Times-Roman")
        textePagination.textState.fontSize = 100f
        textePagination.textState.foregroundColor = Color.getLightGray()
        textePagination.textState.setFontStyle(FontStyles.Bold)
        textePagination.horizontalAlignment = HorizontalAlignment.Center
        textePagination.verticalAlignment = VerticalAlignment.Center
        textePagination.opacity = 0.5

        val document = Document(ByteArrayInputStream(fichier))
        try {
            document.pages.forEach(Consumer { page: Page ->
                page.addStamp(textePagination)
            })

            ByteArrayOutputStream().use { byteArrayOutputStream ->
                document.save(byteArrayOutputStream)
                return byteArrayOutputStream.toByteArray()
            }
        } finally {
            document.close()
        }
    }

    fun ajoutPageBlancheSiNbPageImpair(doc: ByteArray): ByteArray {

        var pdfDocument = Document()
        try {
            ByteArrayOutputStream().use { dstStream ->
                ByteArrayInputStream(doc).use { byteArrayInputStream ->
                    pdfDocument = Document(byteArrayInputStream)
                    pdfDocument.decrypt()
                    pdfDocument.allowReusePageContent = true
                    return if (pdfDocument.pages.size() % 2 == 0) {
                        LOG.info("Le document a un nombre pair de page, on ne fait rien")
                        pdfDocument.save(dstStream)
                        dstStream.toByteArray()
                    } else {
                        LOG.info("Le document a un nombre impair de page, on ajoute une page blanche")
                        pdfDocument.pages.add()
                        pdfDocument.save(dstStream)
                        dstStream.toByteArray()
                    }
                }
            }
        } finally {
            pdfDocument.close()
        }
    }

    fun concatenerDocuments(fichiers: List<ByteArray>): ByteArray {

        if (fichiers.size == 1) {
            LOG.info("Il y a un seul document, on le renvoie tel quel")
            return fichiers[0]
        }

        val listeDocuments = arrayOfNulls<Document>(fichiers.size)
        for ((index, fichier) in fichiers.withIndex()) {
            ByteArrayInputStream(fichier).use { byteArrayInputStream ->
                val pdfDocument = Document(byteArrayInputStream)
                listeDocuments[index] = pdfDocument
            }
        }

        val pdfDocumentFinal = Document()
        try {
            ByteArrayOutputStream().use { dstStream ->
                val pdfFileEditor = PdfFileEditor()
                pdfFileEditor.concatenate(listeDocuments, pdfDocumentFinal)
                pdfDocumentFinal.save(dstStream)
                return dstStream.toByteArray()
            }
        } finally {
            pdfDocumentFinal.close()
            for (document in listeDocuments) {
                document?.close()
            }
        }
    }
}