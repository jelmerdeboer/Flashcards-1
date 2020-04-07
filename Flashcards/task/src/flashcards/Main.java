package flashcards;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private LinkedHashMap<String, String> cardMap;
    private Scanner scanner;
    private boolean gameIsActive;

    private Main() {
        this.scanner = new Scanner(System.in);
        this.cardMap = new LinkedHashMap<>();
        this.gameIsActive = true;
    }

    private void exitGame() {
        gameIsActive = false;
        scanner.close();
        System.out.println("Bye bye!");
    }

    private void runGameByAction() {
        System.out.println("Input the action (add, remove, import, export, ask, exit):");
        String action = scanner.nextLine();

        switch (action) {
            case "add":
                fillCardMap();
                break;
            case "remove":
                removeCards();
                break;
            case "import":
                importCards();
                break;
            case "export":
                exportCards();
                break;
            case "ask":
                runFlashCardGame();
                break;
            case "exit":
                exitGame();
                break;
            default:
                break;
        }
    }

    private void removeCards() {
        System.out.println("The card:");
        String cardToBeRemoved = scanner.nextLine();

        if (cardMap.containsKey(cardToBeRemoved)) {
            cardMap.remove(cardToBeRemoved);
            System.out.println("The card has been removed.");
        } else {
            System.out.println("Can't remove \"" + cardToBeRemoved + "\": there is no such card.");
        }
    }

    private void importCards() {
        System.out.println("File name:");
        File file = new File(scanner.nextLine());

        int cardsImported = 0;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String cardQuestion = scanner.nextLine();
                String cardDefinition = scanner.nextLine();

                cardMap.put(cardQuestion, cardDefinition);
                cardsImported++;
            }
            System.out.println(cardsImported + " cards have been loaded.");
        } catch (Exception e) {
            System.out.println("File not found.");
        }
    }

    private void exportCards() {
        System.out.println("File name:");
        File file = new File(scanner.nextLine());

        int numberOfSavedCards = 0;

        try (FileWriter cardWriter = new FileWriter(file, false)) {
            for (String cardQuestion : cardMap.keySet()) {
                String exportLine = cardQuestion + "\n" + cardMap.get(cardQuestion) + "\n";
                cardWriter.write(exportLine);
                numberOfSavedCards++;
            }
        } catch (Exception e) {
            System.out.println("Something went wrong: \n" + e);
        }
        System.out.println(numberOfSavedCards + " cards have been saved.");
    }

    private void fillCardMap() {
        System.out.println("The card:");
        String cardQuestion = scanner.nextLine();

        if (cardMap.containsKey(cardQuestion)) {
            System.out.println("The card \"" + cardQuestion + "\" already exists.");
        } else {
            System.out.println("The definition of the card:");
            String cardDefinition = scanner.nextLine();

            if (cardMap.containsValue(cardDefinition)) {
                System.out.println("The definition \"" + cardDefinition + "\" already exists.");
            } else {
                cardMap.put(cardQuestion, cardDefinition);
                System.out.println("The pair (\"" + cardQuestion + "\"" + ":" + "\"" + cardDefinition + "\") has been added.");
            }
        }
    }

    private String getRightCardQuestion(String answer) {
        String rightCardQuestion = null;
        for (String CardQuestionToFins : cardMap.keySet()) {
            if (answer.equals(cardMap.get(CardQuestionToFins))) {
                rightCardQuestion = CardQuestionToFins;
                break;
            }
        }
        return rightCardQuestion;
    }

    private void runFlashCardGame() {
        Random randomised = new Random();

        System.out.println("How many times to ask?");
        int numberOfQuestions = Integer.parseInt(scanner.nextLine());

        Object[] cardQuestionList = cardMap.keySet().toArray();

        for (int i = 0; i < numberOfQuestions; i++) {
            String cardQuestion = cardQuestionList[randomised.nextInt(cardQuestionList.length)].toString();

            System.out.println("Print the definition of \"" + cardQuestion + "\":");
            String cardAnswerByPlayer = scanner.nextLine();

            if (cardMap.containsValue(cardAnswerByPlayer)) {
                if (cardMap.get(cardQuestion).equals(cardAnswerByPlayer)) {
                    System.out.println("Correct answer.");
                } else {
                    System.out.println("Wrong answer. The correct one is \"" + cardMap.get(cardQuestion) + "\", " +
                        "you've just written the definition of \"" + getRightCardQuestion(cardAnswerByPlayer) + "\"");
                }
            } else {
                System.out.println("Wrong answer. The correct one is \"" + cardMap.get(cardQuestion) + "\".");
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        while (main.gameIsActive) {
            main.runGameByAction();
        }
    }
}
