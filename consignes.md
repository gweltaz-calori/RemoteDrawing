# Consignes

Ensembles d'exercices à réaliser dans le but de s'exercer à la réalisation d'application multiplateforme.

Tout du long de l'ensemble de la réalisation de ses consignes vous devrez commenter le code afin que quelqu’un qui ne connaisse pas le projet puisse comprendre.

### 1) Pour débuter
Histoire de prendre en main les différents projets.

##### Sur Android :
- ```Changer la couleur du layout de loading dans le XML```

##### Côté serveur :
- ```Afficher dans la console le nombre de personnes connectés```

### 2) On commences les choses plus sérieuses :
**a)** L'utilisateur doit pouvoir demander une nouvelle couleur quand elle ne lui convient pas.Cette nouvelle couleur sera à nouveau choisie au hasard par le serveur.

##### Sur Android :
- ```Création du setter dans DrawingView pour changer de couleur```

##### Côté serveur :
- ```Route pour recevoir et renvoyer une autre couleur```
- ```Ne pas hésiter à ajouter d'autres couleurs au tableau de couleur```

---
**b)** Le serveur doit attribuer aléatoirement une couleur qui sera la même que celle du background de l'application cliente à un utilisateur. Cet utilisateur sera alors appelé : "L'effaceur". Bien entendu l'utilisateur devra être au courant de qui il est ainsi que d'avoir la possibilité de changer de rôle grâce à l'exercice précédent.

##### Sur Android :
- ```Plus gros pinceau```
- ```Couleur du fond pour le pinceau (illusion d'une gomme)```
- ```Afficher si l'utilisateur est l’eraser ou pas```

##### Côté serveur :
- ```Ajouter une couleur de background correspondant```
- ```Un utilisateur doit pouvoir savoir si il est Eraser ou non```

---
**c)** L'utilisateur doit pouvoir choisir un salon de dessin suivant un `#Hashtag`. Lors du lancement de l'application cliente. L'application lui demande quel salon veut-il rejoindre. Par défaut le salon sera `#general`. (Il n'existe qu'un seul effaceur par salon)

##### Sur Android :
- ```Zone de texte de choix de salon```
- ```Possibilité de changement de salon``` 
- ```Afficher le salon en cours sur l'application```

##### Côté serveur :
- ```Chatroom Socket```
- ```Si le salon n'existe pas, le créer et faire rejoindre l'utilisateur```
- ```Attention à ce qu'un utilisateur ne rejoigne pas 2 fois le même salon``` 

---
**d)** On veux pouvoir surveiller l'activité du serveur via un interface web dédié non sécurisé. Cela comprends au minimum (tout ajouts sera le bienvenu) :
- ```Nombre de chat room en cours```
- ```Nombre d'utilisateur connecté```
//Aide : 
Vous pouvez utiliser soit Express, soit socket.io coté client avec Javascript

---
**e)** Il serait intéressant de rajouter quelques fonctionnalités facécieuse à ce back-end :
- ```Clear tous les dessins```
- ```Clear tous les dessins d'un salon en particulier```
- ```Envoyer un dessin (copier coller des coordonnées)```

---
### 4) Pour aller plus loin :
**a)** L'utilisateur aimerait bien identifier qui dessine en temps réel pour cela : 
- ```Ajouter une fenetre au démarrage pour pouvoir ajouter un pseudo```
- ```Le pseudo doit apparaitre en temps réel avec le trait de dessin d'un utilisateur sur l'écran des autres utilisateurs```

**b)** On veux garder une trace de toutes activitées lié au serveur. Pour cela votre chef de projet décide d'utiliser un SGBD non relationnel : `MongoDB`. Ce SGBD devra :
- ```Enregistrer toutes les couleurs```
- ```Enregistrer les dessins```
- ```Enregistrer les utilisateurs```

### 5) Bonus :
- ```Le serveur peut changer la couleur de fond```
- ```Coder l'application sur iOS```
