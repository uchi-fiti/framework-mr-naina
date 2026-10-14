# framework-mr-naina
On va essayer de créer un framework comme spring "from scratch"
Fonctionnalités actuelles: 
- Le framework est utilisé par l'application de test pour: 
- faire apparaître l'url complet sur le navigateur (on l'a compilé, et on l'a mis dans un .jar) (Sprint 0)
- afficher le nom de tous les controllers présent dans le package "controllers" (Sprint 1)
- vérifier si la route sur laquelle elle va, existe (sprint 2, sprint 3, et sprint 3 bis)
    - si oui, le framwork appelle la méthode associé à cette url
    - sinon, le framework affiche tous les routes disponibles 


- Convention de nommage des champs de formulaire (Sprint 7 bis) :
  - Pour les paramètres simples (`String`, `Integer`, etc.), le `name` de l'input doit correspondre au nom du paramètre de la méthode.
    - Exemple : `String nom` → `name="nom"`
  - Pour les paramètres de type Objet, le `name` doit suivre le format `nomParametre.nomAttribut`.
    - Exemple : `Personne p` → `name="p.nom"`
  - Pour les Objets ayant des attributs de type Objet, on utilise un point (`.`) pour accéder aux attributs.
    - Exemple : `p.adresse.ville` → `name="p.adresse.ville"`
  - Pour les listes, on utilise des crochets (`[]`) avec l'indice de l'élément, en commençant par 0.
    - Exemple : `List<String> noms` → `name="noms[0]"`, `name="noms[1]"`
  - Pour les listes d'Objets, on combine les crochets et les points.
    - Exemple : `List<Pseudo> pseudos` → `name="pseudos[0].libelle"`
  - Cette convention permet au framework d'associer automatiquement les valeurs des formulaires aux paramètres des méthodes des controllers, même pour les Objets et les listes imbriqués.