# Surcharge de document

Service de surcharge d'un document
Pour l'instant prend en compte :
- Ajout d'un filigrane
- Ajout d'une page blanche si le nombre de page est impair
- Ajout de la pagination
- Ajout de la mention "copie conforme"

## Tester l'application en local

http://localhost:8081/test.html

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
    "idReference": "reference",
    "idGreffe": "0605",
    "typeDemande": "SURCHARGE"
  },
  "objetMetier": {
    "urlDocument": "http://127.0.0.1:8080/kbis/pdfnumgestion/2012B00025",
    "filigrane": {
      "texte": "Provisoire"
    },
    "pagination": {},
    "ajoutPageBlanche": true
  },
  "reponse": {
    "estReponseOk": false,
    "messageErreur": "noerror",
    "stackTrace": "nostracktrace"
  }
}
```
