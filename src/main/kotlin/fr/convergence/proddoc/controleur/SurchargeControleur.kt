package fr.convergence.proddoc.controleur

import com.aspose.pdf.Document
import fr.convergence.proddoc.model.ConfigurationFiligrane
import fr.convergence.proddoc.model.ConfigurationPagination
import fr.convergence.proddoc.service.surchargeur.aspose.AsposeSurchargeur
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import org.jboss.resteasy.annotations.providers.multipart.PartType
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType


@Path("/testSurcharge")
class SurchargeControleur @Inject constructor(val surchargeur: AsposeSurchargeur) {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/pdf")
    fun testSurcharge(@MultipartForm fichier: MultipartBodySurcharge): ByteArray? {

        val document = Document(fichier.file)

        try {
            if (fichier.pageBlanche == "true") {
                surchargeur.ajouterPageBlanche(document)
            }

            if (fichier.filigrane == "true") {
                val filigrane = ConfigurationFiligrane(fichier.texteFiligrane!!)
                surchargeur.ajouterFiligrane(document, filigrane)
            }

            if (fichier.copieConforme == "true") {
                surchargeur.ajouterCopieConforme(document)
            }

            if (fichier.pagination == "true") {
                val pagination = ConfigurationPagination()
                surchargeur.ajouterPagination(document, pagination)
            }

            ByteArrayOutputStream().use { byteArrayOutputStream ->
                document.save(byteArrayOutputStream)
                return byteArrayOutputStream.toByteArray()
            }

        } finally {
            document.close()
        }
    }
}

class MultipartBodySurcharge {
    @FormParam("monFichier")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    var file: InputStream? = null

    @FormParam("filigrane")
    @PartType(MediaType.TEXT_PLAIN)
    var filigrane: String? = null

    @FormParam("texteFiligrane")
    @PartType(MediaType.TEXT_PLAIN)
    var texteFiligrane: String? = null

    @FormParam("pageBlanche")
    @PartType(MediaType.TEXT_PLAIN)
    var pageBlanche: String? = null

    @FormParam("copieConforme")
    @PartType(MediaType.TEXT_PLAIN)
    var copieConforme: String? = null

    @FormParam("pagination")
    @PartType(MediaType.TEXT_PLAIN)
    var pagination: String? = null
}
