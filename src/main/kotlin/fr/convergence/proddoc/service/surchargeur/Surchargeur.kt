package fr.convergence.proddoc.service.surchargeur

import fr.convergence.proddoc.model.ConfigurationFiligrane
import fr.convergence.proddoc.model.ConfigurationPagination

interface Surchargeur {
    fun ajouterFiligrane(fichier: ByteArray, filigrane: ConfigurationFiligrane): ByteArray
    fun ajouterPageBlanche(fichier: ByteArray): ByteArray
    fun ajouterCopieConforme(fichier: ByteArray): ByteArray
    fun ajouterPagination(fichier: ByteArray, pagination: ConfigurationPagination): ByteArray
}