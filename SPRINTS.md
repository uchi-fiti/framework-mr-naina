Sprint 5:

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

Sprint 6:
- atao dispo API le methode
- annotation pour que methode mamerina json
- refa ataony Object na zavatra hafa le type de retour de coté framework mamadika azy ho JSON (atao toJson)
- sinon raha String de tode iny fotsiny no printena

...upload fichiers, session

DONE