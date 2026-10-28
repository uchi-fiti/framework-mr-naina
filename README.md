# framework-mr-naina

On va essayer de créer un framework comme Spring "from scratch".

Fonctionnalités actuelles :

- Le framework est utilisé par l'application de test pour :
  - faire apparaître l'URL complète sur le navigateur (Sprint 0)
  - afficher le nom de tous les controllers présents dans le package "controllers" (Sprint 1)
  - vérifier si la route sur laquelle elle va existe (Sprint 2, Sprint 3 et Sprint 3 bis)
    - si oui, le framework appelle la méthode associée à cette URL
    - sinon, le framework affiche toutes les routes disponibles

- Gestion des paramètres des méthodes (Sprint 7)
  - Les méthodes des controllers peuvent recevoir des paramètres simples (`String`, `int`, `Integer`, `long`, `Long`, `double`, `Double`).
  - Le nom de l'input du formulaire doit correspondre au nom du paramètre de la méthode.
  - Le framework convertit automatiquement les valeurs reçues selon leurs types.

- Gestion des Objets et des listes (Sprint 7 bis)
  - Les méthodes des controllers peuvent recevoir des Objets, des listes d'Objets ou des Objets contenant d'autres Objets ou listes.
  - Le framework utilise la Reflection pour instancier les Objets et remplir leurs attributs à l'aide des setters.
  - Convention de nommage des champs de formulaire :
    - Paramètre simple : `String nom` → `name="nom"`
    - Objet : `Personne p` → `name="p.nom"`
    - Objet imbriqué : `p.adresse.ville` → `name="p.adresse.ville"`
    - Liste simple : `List<String> noms` → `name="noms[0]"`
    - Liste d'Objets : `List<Pseudo> pseudos` → `name="pseudos[0].libelle"`
    - Liste dans un Objet : `name="p.pseudos[0].libelle"`

- Upload de fichiers (Sprint 8)
  - Le framework permet l'upload de fichiers à partir d'un formulaire HTML.
  - Le formulaire doit utiliser `enctype="multipart/form-data"`.
  - Le développeur peut utiliser un paramètre de type `MapFile` dans une méthode de controller.
  - `MapFile` contient :
    - `nomFichier` : nom du fichier
    - `extensionFichier` : extension du fichier
    - `contenuFichier` : contenu du fichier sous forme de `byte[]`
  - Le framework récupère le fichier et le sauvegarde localement dans un répertoire défini dans la configuration.

- Gestion des sessions (Sprint 9)
  - Le framework possède une classe personnalisée `USession`.
  - Cette classe contient une `Map<String, Object>` pour stocker les variables de session.
  - Méthodes disponibles :
    - `set(key, value)` : ajouter ou modifier une variable
    - `get(key)` : récupérer une variable
    - `remove(key)` : supprimer une variable
  - Pour accéder à la session, le développeur doit déclarer un paramètre de type `USession` dans la méthode du controller.
  - Le framework injecte automatiquement la session correspondante.
  - L'objet `USession` est stocké directement dans le `HttpSession` de l'utilisateur.
  - Les modifications effectuées avec `session.set()` sont conservées entre les requêtes de la même session.

- Authentification (Sprint 10)
  - Vous pouvez protéger les routes grâce à l'annotation @Auth qui, essentiellement, dit que la methode associée à cette route ne peut pas être accédée ou invoquée que si l'utilisateur est authentifié
  - Pour ce faire, il faut une petite configuration dans web.xml
  - Ajoutez un nouveau paramètre de contexte appelée `auth.session.key` et mettez en valeur la clé de session qui doit exister pour prouver que l'utilisateur est authentifié
  Exemple:
  ```xml
      <context-param>
        <param-name>auth.session.key</param-name>
        <param-value>userId</param-value>
    </context-param>
  ```
- Rôles
  - Vous pouvez aussi protéger les routes pour que seulement les utilisateurs connectés et les utilisateurs avec des rôles spécifiques puissent y accéder
  - Vous pouvez faire ça en ajoutant à l'annotation @Auth la liste des seuls rôles qui peuvent accéder à votre route
  Exemple: `@Auth(roles = {"admin", manager})
  - Il faut une dernière petite configuration
  - Il faut dire au framework quelle clé de session vérifier
  - Donc créer un nouveau paramètre de contexte `role.session.key` qui va avoir comme valeur la clé de session que vous avez créez pour stocker la rôle actuelle
  
## Remarque sur les URLs

- Le `FrontControllerServlet` utilise le mapping `/app/*`.
- Pour accéder aux routes des controllers dans le navigateur, les URLs doivent commencer par `/app` 
  - Exemple : `localhost:8080/Framework-test-1/app/etudiants`
- Les pages JSP placées dans `/WEB-INF/` ne sont pas accessibles directement depuis le navigateur.