package fr.convergence.proddoc.controleur

import fr.convergence.proddoc.service.SurchargeService
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import org.jboss.resteasy.annotations.providers.multipart.PartType
import java.io.InputStream
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType


@Path("/testConcatenation")
class ConcatenationControleur @Inject constructor(val surchargeService: SurchargeService) {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/pdf")
    fun testSurcharge(@MultipartForm fichiers: MultipartBodyConcatenation): ByteArray? {

        val listeFichiers = mutableListOf<ByteArray>()
        listeFichiers.add(fichiers.file1!!.readBytes())
        listeFichiers.add(fichiers.file2!!.readBytes())

        return surchargeService.concatenerDocuments(listeFichiers)
    }
}

class MultipartBodyConcatenation {
    @FormParam("fichier1")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    var file1: InputStream? = null

    @FormParam("fichier2")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    var file2: InputStream? = null
}
