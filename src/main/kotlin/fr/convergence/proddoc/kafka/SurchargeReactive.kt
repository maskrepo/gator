package fr.convergence.proddoc.kafka

import fr.convergence.proddoc.model.SurchargeDemande
import fr.convergence.proddoc.model.SurchargeReponse
import fr.convergence.proddoc.model.lib.obj.MaskMessage
import fr.convergence.proddoc.service.SurchargeService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.slf4j.LoggerFactory
import java.io.File
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class SurchargeReactive(
    @Inject var surchargeService: SurchargeService
) {

    @Inject
    @field: Channel("surcharge_fini")
    val retourEmitter: Emitter<MaskMessage>? = null

    companion object {
        private val LOG = LoggerFactory.getLogger(SurchargeReactive::class.java)
    }

    @Incoming("surcharge")
    fun consume(messageOrigine: MaskMessage) {

        LOG.info("Demande de surcharge")

        GlobalScope.launch {
            retour(
                try {
                    val demandeSurcharge = messageOrigine.recupererObjetMetier<SurchargeDemande>()

                    LOG.info("La demande est $demandeSurcharge")

                    val documentModifie = surchargeService.appliquerSurcharge(demandeSurcharge)

                    val fichierTemp = createTempFile(suffix = ".pdf", directory = File("c:\\TEMP\\"))
                    fichierTemp.writeBytes(documentModifie)

                    LOG.info(fichierTemp.path)
                    MaskMessage.reponseOk(SurchargeReponse(fichierTemp.path), messageOrigine, messageOrigine.entete.idReference)

                } catch (ex: Exception) {
                    MaskMessage.reponseKo<Exception>(ex, messageOrigine, messageOrigine.entete.idReference)
                }
            )
        }

    }

    private suspend fun retour(message: MaskMessage) {

        LOG.info("Reponse asynchrone = $message")
        retourEmitter?.send(message)
    }

}