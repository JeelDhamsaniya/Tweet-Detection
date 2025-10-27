# Toxic Tweet Detection Tool

A JavaFX application that detects toxic tweets, finds similar/duplicate content, and analyzes toxic tweet propagation through user retweet networks using graph algorithms.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Algorithms Used](#algorithms-used)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [How to Run](#how-to-run)
- [How It Works](#how-it-works)
- [Data Format](#data-format)
- [Screenshots](#screenshots)

---

## 🎯 Overview

This application analyzes tweets to:

1. **Detect toxic content** using pattern matching algorithms
2. **Identify similar/duplicate tweets** using Jaccard similarity
3. **Trace toxic tweet propagation** through user retweet networks using graph analysis

**No Machine Learning** - Uses classical algorithms: Rabin-Karp, Jaccard Similarity, and Depth-First Search (DFS).

---

## ✨ Features

### 1. **Toxic Tweet Detection**

- Uses **Rabin-Karp** algorithm for efficient string pattern matching
- Detects toxic words from a customizable dictionary (`toxic_words.txt`)
- Highlights toxic words in red in the UI

### 2. **Similar Tweet Detection**

- Uses **Jaccard Similarity** on word sets
- Groups tweets with ≥70% similarity
- Helps identify duplicate/coordinated toxic content

### 3. **User Propagation Graph Analysis**

- Builds a **directed graph** based on retweet relationships
- Uses **Depth-First Search (DFS)** to find connected clusters
- Identifies "source" users (likely originators of toxic content)
- Shows how toxic content spreads through the network

### 4. **Interactive GUI**

- JavaFX-based modern interface
- Real-time tweet highlighting
- Results visualization with clusters and groups

---

## 🧮 Algorithms Used

### 1. **Rabin-Karp Algorithm** (`RabinKarp.java`)

**Purpose:** Fast string pattern matching for toxic word detection

**How it works:**

- Uses rolling hash for efficient substring matching
- Time Complexity: O(n + m) where n = text length, m = pattern length
- Processes all toxic words against tweet text

**Implementation:**

```java
// Rolling hash calculation
hash = (hash - text.charAt(i) * h) * base + text.charAt(i + m);
```

### 2. **Jaccard Similarity** (`HashSimilarity.java`)

**Purpose:** Measure similarity between tweets

**Formula:**

```
Jaccard(A, B) = |A ∩ B| / |A ∪ B|
```

**How it works:**

- Converts each tweet into a set of unique words
- Calculates intersection and union of word sets
- Tweets with ≥70% similarity are grouped together

**Example:**

```
Tweet A: "This is stupid and dumb"
Tweet B: "This is dumb and stupid"
Similarity = 5/5 = 100% (same words, different order)
```

### 3. **Graph Analysis with DFS** (`GraphAnalyzer.java`)

**Purpose:** Find toxic tweet propagation clusters

**Graph Structure:**

- **Nodes:** Users
- **Edges:** Directed edge from retweeter → original author
- **Algorithm:** Depth-First Search (DFS)

**How it works:**

1. Build graph from retweet relationships
2. Start DFS from users who posted toxic tweets
3. Traverse all reachable users through retweet edges
4. Each connected component = one propagation cluster

**Complexity:** O(V + E) where V = users, E = retweet connections

---

## 📁 Project Structure

```
Tweet Detection/
├── src/
│   └── com/example/toxictweet/
│       ├── Main.java              # JavaFX GUI application
│       ├── Tweet.java             # Tweet data model
│       ├── User.java              # User node (graph vertex)
│       ├── RabinKarp.java         # Toxic word detection algorithm
│       ├── HashSimilarity.java    # Similar tweet detection algorithm
│       └── GraphAnalyzer.java     # User graph analysis (DFS)
├── lib/                           # JavaFX JAR files
│   ├── javafx.base.jar
│   ├── javafx.controls.jar
│   ├── javafx.fxml.jar
│   └── javafx.graphics.jar
├── bin/                           # Compiled .class files
├── toxic_words.txt                # Dictionary of toxic words (200 words)
├── tweets.csv                     # Sample tweet dataset (100 tweets)
├── run.bat                        # Windows batch script to compile & run
├── .vscode/                       # VS Code configuration
│   ├── settings.json
│   └── launch.json
└── Readme.md                      # This file
```

---

## 🔧 Prerequisites

### 1. **Java Development Kit (JDK)**

- **Version:** JDK 11 or higher (tested with JDK 21)
- Download: https://www.oracle.com/java/technologies/downloads/

### 2. **JavaFX SDK**

- **Version:** JavaFX 21 (must match your JDK version)
- Download: https://gluonhq.com/products/javafx/
- Extract to: `C:\Users\YourUsername\javafx-sdk-21.0.8`

### 3. **IDE (Optional)**

- **VS Code** with Java Extension Pack, or
- **IntelliJ IDEA**, or
- **Eclipse**

---

## 🚀 Setup Instructions

### Option 1: Using VS Code (Recommended)

#### Step 1: Install Extensions

Install these VS Code extensions:

- **Extension Pack for Java** (by Microsoft)
- **Debugger for Java** (by Microsoft)

#### Step 2: Download JavaFX

1. Download JavaFX SDK 21 from https://gluonhq.com/products/javafx/
2. Extract to `C:\Users\YourUsername\javafx-sdk-21.0.8`

#### Step 3: Copy JavaFX JARs to Project

```powershell
# Run in PowerShell terminal
Copy-Item "C:\Users\YourUsername\javafx-sdk-21.0.8\lib\*.jar" -Destination "d:\DAA Assignment\Tweet Detection\lib\"
```

#### Step 4: Reload Java Project

1. Press `Ctrl+Shift+P`
2. Type: `Java: Clean Java Language Server Workspace`
3. Select **Reload and delete**
4. Wait for reload to complete

#### Step 5: Run the Application

- Press **F5**, or
- Click **Run** button above `main` method in `Main.java`

---

### Option 2: Using Batch File (Windows)

#### Step 1: Edit `run.bat`

Open `run.bat` and update the JavaFX SDK path:

```batch
set JAVAFX_SDK=C:\Users\YourUsername\javafx-sdk-21.0.8
```

#### Step 2: Run the Application

Double-click `run.bat` or run in terminal:

```cmd
.\run.bat
```

---

### Option 3: Manual Compilation (Any OS)

#### Compile:

```bash
javac --module-path /path/to/javafx-sdk/lib \
      --add-modules javafx.controls,javafx.fxml \
      -d bin \
      src/com/example/toxictweet/*.java
```

#### Run:

```bash
java --module-path /path/to/javafx-sdk/lib \
     --add-modules javafx.controls,javafx.fxml \
     -Djava.library.path=/path/to/javafx-sdk/bin \
     -cp bin \
     com.example.toxictweet.Main
```

---

## 🎮 How to Run

### Using the GUI

1. **Launch Application**

   - Run using any method above
   - JavaFX window will open with sample tweets pre-loaded

2. **Run Analysis**

   - Click **"Run Full Analysis"** button
   - Application will:
     - Detect toxic words in tweets (highlighted in red)
     - Find similar tweet groups
     - Build user retweet graph
     - Find toxic propagation clusters

3. **Load Custom Data**

   - Click **"Load Tweets (CSV/TXT)"**
   - Select your CSV file (format: `tweetId,userId,text,RT@sourceUser`)

4. **View Results**
   - **Tweet Table:** Shows all tweets with toxic words highlighted
   - **Results Area:** Displays:
     - Similar tweet groups (≥70% similarity)
     - Toxic user propagation clusters
     - Source users (heuristic: users with no incoming retweets in cluster)

---

## 🔍 How It Works

### End-to-End Flow

```
1. Load Tweets
   ↓
2. Preprocess Text (lowercase, remove symbols)
   ↓
3. Toxic Detection (Rabin-Karp)
   ├── Load toxic_words.txt
   ├── Search each word in tweet
   └── Mark tweet as toxic if matches found
   ↓
4. Similarity Detection (Jaccard)
   ├── Convert tweets to word sets
   ├── Calculate pairwise similarity
   └── Group tweets with ≥70% similarity
   ↓
5. Graph Construction
   ├── Create User nodes
   ├── Parse retweet relationships (RT@user)
   └── Add directed edges (retweeter → original author)
   ↓
6. Graph Analysis (DFS)
   ├── Start from toxic tweet authors
   ├── Traverse retweet connections
   ├── Find connected components (clusters)
   └── Identify source users (in-degree = 0)
   ↓
7. Display Results
   ├── Highlight toxic tweets in UI
   ├── Show similar groups
   └── Show propagation clusters
```

### Graph Construction Example

**Tweet Data:**

```csv
202,user_2,What a pathetic attempt. This is useless.,
204,user_4,Such a stupid idiot.,RT@user_2
205,user_5,Completely useless design.,RT@user_2
206,user_6,Totally agree this is trash.,RT@user_2
```

**Graph Created:**

```
user_2 ← user_4
   ↑
   ├─ user_5
   ↑
   └─ user_6
```

**Cluster Found:**

- Source: `user_2` (original toxic tweet author)
- Propagators: `user_4`, `user_5`, `user_6` (retweeters)

---

## 📊 Data Format

### 1. `tweets.csv` Format

**Structure:** `tweetId,userId,text,retweet_info`

**Examples:**

```csv
201,user_1,This is a fantastic product!,
202,user_2,What a pathetic attempt. This is useless.,
204,user_4,Such a stupid idiot.,RT@user_2
```

**Rules:**

- 4 columns (last column can be empty)
- Retweet format: `RT@originalUserId`
- If not a retweet, leave 4th column blank (but keep the comma)

### 2. `toxic_words.txt` Format

**Structure:** One word per line

**Examples:**

```
hate
stupid
idiot
pathetic
useless
```

**Current Dataset:**

- 200 toxic words included
- Easily customizable (add/remove words)

---

## 📈 Performance & Complexity

| Algorithm              | Time Complexity | Space Complexity |
| ---------------------- | --------------- | ---------------- |
| Rabin-Karp (per tweet) | O(n + m)        | O(m)             |
| Jaccard Similarity     | O(n² × w)       | O(n × w)         |
| DFS Graph Traversal    | O(V + E)        | O(V)             |

**Where:**

- n = number of tweets
- m = pattern length
- w = average words per tweet
- V = number of users
- E = number of retweet connections

---

## 🐛 Troubleshooting

### JavaFX Import Errors

**Problem:** `The import javafx cannot be resolved`

**Solution:**

1. Ensure JavaFX JARs are in `lib/` folder
2. Clean Java workspace: `Ctrl+Shift+P` → `Java: Clean Java Language Server Workspace`
3. Reload VS Code

### Graphics Initialization Error

**Problem:** `No toolkit found` or `Graphics Device initialization failed`

**Solution:**
Add native library path to VM arguments:

```
-Djava.library.path=C:/path/to/javafx-sdk/bin
```

### Compilation Errors

**Problem:** `javac: command not found`

**Solution:**

1. Install JDK 11+
2. Add to PATH: `C:\Program Files\Java\jdk-21\bin`

---

## 📝 Sample Output

### Console Output:

```
Tweet ID: 202 | User: user_2
Text: What a pathetic attempt. This is useless.
Toxic: true
Toxic Words: [pathetic, useless]
---
Tweet ID: 204 | User: user_4
Text: Such a stupid idiot.
Toxic: true
Toxic Words: [stupid, idiot]
---
```

### GUI Results:

```
--- TOXIC TWEET ANALYSIS COMPLETE ---

--- Similar/Duplicate Tweet Groups (≥70% Similarity) ---
Group 1:
  - [TweetID: 202, User: user_2] What a pathetic attempt. This is useless.
  - [TweetID: 205, User: user_5] Completely useless design.

--- Toxic User Propagation Clusters ---
Cluster 1:
  - Source User (Heuristic): user_2
  - Propagating Users: user_2, user_4, user_5, user_6, user_7, user_8, user_9, user_10
```

---

## 🎓 Learning Outcomes

This project demonstrates:

- **String Algorithms:** Rabin-Karp rolling hash
- **Similarity Metrics:** Jaccard similarity on sets
- **Graph Theory:** DFS traversal, connected components
- **Data Structures:** Hash sets, adjacency lists
- **GUI Development:** JavaFX controls and layouts
- **Algorithm Design:** Greedy clustering, heuristic source detection

---

## 📜 License

This project is for educational purposes (DAA Assignment).

---

## 👨‍💻 Author

**Repository:** [Tweet-Detection](https://github.com/JeelDhamsaniya/Tweet-Detection)  
**Owner:** JeelDhamsaniya

---

## 🙏 Acknowledgments

- JavaFX for GUI framework
- Gluon for JavaFX SDK distribution
- Classic algorithms: Rabin-Karp, Jaccard, DFS

---

**Happy Analyzing! 🚀**

Windows (Command Prompt):

```bash
set JFX_PATH="C:\path\to\your\javafx-sdk-xx\lib"
```

macOS/Linux:

```bash
export JFX_PATH=/path/to/your/javafx-sdk-xx/lib
```

### Step 3 : Compile the Java Files

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
