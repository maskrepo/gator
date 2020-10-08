package fr.convergence.proddoc.service.surchargeur.aspose

import com.aspose.pdf.Document
import com.aspose.pdf.License
import com.aspose.pdf.Page
import com.aspose.pdf.facades.PdfFileEditor
import fr.convergence.proddoc.model.ConfigurationFiligrane
import fr.convergence.proddoc.model.ConfigurationPagination
import fr.convergence.proddoc.model.SurchargeDemande
import fr.convergence.proddoc.service.surchargeur.Surchargeur
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.function.Consumer
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class AsposeSurchargeur(@Inject val asposeHelper: AsposeHelper) : Surchargeur {

    companion object {
        private val LOG = LoggerFactory.getLogger(AsposeSurchargeur::class.java)
        private val license: License = License()
    }

    init {
        Thread.currentThread().contextClassLoader.getResourceAsStream("META-INF/resources/Aspose.Total.Java.lic")
            .use { inputStream -> license.setLicense(inputStream) }
    }

    override fun appliquerSurcharge(fichierInputStream: InputStream, surchargeDemande: SurchargeDemande): ByteArrayOutputStream {

        val document = Document(fichierInputStream)

        try {
            if (surchargeDemande.filigrane != null) {
                this.ajouterFiligrane(document, surchargeDemande.filigrane)
            }

            if (surchargeDemande.pagination != null) {
                this.ajouterPagination(document, surchargeDemande.pagination)
            }

            if (surchargeDemande.ajoutPageBlanche) {
                this.ajouterPageBlanche(document)
            }

            ByteArrayOutputStream().use { byteArrayOutputStream ->
                document.save(byteArrayOutputStream)
                return byteArrayOutputStream
            }
        } finally {
            document.close()
        }
    }

    fun ajouterFiligrane(document: Document, filigrane: ConfigurationFiligrane) {
        LOG.info("Ajout d'un filigrane $filigrane")
        val stampFiligrane = asposeHelper.genererStampPourFiligrane(filigrane)
        return asposeHelper.ajouterStampSurToutesLesPages(document, stampFiligrane)
    }

    fun ajouterCopieConforme(document: Document) {
        LOG.info("Ajout copie conforme")
        val stampCopieConforme = asposeHelper.genererStampPourCopieConforme()
        return asposeHelper.ajouterStampSurToutesLesPages(document, stampCopieConforme)
    }

    fun ajouterPagination(document: Document, pagination: ConfigurationPagination) {
        LOG.info("Ajout pagination")
        val stampCopieConforme = asposeHelper.genererStampPourPagination(pagination)
        document.pages.forEach(Consumer { page: Page ->
            stampCopieConforme.value = "Page ${page.number} sur ${document.pages.size()}"
            page.addStamp(stampCopieConforme)
        })
    }

    fun ajouterPageBlanche(document: Document) {

        document.decrypt()
        document.allowReusePageContent = true
        if (document.pages.size() % 2 == 0) {
            LOG.info("Le document a un nombre pair de page, on ne fait rien")
        } else {
            LOG.info("Le document a un nombre impair de page, on ajoute une page blanche")
            document.pages.add()
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