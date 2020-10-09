# Surcharge de document

Service de surcharge d'un document
Pour l'instant prend en compte :
- Ajout d'un filigrane
- Ajout d'une page blanche si le nombre de page est impair
- Ajout de la pagination
- Ajout de la mention "copie conforme"

## Tester l'application en local

http://localhost:8093/test.html

## Application réactive

Topic en entrée : SURCHARGE_DEMANDE

Topic de sortie : SURCHARGE_REPONSE 

Exemple de message de type demande 
```
{
  "entete": {
    "idUnique": "659039e688c23ff08b4f905be07294ab66d600d4",
    "idLot": "12345",
    "dateHeureDemande": "2020-09-23T09:00:00",
    "idEmetteur": "L20019",
    "idReference": "reference2",
    "idGreffe": "0605",
    "typeDemande": "SURCHARGE"
  },
  "objetMetier": {
    "urlDocument": "http://192.168.1.30:8100/convergence-greffe-web/rest/kbis/recupererPdf",
    "filigrane": {
      "texte": "Provisoire"
    },
    "ajoutPageBlanche": true,
    "pagination": {}
  },
  "reponse": {
    "estReponseOk": true,
    "messageErreur": "noerror",
    "stackTrace": "nostracktrace"
  }
}
```
