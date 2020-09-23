package fr.convergence.proddoc.kafka

import fr.convergence.proddoc.model.DemandeSurcharge
import fr.convergence.proddoc.model.lib.obj.MaskMessage
import fr.convergence.proddoc.service.SurchargeService
import fr.convergence.proddoc.util.maskIOHandler
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import org.slf4j.LoggerFactory
import java.io.File
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class SurchargeReactive (@Inject val surchargeService: SurchargeService) {

    companion object {
        private val LOG = LoggerFactory.getLogger(SurchargeReactive::class.java)
    }

    @Incoming("surcharge")
    @Outgoing("surcharge_fini")
    fun consume(message: MaskMessage): MaskMessage = maskIOHandler(message) {

        LOG.info("Demande de surcharge")

        val demandeSurcharge = message.recupererObjetMetier<DemandeSurcharge>()
        LOG.info("La demande est $demandeSurcharge")

        val documentModifie = surchargeService.appliquerSurcharge(demandeSurcharge)

        val fichierTemp = createTempFile(suffix = ".pdf", directory = File("c:\\TEMP\\"))
        fichierTemp.writeBytes(documentModifie)

        LOG.info(fichierTemp.path)

        "OK"
    }
}