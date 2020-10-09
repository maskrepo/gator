package fr.convergence.proddoc.reactive

import fr.convergence.proddoc.model.SurchargeDemande
import fr.convergence.proddoc.model.SurchargeReponse
import fr.convergence.proddoc.model.lib.obj.MaskMessage
import fr.convergence.proddoc.model.metier.FichierStocke
import fr.convergence.proddoc.service.SurchargeService
import fr.convergence.proddoc.util.stinger.StingerUtil
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.slf4j.LoggerFactory.getLogger
import java.io.InputStream
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class SurchargeReactive(
    @Inject var surchargeService: SurchargeService,
    @Inject val stingerUtil: StingerUtil
) {

    @Inject
    @field: Channel("surcharge_fini")
    val retourEmitter: Emitter<MaskMessage>? = null

    companion object {
        private val LOG = getLogger(SurchargeReactive::class.java)
    }

    @Incoming("surcharge")
    fun consume(messageOrigine: MaskMessage) {

        LOG.info("Demande de surcharge")

        val demandeSurcharge = messageOrigine.recupererObjetMetier<SurchargeDemande>()
        LOG.info("La demande est $demandeSurcharge")

        stingerUtil.stockerResultatSurStinger(
            messageOrigine,
            this::getPdfSurcharge,
            this::envoyerMessageRetour,
            "application/pdf",
            "toto"
        )
    }

    fun getPdfSurcharge(maskMessage: MaskMessage): InputStream {
        val demandeSurcharge = maskMessage.recupererObjetMetier<SurchargeDemande>()
        return surchargeService.appliquerSurcharge(demandeSurcharge).toInputStream()
    }

    fun envoyerMessageRetour(messageIn: MaskMessage) {

        retourEmitter?.send(
            try {
                val fichierEnCache = messageIn.recupererObjetMetier<FichierStocke>()
                val urlKbis = fichierEnCache.urlAcces
                LOG.debug("Réception évènement Surcharge stocké : ${fichierEnCache.urlAccesNavigateur}")
                MaskMessage.reponseOk(SurchargeReponse(urlKbis).toString(), messageIn, messageIn.entete.idReference)

            } catch (ex: Exception) {
                MaskMessage.reponseKo<Exception>(ex, messageIn, messageIn.entete.idReference)
            }
        )
    }
}