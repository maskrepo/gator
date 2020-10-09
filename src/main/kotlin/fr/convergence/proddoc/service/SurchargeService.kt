package fr.convergence.proddoc.service

import fr.convergence.proddoc.model.SurchargeDemande
import fr.convergence.proddoc.service.surchargeur.Surchargeur
import org.apache.commons.io.output.ByteArrayOutputStream
import org.slf4j.LoggerFactory
import java.net.URL
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class SurchargeService(@Inject private val surchargeur: Surchargeur) {

    companion object {
        private val LOG = LoggerFactory.getLogger(SurchargeService::class.java)
    }

    fun appliquerSurcharge(demande: SurchargeDemande): ByteArrayOutputStream {

        LOG.info("Applique une surcharge avec la demande $demande")
        val u = URL(demande.urlDocument)
        u.openStream().use { inputStream ->
            return surchargeur.appliquerSurcharge(inputStream, demande)
        }
    }

}