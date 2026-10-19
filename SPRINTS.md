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
Atao disponible anaty controller lay session Utilisateur
Creer Class Mysession 
    - Map<String , Object> 
    - Method utilitaire pour recuperer ou ajouter variable
Refa mahita an iny ny framework atsika dia injectena

Rehefa vita ny invoke dia atao mis a jour ny session 
