# Notes App

A Console driven note creator written in Kotlin

## Features

1. Main menu to process user input
2. Full CRUD support: add, list, update and delete notes
4. Logging of app activity to console
5. User-friendly output of notes

## Note Properties

| Field        | Type    | Description                   |
|--------------|---------|-------------------------------|
| `id`         | Int     | Unique identifier             |
| `title`      | String  | Short title of the note       |
| `body`       | String  | Main content of the note      |
| `priority`   | Int     | Priority level of the note    |
| `category`   | String  | Category the note belongs to  |
| `isArchived` | Boolean | If note is archived           |

## Menu Options

| Option | Action          |
|--------|-----------------|
| 1      | Add a note      |
| 2      | List all notes  |
| 3      | Update a note   |
| 4      | Delete a note   |
| 0      | Exit the app    |

## Getting Started

### Prerequisites

- JDK 17
- Gradle

### Run the app

```bash
git clone <your-repo-url>
cd notes-app
./gradlew run
```

Or open the project in IntelliJ IDEA and run `main()`.

## Example Output

```
#1
title: Shopping
body: Buy milk and eggs
priority: 2
category: Personal
isArchived: false
```

## Author

Jordan Butler
