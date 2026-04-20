Points clés pour le README
Q : Pourquoi deux mocks (OrderService et OrderDao) ?
Parce que OrderController dépend de OrderService, et OrderService dépend de OrderDao. En mockant les deux, chaque couche est testée isolément.
Q : Différence entre Test 1 et Test 2 ?
Le Test 1 vérifie que le contrôleur appelle correctement le service (avec un mock service). Le Test 2 vérifie que le service appelle correctement le DAO (avec un vrai service + mock DAO).
Q : À quoi sert InOrder ?
Il garantit que les appels se font dans un ordre précis — utile quand l'ordre des opérations a un impact métier.



LE TP3  PARTIE 2
exo1
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
# Task Manager — Tests d'intégration avec Testcontainers


EXO2 task-mangaer


## Description du projet

Ce projet est une application Spring Boot de gestion de tâches clonée depuis
[rengreen/task-manager](https://github.com/rengreen/task-manager).
L'objectif principal de ce TP est de réécrire les tests d'intégration existants
en utilisant la bibliothèque **Testcontainers Java** et **Docker**.

---

## Structure du projet

```
src/
├── main/java/pl/rengreen/taskmanager/
│   ├── configuration/        # Sécurité et MVC
│   ├── controller/           # Controllers Spring MVC
│   ├── dataloader/           # Chargement des données initiales
│   ├── model/                # Entités JPA (Task, User, Role)
│   ├── repository/           # Repositories Spring Data JPA
│   ├── service/              # Services métier
│   └── TaskManagerApplication.java
└── test/java/pl/rengreen/taskmanager/
    ├── controller/
    │   └── TaskControllerTest.java     # Réécrit avec Testcontainers
    ├── service/
    │   ├── TaskServiceImplTest.java    # Réécrit avec Testcontainers
    │   └── UserServiceImplTest.java    # Réécrit avec Testcontainers
    └── TaskManagerApplicationTests.java
```

---

## Prérequis

- Java 8+
- Maven 3.6+
- Docker installé et en cours d'exécution (`docker ps` doit fonctionner)
- L'utilisateur doit avoir accès au socket Docker :
  ```bash
  sudo usermod -aG docker $USER
  newgrp docker
  ```

---

## Lancer les tests

```bash
mvn test
```

Pour lancer un test spécifique :

```bash
mvn test -Dtest=TaskServiceImplTest
mvn test -Dtest=UserServiceImplTest
mvn test -Dtest=TaskControllerTest
```

---

## Analyse des tests existants

### Approches utilisées dans les tests originaux

#### 1. Tests unitaires avec Mockito (`TaskServiceImplTest`, `UserServiceImplTest`)

Les services sont testés en isolation totale grâce aux annotations `@Mock` et
`@InjectMocks` de Mockito. Le repository est simulé — aucune base de données
n'est sollicitée.

```java
@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskServiceImpl taskServiceMock;

    @Test
    public void findAll_shouldReturnTasksList() {
        when(taskRepository.findAll()).thenReturn(expectedTasks);
        assertThat(taskServiceMock.findAll()).isEqualTo(expectedTasks);
    }
}
```

#### 2. Tests de controller avec MockMvc standalone (`TaskControllerTest`)

Le controller est testé sans démarrer de vrai serveur ni de base de données.
MockMvc simule les requêtes HTTP et les dépendances sont mockées avec Mockito.

```java
mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
```

#### 3. Test de chargement du contexte (`TaskManagerApplicationTests`)

Un simple test vérifiant que le contexte Spring démarre sans erreur,
avec H2 comme base de données en mémoire.

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskManagerApplicationTests {
    @Test
    public void contextLoads() { }
}
```

---

### Limitations des tests existants

| Limitation | Description |
|---|---|
| **Base H2 ≠ MySQL** | H2 est une base en mémoire qui ne se comporte pas exactement comme MySQL. Certaines requêtes SQL spécifiques à MySQL peuvent passer avec H2 et échouer en production. |
| **Mocks ≠ réalité** | Les tests Mockito ne vérifient pas les vraies requêtes JPA ni les contraintes de la base de données (unicité, NOT NULL, clés étrangères). |
| **Pas de test de repository** | Aucun test ne vérifie directement les requêtes `TaskRepository` ou `UserRepository`. |
| **Couverture fonctionnelle faible** | Un seul scénario par service (`findAll`). Pas de test de création, suppression ou mise à jour réelle. |
| **contextLoads() ne teste rien** | Ce test passe toujours si le contexte démarre, même si la logique métier est cassée. |
| **MockMvc standalone incomplet** | Sans le contexte de sécurité complet, certains comportements liés à Spring Security ne sont pas testés. |

---

## Tests réécrits avec Testcontainers

### Principe de fonctionnement

Testcontainers démarre automatiquement un vrai conteneur **MySQL 8.0** via
Docker avant chaque classe de test, et l'arrête à la fin. Spring Boot se
connecte dynamiquement à ce conteneur grâce à `@DynamicPropertySource`.

```java
@SpringBootTest
@Testcontainers
public class TaskServiceImplTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("taskmanager")
            .withUsername("root")
            .withPassword("password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
}
```

### Scénarios couverts

#### TaskServiceImplTest

| Méthode de test | Scénario | Nouveau ? |
|---|---|---|
| `findAll_shouldReturnTasksList` | Vérifier que la liste des tâches est retournée depuis MySQL | Non (réécrit) |
| `createTask_shouldPersistTaskInDatabase` | Créer une tâche et vérifier sa persistance réelle | Non (réécrit) |
| `deleteTask_shouldRemoveTaskFromDatabase` | Supprimer une tâche et vérifier qu'elle n'existe plus | Non (réécrit) |
| `setTaskCompleted_shouldMarkTaskAsCompleted` | Marquer une tâche comme terminée et vérifier le flag | **Oui (nouveau)** |

#### UserServiceImplTest

| Méthode de test | Scénario | Nouveau ? |
|---|---|---|
| `findAll_shouldReturnUsersList` | Vérifier que la liste des utilisateurs est retournée | Non (réécrit) |
| `getUserByEmail_shouldReturnCorrectUser` | Récupérer un utilisateur par son email | Non (réécrit) |
| `createUser_shouldPersistUserInDatabase` | Créer un utilisateur et vérifier sa persistance | **Oui (nouveau)** |

#### TaskControllerTest

| Méthode de test | Scénario | Nouveau ? |
|---|---|---|
| `updateTask_shouldReturnStatusOkAndFilledTaskForm` | Vérifier la vue d'édition d'une tâche | Non (réécrit) |
| `deleteTask_shouldRedirectToTasks` | Vérifier la redirection après suppression | Non (réécrit) |
| `listTasks_shouldReturnTasksView` | Vérifier que la liste des tâches s'affiche | **Oui (nouveau)** |

---

## Comparaison : tests existants vs tests réécrits

### Couverture

| Critère | Tests originaux | Tests Testcontainers |
|---|---|---|
| Base de données réelle | Non (H2 / Mock) | **Oui (MySQL 8.0)** |
| Test du repository JPA | Non | **Oui** |
| Test de persistance réelle | Non | **Oui** |
| Test de suppression réelle | Non | **Oui** |
| Contexte Spring complet | Partiel | **Oui** |
| Spring Security réelle | Non | **Oui** |
| Nombre de scénarios | 4 | **10** |

### Lisibilité

Les tests Testcontainers sont légèrement plus verbeux à cause de la
configuration du conteneur (`@Container`, `@DynamicPropertySource`), mais
chaque test est plus explicite sur ce qu'il teste réellement. Le pattern
**Arrange / Act / Assert** est clairement visible dans chaque méthode.

### Maintenabilité

Testcontainers isole chaque test dans son propre environnement Docker. Si la
base de données évolue (nouveau champ, nouvelle contrainte), les tests
Testcontainers le détecteront immédiatement. Avec H2 et les mocks, ces
régressions passeraient inaperçues.

### Fiabilité

Les tests Testcontainers sont plus fiables car ils s'exécutent contre une vraie
instance MySQL identique à celle de production. Les résultats sont
reproductibles sur n'importe quelle machine ayant Docker installé.

---

## Avantages et inconvénients de Testcontainers

### Avantages

- **Fidélité** : même base de données qu'en production (MySQL, PostgreSQL, etc.)
- **Isolation** : chaque exécution repart d'un état propre
- **Portabilité** : fonctionne sur toute machine avec Docker, sans installation de base de données locale
- **Détection précoce** : les bugs liés au dialecte SQL ou aux contraintes sont détectés dès les tests
- **Automatisation** : démarrage et arrêt du conteneur entièrement gérés par la bibliothèque

### Inconvénients

- **Vitesse** : les tests sont plus lents car Docker doit démarrer un conteneur (~10-30 secondes)
- **Dépendance Docker** : Docker doit être installé et accessible sur la machine de CI/CD
- **Ressources** : chaque test consomme de la mémoire et du CPU pour le conteneur
- **Complexité** : configuration initiale plus longue qu'avec H2

---

## Nouveau scénario ajouté

### `setTaskCompleted_shouldMarkTaskAsCompleted` (TaskServiceImplTest)

Ce scénario teste la fonctionnalité de complétion d'une tâche, absente des
tests originaux. Il vérifie que :

1. Une tâche est créée avec `completed = false`
2. La méthode `setTaskCompleted(id)` est appelée
3. La tâche est récupérée depuis la base de données
4. Le champ `completed` vaut bien `true`

Ce scénario est important car il couvre un cas métier central de l'application
(marquer une tâche comme terminée) qui n'était pas du tout testé.

### `createUser_shouldPersistUserInDatabase` (UserServiceImplTest)

Ce scénario vérifie la création et la persistance d'un nouvel utilisateur dans
MySQL, y compris le hachage du mot de passe par Spring Security.

### `listTasks_shouldReturnTasksView` (TaskControllerTest)

Ce scénario teste l'endpoint GET `/tasks` avec un utilisateur authentifié et
vérifie que la vue correcte est retournée avec le contexte Spring Security
complet, ce qui n'était pas couvert par le MockMvc standalone original.

---

## Références

- [Testcontainers Java — Documentation officielle](https://java.testcontainers.org/)
- [Spring Boot Test — Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/test-auto-configuration.html)
- [Projet original rengreen/task-manager](https://github.com/rengreen/task-manager)
