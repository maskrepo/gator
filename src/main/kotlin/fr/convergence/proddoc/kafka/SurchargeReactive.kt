package fr.convergence.proddoc.kafka

import fr.convergence.proddoc.model.SurchargeDemande
import fr.convergence.proddoc.model.SurchargeReponse
import fr.convergence.proddoc.model.lib.obj.MaskEntete
import fr.convergence.proddoc.model.lib.obj.MaskMessage
import fr.convergence.proddoc.model.lib.obj.MaskReponse
import fr.convergence.proddoc.service.SurchargeService
import fr.convergence.proddoc.util.maskIOHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.slf4j.LoggerFactory
import java.io.File
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class SurchargeReactive(
    @Inject val surchargeService: SurchargeService
) {

    @Inject
    @field: Channel("surcharge_fini")
    val retourEmitter: Emitter<MaskMessage>? = null

    companion object {
        private val LOG = LoggerFactory.getLogger(SurchargeReactive::class.java)
    }

    @Incoming("surcharge")
    fun consume(message: MaskMessage): MaskMessage = maskIOHandler(message) {

        LOG.info("Demande de surcharge")

        GlobalScope.launch {
            val demandeSurcharge = message.recupererObjetMetier<SurchargeDemande>()
            LOG.info("La demande est $demandeSurcharge")

            val documentModifie = surchargeService.appliquerSurcharge(demandeSurcharge)

            val fichierTemp = createTempFile(suffix = ".pdf", directory = File("c:\\TEMP\\"))
            fichierTemp.writeBytes(documentModifie)

            LOG.info(fichierTemp.path)
            retour(message, SurchargeReponse(fichierTemp.path))
        }

    }

    private suspend fun retour(message: MaskMessage, reponseAsync: SurchargeReponse) {

        LOG.info("Reponse asynchrone = $reponseAsync")

        val messageRetour = MaskMessage(
            MaskEntete(
                message.entete.idUnique, message.entete.idLot, LocalDateTime.now(),
                message.entete.idEmetteur, message.entete.idGreffe, message.entete.typeDemande
            ),
            Json.encodeToJsonElement(reponseAsync),
            MaskReponse((true))
        )

        retourEmitter?.send(messageRetour)
    }

}