# Sprint 5:

methode d'action retourne String
parametre : ModelAndView

creation methode
    setAttributes(Map)
    addAttribute("attribute", object)

String Object
miantso map.put

manampy paramatre
    - Prefixe
    - suffixe
invoken methode de recuperena ny valeur de retour 

----------------------------------------------------------
    attribut 
        map
        Url
    alaina leh url de concatenena amin suffixe sy prefixe anaty param
    dispatcherforward

--------------------------------------------------------------
Sprint 5 bis:
- Declarer listenerspring ( efa misy ao : demarrage instance container)
    -> web.xml no ideclarena azy  

 <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/applicationContext.xml</param-value>
</context-param>

<!-- Déclaration du listener qui démarre le conteneur Spring -->
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
- Comment avoir une instance d'un container spring ?

# Sprint 6: 4 octobre
- atao dispo API le methode
- annotation pour que methode mamerina json
- refa ataony Object na zavatra hafa le type de retour de coté framework mamadika azy ho JSON (atao toJson)
- sinon raha String de tode iny fotsiny no printena

...upload fichiers, session

# Sprint 7: binding 11 octobre
- formulaire
- matchena ilay request.getParameter sy ny parametres an'ilay methode
- request.getParameter <-> anararanle parametre anle fonction voaantso
- atao null par defaut ny valeur nle parametre

Next: aona zany ny anaovana anle objet ary?

# Sprint 7 bis: binding objet 
- mety misy argument objet: ohatra hoe Employe

Les methodes des controllers peuvent prendre des Objets comme arguemnt
Mais aussi des listes d'objets, ou des Objets ayant des attributs Objets ou de liste d'objets 

exemple 

class Personne {

String nom;
List<Pseudo> pseudos;

}

avec :

class Pseudo {
  String libelle
}
doit marcher
donc on doit pouvoir faire 

ModelView test(List<String> randomArg, Personne p, String randomInput) par exemple

# Sprint 8: upload fichiers:

- si on veut uploader un fichier avec un formulaire, l'input avec le type "file" va correspondre au paramètre de fonction MapFile
- MapFile est une classe avec les attributs: String nomFichier, String extensionFichier et byte [] contenuFichier
- on sauvegarde le fichier localement suivant une localisation déclarée dans un fichier de configuration (comme web.xml ou un fichier custom de notre choix, comme nom_du_projet.properties par exemple)

# Sprint 9
les sessions doivent être disponible dans les controllers
Creer classe session personnaliseé (USession)
    - Map<String , Object> 
    - Methodes utilitaires pour recuperer ou ajouter variable (set, get, etc)
Si le developpeur veut utiliser une session dans un controller, il doit la mettre en parametre
Le framework s'en charge de "injecter" la session dans le controller pour que le developpeur a accès à ses valeurs 
Comment stocker les valeurs?
- request.getContext ou
- request.getSession

On met a jour la session dans invoke s'il y a des mis a jours (session.set())

# Sprint 10
- misy methode d'action any anaty Controller an'ny developpeur
- tenenina hoe tsy maintsy connecté vao afaka mi-executer anle methode (authentifié)
- asiana configuration anaty web.xml hoe inona no anaranle variable session jerena refa tina ho fantatra hoe authentifié ve le olona refa mapiasa anle methode
- init-param anaty web.xml
- de indray mande fotsiny alaina le izy zany hoe anaty fonction init  (context-param)
**french version**
- on peut dire à une methode d'un controller qu'on doit être authentifié pour l'accéder
- on peut faire cela à l'aide d'une annotation personnalisé Auth par exemple
- pour savoir si l'utilisateur est authentifié quand il utilise la methode, on crée une configuration web.xml qui mentionne quelle variable session on regarde (context-param)
- on les récupère juste une fois (les configurations), donc dans la fonction init de FrontControllerServlet
# Sprint 10 bis
- tsy hoe connecté fotsiny fa tsy maintsy manana role specifique