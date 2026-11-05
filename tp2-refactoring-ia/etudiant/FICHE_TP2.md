# TP2 — Refactoring et évolution assistés par IA

**Objectif.** Vous allez faire évoluer le même petit programme (CaveManager, une cave à vins) de deux façons : d'abord en
demandant simplement à une IA, puis après l'avoir cadrée avec des *guidelines* (consignes permanentes), un refactoring
et des tests. Vous mesurerez ensuite l'écart avec des chiffres, pas avec une impression. Travail individuel, entraide permise.

## 1. Matériel

Dézippez `cave-depart.zip` : vous obtenez `cave/` (le projet) et `outils/` (le vérificateur de comportement). **Travaillez dans votre propre dossier, hors de tout dépôt existant** : le TP ne demande aucun outil de gestion de versions, et vos sauvegardes de phase sont gérées par `scripts/suivi.py`.
Prérequis : JDK 17+, Maven 3.8+, Python 3.9+. Un chat gratuit suffit ; un agent de code (qui lit et écrit vos fichiers) est un plus.

Depuis le dossier `cave/` (terminal bash sous macOS ou Linux ; sous Windows sans bash, utilisez les commandes `cmd` de `cave/README.md`) :

| Pour... | Commande |
| --- | --- |
| compiler / tester | `mvn -q compile` / `mvn -q test` |
| tests + couverture (rapport dans `target/site/jacoco/`) | `mvn -q verify` |
| lancer le programme | `java -cp target/classes fr.cavemanager.Main 12` |
| vérifier le comportement | `scripts/verifier.sh refacto` ou `scripts/verifier.sh r1` |
| mesurer la qualité du code | `python3 scripts/metrics.py` |
| sauvegarder la fin d'une phase | `python3 scripts/suivi.py sauvegarder 1` (1 à 5) |
| taille de votre modification | `python3 scripts/suivi.py diff depart 1` (ou `diff 4 5`) |
| revenir au départ ou à une phase | `python3 scripts/suivi.py restaurer depart` (ou `restaurer 4`) |
| vos tests sur le code de départ | `scripts/tests-sur-depart.sh` |
| fichier unique pour un chat | `python3 scripts/aplatir.py` (produit `cave-aplati.txt`) |

**`scripts/verifier.sh [mode]`** : le mode dépend de la phase.

