# Poisson Rouge

lien du miro : ![https://miro.com/app/board/uXjVORPMtRw=/?invite_link_id=389042144776](https://miro.com/app/board/uXjVORPMtRw=/?invite_link_id=389042144776)

## Outils

Pour mener à bien notre projet, nous avons utilisé divers outils, en voici la liste. 

### Miro

Ce logiciel permet de créer divers tableaux, informations qui pourront être partagées avec tous les membres du projet. Nous y avons noté notre user story, nos maquettes, le schéma de notre système, enfin tout ce qui concerne la conception du produit final.

### Android Studio

L'application Poisson Rouge est écrite en Kotlin, nous nous sommes donc servi d'Android Studio pour la développer. 

### Firebase

Firebase nous a servi à créer un jeu de donnée, de récupérer et enregistrer des informations depuis Android Studio. Ce système de base de données assure des échanges rapide avec l'application Kotlin et possède également un système de notification indispensable à notre produit.

### Raspberry

Poisson rouge est une application qui reçoit des notifications chaque fois que la température d'une aquarium n'est plus idéale pour le poisson qui y vit. Pour cela, il fallait donc un capteur. Afin de faire la liaiso, entre firebase et le capteur, nous avons utilisé une raspberry permet de récupérer les informations envoyées par le capteur et de les retourner jusqu'à Firebase

### Beacon

Nous devions ajouter un système de localisation pour reconnaître l'aquarium sujet à une alerte. Le beacon, pouvant être propre à un aquarium, est un outil idéal pour localiser chaque aquarium. Il peut être détecter depuis l'application Kotlin via bluetooth et localisation du smartphone.

## Code source

### Models

Notre code contient 3 modèles appelés utilisées pour définir les entités de notre produit.
 - Alert : id, temperature détecté, id de l'aquarium concerné, id de l'utilisateur en charge de l'alerte, état (en cours ou finie), description et photo
 - Species : id, nom de l'espèce, température minimal, température maximal
 - Aquarium : id, nom, Species contenu, température actuelle, id du beacon associé

Ce sont ces entités que nous remplirons avec les bases de données Firebase et modifierons en conséquence selon les changements à venir (notamment pour l'entité Alert à qui on ajoutera la description, l'id d'utilisateur, la photo...)

### Adapters

L'application contient deux listes : 
 - La liste des aquariums, dans la page d'accueil (MainActivity)
 - L'historique des alertes dans le detail de l'aquarium (HistoricFragment)

Ces deux listes sont personnalisées grâce à un adapter, lié au fichier XML qui définira le code visuel de chaque occurence présent dans la liste

### Fragments

Lorsqu'un aquarium est sélectionné, on entre dans le détail de ce dernier. Pour afficher ses données, nous avons créer deux fragments, dépendants d'une barre de navigation, le premier contiendra les informations classiques de l'aquarium et l'alerte en cours (s'il y en a une), c'est de là que se traitera l'alerte ;  le second contient l'historique de l'aquarium.

Ces fragments seront appelés depuis l'activité DetailAquariumActivity, qui récupérera toutes les informations nécessaire sur Firebase pour les distribuer dans chaque fragment. 

Ils ont leurs propres fichiers XML associés. 

La barre de navigation a également son fichier XML : menu/nav-menu.xml

### Resources

Les couleurs utilisées sont enregistrés dans le fichier values/colors.xml
Les textes fixes dans values/strings.xml 
Les images, logos... dans le dossier drawable


