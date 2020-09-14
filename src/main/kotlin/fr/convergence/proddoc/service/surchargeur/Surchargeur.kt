package fr.convergence.proddoc.service.surchargeur

interface Surchargeur {

    fun ajouterFiligrane(fichier: ByteArray, texteFiligrane: String?): ByteArray
    fun ajouterPageBlanche(fichier: ByteArray): ByteArray
    fun ajouterCopieConforme(fichier: ByteArray): ByteArray
}