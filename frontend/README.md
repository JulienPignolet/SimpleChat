# frontend

## Project setup
```
npm install  
```

# Conseil à prendre ou à laisser bande d'enfoirés
```
vue ui  (c archi bien vous verrez)
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

# Docs
```
Matez vuex et vuex-pathify, c verbeux au début mais c ez après, on en reparle
go faire de la doc despi aussi, et go utilisez jest pour les tests
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

# Internationalisation

### Ajout de traductions

Les fichiers de traduction sont dans `frontend\src\assets\i18n\translations`.

Si vous ajoutez du texte qui doit être traduit, modifiez `fr.json` en réspectant la structure du fichier.
Ensuite dans [POEditor](https://poeditor.com/) :
- Sélectionner le projet SimpleChat
- Cliquer sur "Import"
- Cliquer sur "Browse" pour sélectionner le fichier `fr.json`
- Choisir l'option "Also import translations in..." > "French"
- Cliquer sur "Import file"

![POEditor](https://i.postimg.cc/HLd3pgdm/image.png)

Traduire ensuite les nouveaux textes dans chaque langue, puis exporter les fichiers en format **Key-Value JSON** afin de mettre à jour les autres fichiers de traduction.

La structure des fichiers de traduction est la suivante : 

```json
{
  "component_x": {
    "clé_1": "Traduction clé 1",
    "clé_2": "Traduction clé 2",
    "etc": "..."
  },
  "dialog": {
      "component_y": {
        "clé_3": "Traduction clé 3",
        "clé_4": "Traduction clé 4",
        "etc": "..."
      }
  },
  "store": {
      "store_z": {
        "clé_5": "Traduction clé 5",
        "clé_6": "Traduction clé 6",
        "etc": "..."
      }
  },
  "error": {
    "message_d_erreur": "Traduction du message d'erreur"
  },
  "general": {
    "cancel": "Annuler",
    "add": "Ajouter",
    "etc": "..."
  }
}
```

### Utilisation dans le code

Dans le code, utiliser simplement `$t('clé de trad')`.

Par exmple `$t('dialog.component_y.clé_4')`.
