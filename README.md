# Notes App

A Console driven note creator written in Kotlin

## Features

1. Main menu to process user input
2. Full CRUD support: add, list, update and delete notes
3. Create items for notes
4. Logging of app activity to console
5. User-friendly output of notes
6. Persistence to XML or JSON format
7. Auto versioning of notes

### Note Properties

| Field        | Type                | Description                  |
|--------------|---------------------|------------------------------|
| `id`         | Int                 | Unique identifier            |
| `title`      | String              | Short title of the note      |
| `body`       | String              | Main content of the note     |
| `priority`   | Int                 | Priority level of the note   |
| `status`     | Enum                | To Do, In Progress or Done   |
| `category`   | String              | Category the note belongs to |
| `tags`       | MutableList<String> | Tags relevant to note        |
| `version`    | Double              | Auto versioning              |
| `isArchived` | Boolean             | If note is archived          |

### Item Properties
| Field         | Type    | Description              |
|---------------|---------|--------------------------|
| `id`          | Int     | Unique identifier        |
| `description` | String  | Short title of the note  |
| `isCompleted` | Boolean | Main content of the note |

## Main Menu Options

| Option | Action           |
|--------|------------------|
| 1      | Add a note       |
| 2      | Add item to note |
| 3      | View note items  |
| 4      | List notes       |
| 5      | Search notes     |
| 6      | Update a note    |
| 7      | Archive a note   |
| 8      | Delete a note    |
| 9      | Save notes       |
| 10     | Load notes       |

## Item Menu (within main menu)
| Option | Action         |
|--------|----------------|
| 11     | Add item       |
| 12     | List all items |
| 13     | Item Report    |
| 14     | Search Items   |
| 0      | Exit the app   |

## List Menu Options

| Option | Action              |
|--------|---------------------|
| 1      | List all notes      |
| 2      | List active notes   |
| 3      | List archived notes |
| 4      | List by category    |
| 5      | List by priority    |
| 0      | Exit to main menu   |

## Example Note Output

```
#0
Title: Kotlin Assignment
Body: Create tests for new features
Priority: 5
Status: To Do
Category: College
Tags: kotlin, assignment, github
Version: 1.0
Archive status: false
```
## Example Item Output
```
#0
Description: Write item tests
Completed?: true
```

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

## Author

Jordan Butler
