package fr.convergence.proddoc.model

import kotlinx.serialization.Serializable

@Serializable
class SurchargeReponse (val urlDocument: String) {

    override fun toString(): String {
        return "SurchargeReponse(urlDocument='$urlDocument')"
    }
}