| Phase | Commande | Résultat attendu à la fin de la phase |
| --- | --- | --- |
| 1 et 5 | `scripts/verifier.sh r1` | tout au vert (au départ, « DIFFÉRENT » est normal : R1 n'existe pas encore) |
| 3 et 4 | `scripts/verifier.sh refacto` (ou sans argument) | tout au vert, avant comme après votre travail |
| bonus | `scripts/verifier.sh r2` | tout au vert |

Le script s'arrête avec le code 0 si tout est au vert, 1 si un contrôle échoue, 2 s'il ne peut pas démarrer (mode inconnu, `outils/golden-check.jar` introuvable).
Le détail des arguments est dans `cave/README.md`. Sous l'invite de commandes Windows, les commandes équivalentes y sont aussi.

## 2. Règles du TP

- **Une nouvelle conversation avec l'IA à chaque phase.** Ne laissez pas le contexte d'une phase influencer la suivante.
- `Bottle.java` appartient à une autre équipe : **ne le modifiez pas**. L'API publique reste celle du départ (voir `cave/README.md`).
- Le code de départ fait foi. Si un comportement vous surprend, notez-le dans vos observations plutôt que de le « corriger ».
- Ne donnez jamais à l'IA ce que vous n'avez pas : pas de corrigé, pas de guidelines d'un autre groupe avant la mise en commun.
- À la fin de chaque phase : `python3 scripts/suivi.py sauvegarder N` (N = numéro de la phase). Rien n'est envoyé nulle part : ce sont des copies locales dans `cave/.suivi/`.
- Ne touchez pas au dossier `.depart/` (le code d'origine) et ne le donnez pas à l'IA.
- Remplissez `SUIVI.csv` au fil de l'eau : texte du prompt, nombre d'échanges avec l'IA, minutes, résultats des commandes.
  Un « échange » = un message de votre part, y compris une relance.

## 3. La demande métier (R1)

Voici ce que le client demande, en phase 1 **et** en phase 5. **La façon de le demander à l'IA est libre** : à vous de rédiger le prompt
que vous voulez (court ou détaillé, avec ou sans contexte, en une fois ou par étapes, avec l'outil de votre choix).
Une seule contrainte : le prompt écrit en phase 1 est **repris à l'identique** en phase 5. C'est ce qui rend la comparaison valable.
Notez-le dans `SUIVI.csv` dès la phase 1.

> Les bouteilles dont le nom commence par « Entamé » perdent leur cote
> deux fois plus vite qu'une bouteille normale : 2 par mois, et 4 par mois une fois
> l'échéance dépassée. La cote ne descend pas sous 0.
> Les autres catégories ne changent pas.

## 4. Déroulé (220 minutes)

**Phase 1 — Évolution directe sur le code d'origine (40 min).** Vous partez du code d'origine, tel que fourni.
- [ ] Choisir et noter l'outil et le modèle d'IA utilisés.
- [ ] Rédiger votre prompt à partir de la demande R1, le noter dans `SUIVI.csv`, puis le donner à l'IA avec le code (agent : le dossier ; chat : `cave-aplati.txt`), sans guideline ni refactoring.
- [ ] Appliquer la réponse, puis itérer jusqu'à ce que `scripts/verifier.sh r1` soit au vert. Compter les échanges.
- [ ] Relever `python3 scripts/suivi.py diff depart` et `python3 scripts/metrics.py`, puis `python3 scripts/suivi.py sauvegarder 1`.

**Phase 2 — Écrire des guidelines (50 min).** Pas de code à modifier.
- [ ] 10 min, sans IA : listez ce qui vous a gêné ou ce que l'IA a mal fait en phase 1, et ce qui gêne dans le code.
- [ ] 20 min : faites produire un projet de guidelines par l'IA, à partir du code et de votre liste.
- [ ] 20 min : relisez, corrigez, complétez **à la main**. Enregistrez le résultat dans `AGENTS.md` (60 lignes au plus) à la racine de `cave/`.

**Mise en commun (20 min).** Deux ou trois guidelines sont projetées et comparées. Ensuite seulement, l'enseignant projette une référence.

**Phase 3 — Refactoring guidé (35 min).** Repartez du code d'origine, pas du résultat de la phase 1 : `python3 scripts/suivi.py restaurer depart` (votre `AGENTS.md` est conservé).
- [ ] Nouvelle conversation ; avec un agent, `AGENTS.md` est lu par l'outil ; avec un chat, collez-le en début de conversation.
- [ ] Demandez de refactorer `Cellar` **sans changer le comportement** : pas d'évolution R1 ici.
- [ ] Vérifiez avec `scripts/verifier.sh refacto` jusqu'au vert, puis relevez les mesures et lancez `python3 scripts/suivi.py sauvegarder 3`.

**Phase 4 — Tests unitaires (30 min).** Vous continuez sur le résultat de la phase 3.
- [ ] Faites écrire des tests JUnit 5 qui décrivent le comportement actuel du code.
- [ ] `mvn -q verify` : tous les tests passent, notez la couverture de branches (rapport JaCoCo).
- [ ] `scripts/tests-sur-depart.sh` : aucun test ne doit échouer sur le code de départ. Corrigez le test fautif, jamais le départ. Puis `python3 scripts/suivi.py sauvegarder 4`.

**Phase 5 — Évolution guidée (25 min).** Vous continuez sur le résultat de la phase 4 (si besoin : `python3 scripts/suivi.py restaurer 4`).
- [ ] Nouvelle conversation, mêmes guidelines, **même texte de prompt qu'en phase 1** (copiez-le depuis `SUIVI.csv`).
- [ ] Faites ajouter aussi les tests de R1. Vérifiez : `scripts/verifier.sh r1` et `mvn -q test` au vert. Puis `python3 scripts/suivi.py sauvegarder 5`.
- [ ] Mesures : `python3 scripts/suivi.py diff 4 5`, `python3 scripts/metrics.py`, couverture.

**Bilan (20 min).** Remplissez la grille, comparez phase 1 et phase 5 (diff, complexité, comportement, tests), puis répondez au débrief :
qu'est-ce qui a le plus changé les résultats ? quelle ligne de vos guidelines a servi, laquelle manquait ? qu'avez-vous constaté que la phase 1 ne montrait pas ?

## 5. Gabarit de guidelines (`AGENTS.md`, 60 lignes au plus)

```markdown
# AGENTS.md — CaveManager

## Rôle et contexte
(Qui est l'IA, à quoi sert le programme, langage, outils, commandes de build et de test.)

## Nommage
(Langue des identifiants, conventions.)

## Taille et structure
(Longueur maximale des méthodes et des classes, imbrication, ce qu'on évite. Donnez des chiffres.)

## Tests
(Cadre, nommage, ce qu'il faut tester, ce qu'on ne fait jamais.)

## Périmètre
(Ce qu'il est interdit de modifier, ce qui doit rester stable.)

## Méthode de travail
(Dans quel ordre procéder, comment vérifier, quand s'arrêter pour demander.)

## Format de réponse
(Ce que l'IA doit rendre : diff ou fichiers, longueur de l'explication, comment signaler un doute.)

## Exemples et contre-exemples
(Un « bien » et un « mal » concrets.)
```

## 6. Glossaire

- **Refactoring** : réorganiser le code pour le rendre plus lisible et plus simple à faire évoluer, *sans changer son comportement*.
- **Guideline** : consigne qui persiste d'une demande à l'autre (fichier lu par l'outil, instructions de projet, ou texte collé en début de conversation).
- **Golden master** : trace complète du comportement d'un programme sur de nombreux cas, comparée à celle d'une référence. `verifier.sh` s'en sert.
- **Test de caractérisation** : test qui fige ce que le code fait *aujourd'hui*, bon ou pas, pour détecter ensuite toute modification involontaire.

## 7. Si ça bloque

- **Pas d'agent de code** : travaillez avec un chat. Collez `cave-aplati.txt` (régénérez-le avec `python3 scripts/aplatir.py` après chaque phase),
  exigez des **fichiers complets** et non des fragments, recopiez-les dans `src/main/java` **sans les numéros de ligne** (`012 | `), puis lancez `scripts/verifier.sh`.
- **L'IA renvoie du code qui ne compile pas** : donnez-lui le message d'erreur exact. Si trois échanges n'y suffisent pas, repartez d'une nouvelle conversation et notez-le.
- **L'IA modifie des fichiers que vous ne vouliez pas toucher** : `python3 scripts/suivi.py fichier <Nom.java>` remet un fichier à son état de départ (ou `restaurer 4` pour revenir à une phase), puis précisez le périmètre dans votre prompt.
- **`verifier.sh` ne fonctionne pas** : voir les commandes équivalentes dans `cave/README.md`, ou demandez de l'aide à un voisin ou à l'enseignant.
- **Vous avez fini en avance** : tentez le bonus R2 avec `scripts/verifier.sh r2` (texte fourni par l'enseignant).
