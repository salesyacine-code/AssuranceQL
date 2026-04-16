Points clés pour le README
Q : Pourquoi deux mocks (OrderService et OrderDao) ?
Parce que OrderController dépend de OrderService, et OrderService dépend de OrderDao. En mockant les deux, chaque couche est testée isolément.
Q : Différence entre Test 1 et Test 2 ?
Le Test 1 vérifie que le contrôleur appelle correctement le service (avec un mock service). Le Test 2 vérifie que le service appelle correctement le DAO (avec un vrai service + mock DAO).
Q : À quoi sert InOrder ?
Il garantit que les appels se font dans un ordre précis — utile quand l'ordre des opérations a un impact métier.



LE TP3  PARTIE 2
## Tests d'intégration avec Testcontainers

### Approche originale
Les tests existants utilisent H2 (base de données en mémoire) avec des mocks.
**Limitations** : H2 ne se comporte pas exactement comme MySQL (dialecte SQL
différent, contraintes absentes), ce qui peut masquer des bugs de production.

### Nouvelle approche : Testcontainers
Chaque test démarre un vrai conteneur MySQL via Docker, garantissant :
- Un environnement identique à la production
- L'isolation entre tests (base nettoyée avant chaque test)
- La reproductibilité sur toute machine avec Docker

### Nouveau scénario ajouté
- `testListAllTasks()` : vérifie que plusieurs tâches peuvent être persistées
  et listées en une seule requête.

### Comparaison
| Critère         | Tests H2/Mocks     | Testcontainers      |
|-----------------|--------------------|---------------------|
| Réalisme        | Faible             | Élevé               |
| Vitesse         | Rapide             | Plus lent (~30s)    |
| Fiabilité       | Moyenne            | Haute               |
| Maintenance     | Simple             | Nécessite Docker    |
