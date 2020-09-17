package fr.convergence.proddoc.service

import fr.convergence.proddoc.model.TypeSurcharge
import fr.convergence.proddoc.service.surchargeur.Surchargeur
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class SurchargeService (@Inject private val surchargeur: Surchargeur) {

    companion object {
        private val LOG = LoggerFactory.getLogger(SurchargeService::class.java)
    }

    fun appliquerSurcharge(fichierOrigine: ByteArray, listeSurcharge: List<TypeSurcharge>): ByteArray {

        var fichierSurcharge = fichierOrigine

        // Applique toutes les surcharges dans l'ordre
        for (typeSurcharge in listeSurcharge) {
            fichierSurcharge = when (typeSurcharge) {
                TypeSurcharge.FILIGRANE_PROVISOIRE -> surchargeur.ajouterFiligrane(fichierSurcharge, "Provisoire")

                TypeSurcharge.RECTO_VERSO -> surchargeur.ajouterPageBlanche(fichierSurcharge)

                TypeSurcharge.COPIE_CERTIFIEE_CONFORME -> surchargeur.ajouterCopieConforme(fichierSurcharge)

                TypeSurcharge.PAGINATION -> surchargeur.ajouterPagination(fichierSurcharge)
            }
        }

        // Retourne le fichier modifié
        return fichierSurcharge
    }

}