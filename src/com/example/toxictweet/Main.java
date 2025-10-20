package com.example.toxictweet;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main extends Application {

    private final ObservableList<Tweet> tweets = FXCollections.observableArrayList();
    private final List<String> toxicWords = new ArrayList<>();
    private final Map<String, User> users = new HashMap<>();

    private final TableView<Tweet> tweetTable = new TableView<>();
    private final TextArea resultArea = new TextArea();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Toxic Tweet Detection Tool");

        // --- Main Layout ---
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // --- Top: Title and Controls ---
        VBox topContainer = new VBox(10);
        topContainer.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("Toxic Tweet Detection");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER);
        Button loadTweetsBtn = new Button("Load Tweets (CSV/TXT)");
        Button analyzeBtn = new Button("Run Full Analysis");
        controlBox.getChildren().addAll(loadTweetsBtn, analyzeBtn);

        topContainer.getChildren().addAll(titleLabel, controlBox);
        root.setTop(topContainer);

        // --- Center: Tweet Table ---
        setupTweetTable();
        root.setCenter(tweetTable);
        BorderPane.setMargin(tweetTable, new Insets(10, 0, 10, 0));

        // --- Bottom: Results Area ---
        VBox bottomContainer = new VBox(5);
        Label resultLabel = new Label("Analysis Results:");
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resultArea.setEditable(false);
        resultArea.setPrefHeight(200);
        bottomContainer.getChildren().addAll(resultLabel, resultArea);
        root.setBottom(bottomContainer);

        // --- Event Handlers ---
        loadTweetsBtn.setOnAction(e -> loadTweetsFromFile(primaryStage));
        analyzeBtn.setOnAction(e -> runAnalysis());

        // --- Initial Data Loading ---
        loadToxicWords(); // Load the toxic words list on startup
        loadSampleTweets(); // Load sample tweets for demonstration

        // --- Scene and Stage ---
        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Configures the columns for the JavaFX TableView.
     */
    private void setupTweetTable() {
        TableColumn<Tweet, String> idCol = new TableColumn<>("Tweet ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("tweetId"));
        idCol.setPrefWidth(100);

        TableColumn<Tweet, String> userCol = new TableColumn<>("User ID");
        userCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userCol.setPrefWidth(100);

        TableColumn<Tweet, String> textCol = new TableColumn<>("Tweet Text");
        textCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOriginalText()));
        textCol.setPrefWidth(500);

        // Custom cell factory to highlight toxic words
        textCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    Tweet tweet = getTableView().getItems().get(getIndex());

                    // Print tweet text to terminal
                    System.out.println("Tweet ID: " + tweet.getTweetId() + " | User: " + tweet.getUserId());
                    System.out.println("Text: " + item);
                    System.out.println("Toxic: " + tweet.isToxic());
                    if (tweet.isToxic()) {
                        System.out.println("Toxic Words: " + tweet.getToxicWords());
                    }
                    System.out.println("---");

                    TextFlow textFlow = new TextFlow();
                    textFlow.setPadding(new Insets(2, 5, 2, 5));
                    textFlow.setLineSpacing(1);

                    String[] words = item.split("(?<=\\s)|(?=\\s)");
                    for(String word : words) {
                        Text textNode = new Text(word);
                        textNode.setFont(Font.font("System", 12));

                        // Highlight toxic words in red if tweet is toxic
                        if (tweet.isToxic() && tweet.getToxicWords().stream()
                                .anyMatch(toxicWord -> word.toLowerCase().contains(toxicWord))) {
                            textNode.setFill(Color.RED);
                            textNode.setFont(Font.font("System", FontWeight.BOLD, 12));
                        }
                        textFlow.getChildren().add(textNode);
                    }
                    setText(null);
                    setGraphic(textFlow);
                    setStyle("-fx-padding: 2; -fx-cell-size: 30;");
                }
            }
        });

        TableColumn<Tweet, Boolean> toxicCol = new TableColumn<>("Is Toxic?");
        toxicCol.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isToxic()));
        toxicCol.setPrefWidth(100);
        toxicCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item ? "Yes" : "No");
                    setStyle("-fx-padding: 2; -fx-alignment: CENTER;");
                    if (item) {
                        setTextFill(Color.RED);
                        setFont(Font.font("System", FontWeight.BOLD, 12));
                    } else {
                        setTextFill(Color.GREEN);
                        setFont(Font.font("System", FontWeight.BOLD, 12));
                    }
                }
            }
        });

        tweetTable.getColumns().addAll(idCol, userCol, textCol, toxicCol);
        tweetTable.setItems(tweets);
        tweetTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tweetTable.setFixedCellSize(35); // Set compact fixed cell height
    }
    /**
     * Loads the list of toxic words from toxic_words.txt.
     */
    private void loadToxicWords() {
        try (BufferedReader reader = new BufferedReader(new FileReader("toxic_words.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    toxicWords.add(line.trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            resultArea.setText("Error loading toxic_words.txt. Make sure the file exists.\n" + e.getMessage());
            // Add some default words if file loading fails
            toxicWords.addAll(Arrays.asList("hate", "stupid", "idiot", "kill", "dumb"));
        }
    }

    /**
     * Opens a FileChooser to let the user select a CSV/TXT file of tweets.
     */
    private void loadTweetsFromFile(Stage owner) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Tweet File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.csv")
        );
        File file = fileChooser.showOpenDialog(owner);
        if (file != null) {
            parseTweetFile(file);
        }
    }

    /**
     * Loads some sample tweets into the table for quick demonstration.
     */
    private void loadSampleTweets() {
        tweets.clear();
        users.clear();

        List<String> sampleData = Arrays.asList(
                "101,userA,This is a wonderful day!",
                "102,userB,I hate this stupid game, it's so dumb.",
                "103,userC,Spreading love and positivity.",
                "104,userD,What an idiot! Can't believe you would say that.",
                "105,userE,This is another terrible, awful, hateful game.,RT@userB",
                "106,userF,I agree with @userB, that game is dumb and stupid.,RT@userB",
                "107,userG,Let's all be friends.",
                "108,userH,You should go away and never come back.,RT@userD"
        );

        for (String line : sampleData) {
            processTweetLine(line);
        }

        tweetTable.refresh();
    }


    /**
     * Parses the selected tweet file line by line.
     */
    private void parseTweetFile(File file) {
        tweets.clear();
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processTweetLine(line);
            }
        } catch (IOException e) {
            resultArea.setText("Error reading tweet file: " + e.getMessage());
        }
        tweetTable.refresh();
    }

    /**
     * Processes a single line from the tweet data source.
     */
    private void processTweetLine(String line) {
        String[] parts = line.split(",", 4); // Split into max 4 parts
        if (parts.length >= 3) {
            String tweetId = parts[0].trim();
            String userId = parts[1].trim();
            String text = parts[2].trim();

            Tweet tweet = new Tweet(tweetId, userId, text);
            tweets.add(tweet);

            // Update or create user
            users.computeIfAbsent(userId, User::new);

            // Handle retweets for graph building
            if (parts.length == 4 && parts[3].toUpperCase().startsWith("RT@")) {
                String sourceUser = parts[3].substring(3).trim();
                users.computeIfAbsent(sourceUser, User::new);
                // Add a directed edge from retweeter to source user
                users.get(userId).addConnection(sourceUser);
            }
        }
    }


    /**
     * Runs the full analysis pipeline on the loaded tweets.
     */
    private void runAnalysis() {
        if (tweets.isEmpty()) {
            resultArea.setText("Please load some tweets before running the analysis.");
            return;
        }

        // --- Step 1: Preprocessing and Toxic Detection ---
        RabinKarp toxicDetector = new RabinKarp(toxicWords);
        for (Tweet tweet : tweets) {
            String preprocessedText = preprocess(tweet.getOriginalText());
            Set<String> foundToxicWords = toxicDetector.search(preprocessedText);
            if (!foundToxicWords.isEmpty()) {
                tweet.setToxic(true);
                tweet.setToxicWords(foundToxicWords);
            }
        }
        tweetTable.refresh();

        // --- Step 2: Similarity Detection ---
        HashSimilarity similarityDetector = new HashSimilarity(70.0); // 70% threshold
        List<List<Tweet>> similarGroups = similarityDetector.findSimilarGroups(tweets);

        // --- Step 3: User Propagation Graph Analysis ---
        List<Tweet> toxicTweets = tweets.stream()
                .filter(Tweet::isToxic)
                .collect(Collectors.toList());
        GraphAnalyzer graphAnalyzer = new GraphAnalyzer(users);
        List<Set<String>> toxicClusters = graphAnalyzer.findToxicClusters(toxicTweets);

        // --- Step 4: Display Results ---
        displayResults(similarGroups, toxicClusters);
    }

    /**
     * Preprocesses a tweet's text by converting to lowercase and removing symbols.
     */
    private String preprocess(String text) {
        return text.toLowerCase().replaceAll("[^a-z0-9\\s]", "");
    }

    /**
     * Displays the final analysis results in the TextArea.
     */
    private void displayResults(List<List<Tweet>> similarGroups, List<Set<String>> toxicClusters) {
        StringBuilder sb = new StringBuilder();

        sb.append("--- TOXIC TWEET ANALYSIS COMPLETE ---\n\n");

        // Similar Tweet Groups
        sb.append("--- Similar/Duplicate Tweet Groups (>=70% Similarity) ---\n");
        if (similarGroups.isEmpty()) {
            sb.append("No similar tweet groups found.\n");
        } else {
            for (int i = 0; i < similarGroups.size(); i++) {
                sb.append("Group ").append(i + 1).append(":\n");
                for (Tweet tweet : similarGroups.get(i)) {
                    sb.append("  - [TweetID: ").append(tweet.getTweetId())
                            .append(", User: ").append(tweet.getUserId()).append("] ")
                            .append(tweet.getOriginalText()).append("\n");
                }
            }
        }
        sb.append("\n");

        // Toxic User Clusters
        sb.append("--- Toxic User Propagation Clusters ---\n");
        if (toxicClusters.isEmpty()) {
            sb.append("No toxic user clusters found.\n");
        } else {
            for (int i = 0; i < toxicClusters.size(); i++) {
                Set<String> cluster = toxicClusters.get(i);
                // Heuristic: Find a source user (one with no incoming connections within the cluster)
                String source = GraphAnalyzer.findSourceUserInCluster(cluster, users);
                sb.append("Cluster ").append(i + 1).append(":\n");
                sb.append("  - Source User (Heuristic): ").append(source).append("\n");
                sb.append("  - Propagating Users: ").append(String.join(", ", cluster)).append("\n");
            }
        }

        resultArea.setText(sb.toString());
    }
}
