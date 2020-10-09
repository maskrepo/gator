package fr.convergence.proddoc.service.surchargeur

import fr.convergence.proddoc.model.SurchargeDemande
import org.apache.commons.io.output.ByteArrayOutputStream
import java.io.InputStream

interface Surchargeur {
    fun appliquerSurcharge(fichierInputStream: InputStream, surchargeDemande: SurchargeDemande): ByteArrayOutputStream
}