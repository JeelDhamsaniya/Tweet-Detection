# Toxic Tweet Detection Tool

A standalone **JavaFX** application for detecting toxic tweets, finding similar tweets, and analyzing user propagation graphs without using any machine learning libraries or build tools like Maven.

---

## Features

- **Toxic Word Detection:** Detects toxic words in tweets using the Rabin-Karp string matching algorithm.
- **Similar Tweet Detection:** Finds similar tweets based on hash similarity.
- **User Graph Analysis:** Analyzes propagation and relationships of users using graph traversal (DFS).
- **Simple and Lightweight:** No ML libraries or Maven/Gradle build tools required.

---

## Project Structure

All files are located in a single directory:

- `Main.java` – Main JavaFX application class.
- `Tweet.java` – Data model for a tweet.
- `User.java` – Data model for a user (graph node).
- `RabinKarp.java` – Algorithm for toxic word detection.
- `HashSimilarity.java` – Algorithm for finding similar tweets.
- `GraphAnalyzer.java` – Algorithm for user graph analysis (DFS).
- `toxic_words.txt` – List of toxic words.
- `tweets.csv` – Sample tweet data for testing.

---

## Prerequisites

- **Java Development Kit (JDK) 11** or higher.
- **JavaFX SDK**. Ensure your environment is configured to use JavaFX.

---

## How to Compile and Run

### Step 1: Navigate to the Project Directory

Open a terminal or command prompt and navigate to the folder containing all `.java` and `.txt` files:

```bash
cd path/to/your/project/folder
```
### Step 2 :  Set the Path to JavaFX

Specify the path to the JavaFX SDK lib folder:

Windows (Command Prompt):
```bash
set JFX_PATH="C:\path\to\your\javafx-sdk-xx\lib"
```

macOS/Linux:

```bash
export JFX_PATH=/path/to/your/javafx-sdk-xx/lib
```

### Step 3 :  Compile the Java Files

Compile all .java files while specifying the module path and required JavaFX modules:

Windows:
```bash
javac --module-path %JFX_PATH% --add-modules javafx.controls,javafx.fxml *.java
```


macOS/Linux:

```bash
javac --module-path $JFX_PATH --add-modules javafx.controls,javafx.fxml *.java
```
### Step 4: Run the Application

Run the main class:

Windows:
```bash
java --module-path %JFX_PATH% --add-modules javafx.controls,javafx.fxml Main
```


macOS/Linux:

```bash
java --module-path $JFX_PATH --add-modules javafx.controls,javafx.fxml Main
```

### Usage

- Launch the application.

- Load the provided tweets.csv file or your own tweet dataset.

- Detect toxic tweets and find similar tweets.

- Analyze the user propagation graph.

### License

This project is open for educational purposes and personal use.