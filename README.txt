Exemple d'application JavaEE
============================

Pré-requis
----------

Maven 3+
Java 6, la variable d'environnement JAVA_HOME doit être définit
JBoss 7.x

Modules
-------

commons			modèle métier et définition des services.
ear				packaging de l'application
services		implémenatation des services avec des EJB Stateless
services-test	test d'intégration de la couche service avec Arquillian 

Profiles Maven
--------------

ci					profile utilisé par le serveur d'intégration continue. Il contient en plus le reporting checkstyle et PMD.
jboss-as-remote		profile pour lancer les tests Arquillian sur un JBoss AS 7 en mode Remote. Il est actif par défaut.

FAQ
---

### Comment je fais sour Eclipse pour avoir mes projets ?
Tout d'abord il faut générer les fichiers projets d'Eclipse.
$ cd $PROJECT_HOME
$ mvn eclipse:clean eclipse:eclipse

NB: 
 * le $PROJECT_HOME est le chemin vers la racine du project.
 * le eclipse:clean n'est pas toujours nécessaire mais il évite parfois certaine situation délicate.

Ensuite il suffit d'importer les projets sous Eclipse (Import.../Import Existing Projects)

### Pourquoi séparer le module services-test du module services ?
Eclipse ne sait pas gérer les scopes maven, du coup pour éviter que les dépendances de test se retrouve dans le classpath d'Eclipse on sépare ces modules.

### Comment créer un Archetype maven depuis ce projet ?
$ cd $PROJECT_HOME
$ mvn archetype:create-from-project
$ cd target/generated-sources/archetype/
$ mvn install

### Comment utiliser l'Archetype
$ mvn archetype:generate -DarchetypeGroupId=org.ilaborie.lged -DarchetypeArtifactId=org.ilaborie.lged-archetype

Idées
-----

### Application à la carte
On peut imaginer utiliser les branches de Git pour ajouter des fonctionnalités  puis faire des merges pour construire notre application modèle. On pourrait par exemple avoir une arborescence comme ceci:

master
  |- JPA		utilisation d'une entité, test avec H2 et DBUnit
  |- WS		Exposition des services sous WebServics avec JAX-WS
  |- REST		Exposition des services en REST avec JAX-WS
  |- GWT		Client GWT
  |- Struts	Client en Struts
  |- ExtJS	Client en ExtJS
  \_ Mock 	ajout d'un module implémentant les services en mode bouchon 

Ainsi si on souhaite de faire du GWT+JPA+REST on peut faire un merge des trois branches...
Pour que le résultat fonctionne il faut bien sur ne pas touché au module 'commons'.