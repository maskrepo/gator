package fr.convergence.proddoc.model

import kotlinx.serialization.Serializable

@Serializable
class SurchargeDemande (val urlDocument: String,
                        val ajoutPageBlanche: Boolean = false,
                        val filigrane: ConfigurationFiligrane? = null,
                        val pagination: ConfigurationPagination? = null) {

    override fun toString(): String {
        return "SurchargeDemande(urlDocument='$urlDocument', ajoutPageBlanche=$ajoutPageBlanche, filigrane=$filigrane)"
    }

}