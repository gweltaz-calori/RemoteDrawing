# Consignes

Ensembles d'exercices à réaliser dans le but de s'exercer à la réalisation d'application multiplateforme.

Tout du long de l'ensemble de la réalisation de ses consignes vous devrez commenter le code afin que quelqu’un qui ne connaisse pas le projet puisse comprendre.

### 1) Pour débuter
Histoire de prendre en main les différents projets.
###### Sur Android :
- ```Changer la couleur du layout de loading dans le XML```

###### Côté serveur :
- ```Afficher dans la console le nombre de personnes connectés```

### 2) On commences les choses plus sérieuses :
**a)** L'utilisateur doit pouvoir demander une nouvelle couleur quand elle ne luit convient pas.Cette nouvelle couleur sera à nouveau choisi au hazard par le serveur.
##### Sur Android :
- ```Création du setter dans la DrawingView pour changer de couleur```

##### Côté serveur :
- ```Nouvelle route pour recevoir et renvoyer une autre couleur```

---
**b)** Le serveur doit attribué aléatoirement une couleurs qui sera la même que celle du background de l'application cliente. Cet utilisateur sera alors appelé : "L'effaceur". Bien entendu l'utilisateur devra être au courant de qui il est ainsi que d'avoir la possibilité de changer de rôle grâce à l'erxice précèdent. 

##### Sur Android :
- ```Plus gros pinceau + couleur du background + afficher si on est l’eraser ou pas```

##### Côté serveur :
- ```Ajouter une couleur de background correspondant```

---
**c)** L'utilisateur doit pouvoir choisir un salon de dessin suivant un `#Hashtag`. Lors du lancement de l'application cliente. L'application lui demande quel salon veut-il rejoindre. Par défaut le salon sera `#general`

##### Sur Android :
- ```Zone de texte de choix de salon```

##### Côté serveur :
- ```Chatroom Socket```

---
**d)** On veux pouvoir monitorer l'activité du serveur via un interface web dédié non sécurisé. Cela comprends au minimum (tout ajouts sera le bienvenu) :
- ```Nombre de chat room en cours```
- ```Nombre de User connecté```

---
**e)** Il serait intéressant de rajouter quelques fonctionnalité facécieuse à ce back-end :
- ```Clear tous les dessins```
- ```Clear tous les dessins d'un salon en particulier```
- ```Envoyer un dessin (copier coller des coordonnées)```

---

### 4) Pour aller plus loin :
**a)** On veux garder une trace de toutes activitées lié au serveur. Pour cela votre chef de projet décide d'utiliser un SGBD non relationnel : `MongoDB`. Ce SGBD devra :
- ```Enregistrer toutes les couleurs```
- ```Enregistrer les dessins```
- ```Enregistrer les utilisateurs```

### 5) Bonus :
- ```Le serveur peut changer la couleur de fond```
