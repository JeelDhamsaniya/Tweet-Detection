Toxic Tweet Detection Tool

This is a standalone JavaFX application for detecting toxic tweets, finding similar tweets, and analyzing user propagation graphs without using any machine learning libraries or build tools like Maven.

Project Structure

All necessary files are located in a single directory:

Main.java: The main application class with the JavaFX GUI.

Tweet.java: Data model for a tweet.

User.java: Data model for a user (graph node).

RabinKarp.java: Algorithm for toxic word detection.

HashSimilarity.java: Algorithm for finding similar tweets.

GraphAnalyzer.java: Algorithm for user graph analysis (DFS).

toxic_words.txt: A simple text file containing a list of toxic words.

tweets.csv: Sample tweet data for testing.

Prerequisites

Java Development Kit (JDK) 11 or higher.

JavaFX SDK. Make sure your environment is configured to use JavaFX.

How to Compile and Run

Follow these steps from your terminal or command prompt.

Step 1: Navigate to the Project Directory

Open your terminal and change the directory to where you have saved all the .java and .txt files.

cd path/to/your/project/folder


Step 2: Set the Path to JavaFX

You need to tell the Java compiler and runtime where to find the JavaFX library files. Replace path/to/your/javafx-sdk/lib with the actual path to the lib folder of your JavaFX SDK installation.

For Windows (Command Prompt):

set JFX_PATH="C:\path\to\your\javafx-sdk-xx\lib"


For macOS/Linux:

export JFX_PATH=/path/to/your/javafx-sdk-xx/lib


Step 3: Compile the Java Files

Compile all .java files. You need to specify the module path and the modules required for JavaFX.

javac --module-path %JFX_PATH% --add-modules javafx.controls,javafx.fxml *.java


Note: On macOS/Linux, use $JFX_PATH instead of %JFX_PATH%.

Step 4: Run the Application

Run the main class. Again, you need to provide the module path and add the required modules.

java --module-path %JFX_PATH% --add-modules javafx.controls,javafx.fxml Main


Note: On macOS/Linux, use $JFX_PATH instead of %JFX_PATH%.

The application window should now appear. You can load the provided tweets.csv file or your own data to begin the analysis.