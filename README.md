Points clés pour le README
Q : Pourquoi deux mocks (OrderService et OrderDao) ?
Parce que OrderController dépend de OrderService, et OrderService dépend de OrderDao. En mockant les deux, chaque couche est testée isolément.
Q : Différence entre Test 1 et Test 2 ?
Le Test 1 vérifie que le contrôleur appelle correctement le service (avec un mock service). Le Test 2 vérifie que le service appelle correctement le DAO (avec un vrai service + mock DAO).
Q : À quoi sert InOrder ?
Il garantit que les appels se font dans un ordre précis — utile quand l'ordre des opérations a un impact métier.
