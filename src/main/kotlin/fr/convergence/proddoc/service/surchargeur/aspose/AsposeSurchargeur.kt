package fr.convergence.proddoc.service.surchargeur.aspose

import com.aspose.pdf.Document
import com.aspose.pdf.facades.PdfFileEditor
import fr.convergence.proddoc.service.surchargeur.Surchargeur
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class AsposeSurchargeur (@Inject val asposeHelper: AsposeHelper): Surchargeur {

    companion object {
        private val LOG = LoggerFactory.getLogger(AsposeSurchargeur::class.java)
    }

    override fun ajouterFiligrane(fichier: ByteArray, texteFiligrane: String?): ByteArray {
        LOG.info("Ajout en filigrane du texte $texteFiligrane")
        val stampFiligrane = asposeHelper.genererStampPourFiligrane(texteFiligrane)
        return asposeHelper.ajouterStampSurToutesLesPages(fichier, stampFiligrane)
    }

    override fun ajouterCopieConforme(fichier: ByteArray): ByteArray {
        LOG.info("Ajout copie conforme")
        val stampCopieConforme = asposeHelper.genererStampPourCopieConforme()
        return asposeHelper.ajouterStampSurToutesLesPages(fichier, stampCopieConforme)
    }

    override fun ajouterPageBlanche(fichier: ByteArray): ByteArray {

        var pdfDocument = Document()
        try {
            ByteArrayOutputStream().use { dstStream ->
                ByteArrayInputStream(fichier).use { byteArrayInputStream ->
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