package fr.convergence.proddoc.model

import kotlinx.serialization.Serializable

@Serializable
class DemandeSurcharge (val urlDocument: String,
                        val ajoutPageBlanche: Boolean = false,
                        val filigrane: ConfigurationFiligrane?,
                        val pagination: ConfigurationPagination?) {

    override fun toString(): String {
        return "SurchargeDemande(urlDocument='$urlDocument', ajoutPageBlanche=$ajoutPageBlanche, filigrane=$filigrane)"
    }

}