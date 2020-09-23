package fr.convergence.proddoc.service

import fr.convergence.proddoc.model.DemandeSurcharge
import fr.convergence.proddoc.service.surchargeur.Surchargeur
import org.slf4j.LoggerFactory
import java.net.URL
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class SurchargeService (@Inject private val surchargeur: Surchargeur) {

    companion object {
        private val LOG = LoggerFactory.getLogger(SurchargeService::class.java)
    }

    fun appliquerSurcharge(demande: DemandeSurcharge): ByteArray {

        val u = URL(demande.urlDocument)
        var fichierSurcharge: ByteArray

        u.openStream().use { inputStream -> fichierSurcharge = inputStream.readBytes() }

        if (demande.filigrane != null) {
            fichierSurcharge = surchargeur.ajouterFiligrane(fichierSurcharge, demande.filigrane)
        }

        if (demande.pagination != null) {
            fichierSurcharge = surchargeur.ajouterPagination(fichierSurcharge, demande.pagination)
        }

        if (demande.ajoutPageBlanche) {
            fichierSurcharge = surchargeur.ajouterPageBlanche(fichierSurcharge)
        }


        // Retourne le fichier modifié
        return fichierSurcharge
    }

}