package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.BeyondGridBoundsException;
import Exceptions.DataNotSentException;
import Exceptions.InvalidButtonPressException;
import Exceptions.InvalidDataReceivedException;
import Exceptions.InvalidShipPlacementException;
import Exceptions.StartGameException;
import application.Board.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 
 * This is the Main Class which will help us to play the game . Method which
 * will startGame , Create Boards.
 * <p>
 * Other tasks are like:
 * </p>
 * <ol>
 * <li>Handles the Mouse events with player and opponent moves.</li>
 * <li>Initialize Variables.</li>
 * </ol>
 * 
 * @author K3
 *
 */

public class Battle extends Application {

	private boolean executing = false;

	private boolean isCheating = true;

	private Board opponentBoard, firstPlayerBoard;

	private int numberOfShips = 5;

	private boolean normalGame;

	private boolean salvation;

	private boolean suggSalvation;

	private int hits = 1;

	private ArrayList<String> cellsSelected = new ArrayList<>();

	private ArrayList<Cell> numberOfShots = new ArrayList<>();

	private ArrayList<Integer> shipLengths = new ArrayList<Integer>() {

		{
			add(5);
			add(4);
			add(3);
			add(3);
			add(2);
		}
	};

	private int currentShip = 0;

	private boolean opponentTurn = false;

	File shipFile = new File(".");

	private Random random = new Random();

	private Button st = new Button("P2C");

	private Button reset = new Button("RESET");

	private Button adjust = new Button("ADJUST");

	private Button load = new Button("LOAD");

	private Button save = new Button("SAVE");

	private Button exit = new Button("EXIT");

	private Button doNotCheat = new Button("CHEAT");

	private Button p2p = new Button("P2P");

	private double cellSize = 30.0;

	private AI ai = new AI();

	private Rectangle ship1 = new Rectangle(50, 450, 150, 30);
	private Rectangle ship2 = new Rectangle(50, 490, 120, 30);
	private Rectangle ship3 = new Rectangle(50, 530, 90, 30);
	private Rectangle ship4 = new Rectangle(50, 570, 90, 30);
	private Rectangle ship5 = new Rectangle(50, 610, 60, 30);

	private boolean needToRotate = true;
	private boolean isRotated = false;
	private Rectangle selectedShip;
	private double previoustime = 0;
	private double currenttime = 0;

	private double previoustime2 = 0;
	private double currenttime2 = 0;

	HBox hBox, hBox1;

	static int player1Score, player2Score;

	Text timer1, timer2, player2ScoreDisplay, player1ScoreDisplay;
	Timeline timelinePlayer1, timelinePlayer2;

	int mins = 0, secs = 0, millis = 0;
	int mins1 = 0, secs1 = 0, millis1 = 0;

	boolean player1Timer = true;
	boolean player2Timer = true;

	checkTimer checkTime = new checkTimer();

	Thread t1 = new Thread(checkTime, "T1");

	static boolean checkTimeForSug = true;

	// Map to keep a match of the ship names to selected ships

	Map<String, Rectangle> strToShip = new HashMap<String, Rectangle>();

	// Map to match the selected ship to its properties
	Map<Rectangle, String> dragAndDropShips = new HashMap<Rectangle, String>();
	
	// map to store the opponent ships and their coordinates
	Map<Integer, String> opponetShipDetails = new HashMap<Integer, String>();

	// for Suggestion Salva
	Map<Rectangle, String> dragAndDropShipsOpponent = new HashMap<Rectangle, String>();

	boolean checkForSugg = false;

	final FileChooser fileChooser = new FileChooser();

	boolean loadCheck = false;

	static boolean twoPlayer = false;

	Stage global_stage = null;

	/**
	 * <p>
	 * It updates the timer text of Player1
	 * </p>
	 * 
	 * @param text
	 */
	void changeTimer1(Text text) {
		if (millis == 1000) {
			secs++;
			millis = 0;
		}
		if (secs == 60) {
			mins++;
			secs = 0;
		}
		text.setText((((mins / 10) == 0) ? "0" : "") + mins + ":" + (((secs / 10) == 0) ? "0" : "") + secs + ":"
				+ (((millis / 10) == 0) ? "00" : (((millis / 100) == 0) ? "0" : "")) + millis++);
	}

	/**
	 * <p>
	 * It updates the timer text of opponent/Player2
	 * </p>
	 * 
	 * @param text
	 */
	void changeTimer2(Text text) {
		if (millis1 == 1000) {
			secs1++;
			millis1 = 0;
		}
		if (secs1 == 60) {
			mins1++;
			secs1 = 0;
		}
		text.setText((((mins1 / 10) == 0) ? "0" : "") + mins1 + ":" + (((secs1 / 10) == 0) ? "0" : "") + secs1 + ":"
				+ (((millis1 / 10) == 0) ? "00" : (((millis1 / 100) == 0) ? "0" : "")) + millis1++);

	}

	/**
	 * In general adding styles and layout to the output screen i.e titles ,grid
	 * layout,mouse effects and movement etc.
	 * <p>
	 * In Detail tasks carried out for board design.
	 * </p>
	 * <ol type="1">
	 * <li>Setting the title for the game.</li>
	 * <li>Giving heading to grid1 and grid2 i.e player 1 grid and opponent
	 * grid</li>
	 * <li>Create HBox i.e icons to specify different functionalities in game like
	 * start,reset,pause and load</li>
	 * <li>Mouse handler events for opponent and player 1</li>
	 * </ol>
	 * 
	 * @param personStage
	 *            Stage that holds the board, grids and ships on it.
	 * @param background
	 * @return - returns the root node ( Parent )
	 */
	private Parent designBoard(Stage personStage, Background background) {

		BorderPane root = new BorderPane();
		root.setPrefSize(1300, 800);

		Text battle = new Text();
		battle.setText("BATTLESHIP GAME--PLAYER 1");
		battle.setFill(Color.BLACK);
		battle.setStrokeWidth(2);
		battle.setStroke(Color.WHITE);
		battle.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 35));

		battle.setX(350);
		battle.setY(50);
		battle.setUnderline(true);

		Text player1 = new Text();
		player1.setText("PLAYER 1 GRID");
		player1.setX(300);
		player1.setY(100);
		player1.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

		Text Opponent = new Text();
		Opponent.setText("OPPONENT GRID");
		Opponent.setX(800);
		Opponent.setY(100);
		Opponent.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

		HBox actions = new HBox(30, st, reset, adjust, save, load, p2p, doNotCheat, exit);
		actions.setAlignment(Pos.CENTER);
		buttonGeometry();

		actions.setTranslateX(670);
		actions.setTranslateY(750);

		root.getChildren().add(battle);
		root.getChildren().add(player1);
		root.getChildren().add(Opponent);
		root.getChildren().add(actions);

		// Player 1

		Text player1Summary = new Text();
		player1Summary.setText("Player 1");
		player1Summary.setFill(Color.GOLD);
		player1Summary.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 24));

		Text player1TimerHeading = new Text();
		player1TimerHeading.setText("Timer:");
		player1TimerHeading.setFill(Color.RED);
		player1TimerHeading.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

		timer1 = new Text("00:00:000");
		timer1.setFill(Color.WHITE);
		timer1.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

		timelinePlayer1 = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				changeTimer1(timer1);
			}
		}));

		timelinePlayer1.setCycleCount(Timeline.INDEFINITE);
		timelinePlayer1.setAutoReverse(false);

		Text player1ScoreHeading = new Text();
		player1ScoreHeading.setText("Score:");
		player1ScoreHeading.setFill(Color.RED);
		player1ScoreHeading.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

		player1ScoreDisplay = new Text();
		player1ScoreDisplay.setText("0");
		player1ScoreDisplay.setFill(Color.WHITE);
		player1ScoreDisplay.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

		hBox = new HBox(10, player1TimerHeading, timer1);
		hBox1 = new HBox(10, player1ScoreHeading, player1ScoreDisplay);

		VBox player1Details = new VBox(20, player1Summary, hBox, hBox1);
		player1Details.setLayoutX(50);
		player1Details.setLayoutY(100);
		

		root.getChildren().add(player1Details);

		// Player 2

		Text player2Summary = new Text();
		player2Summary.setText("Player 2");
		player2Summary.setFill(Color.GOLD);
		player2Summary.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 24));

		Text player2TimerHeading = new Text();
		player2TimerHeading.setText("Timer:");
		player2TimerHeading.setFill(Color.RED);
		player2TimerHeading.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

		timer2 = new Text("00:00:000");
		timer2.setFill(Color.WHITE);
		timer2.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

		timelinePlayer2 = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				changeTimer2(timer2);
			}
		}));
		timelinePlayer2.setCycleCount(Timeline.INDEFINITE);
		timelinePlayer2.setAutoReverse(false);

		Text player2ScoreHeading = new Text();
		player2ScoreHeading.setText("Score:");
		player2ScoreHeading.setFill(Color.RED);
		player2ScoreHeading.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

		player2ScoreDisplay = new Text();
		player2ScoreDisplay.setText("0");
		player2ScoreDisplay.setFill(Color.WHITE);
		player2ScoreDisplay.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

		hBox = new HBox(10, player2TimerHeading, timer2);
		hBox1 = new HBox(10, player2ScoreHeading, player2ScoreDisplay);
		VBox player2Details = new VBox(20, player2Summary, hBox, hBox1);
		player2Details.setLayoutX(1100);
		player2Details.setLayoutY(100);

		root.getChildren().add(player2Details);
		ArrayList<Rectangle> shipList = new ArrayList<Rectangle>() {
			{
				add(ship1);
				add(ship2);
				add(ship3);
				add(ship4);
				add(ship5);
			}
		};
		root.getChildren().add(ship1);
		root.getChildren().add(ship2);
		root.getChildren().add(ship3);
		root.getChildren().add(ship4);
		root.getChildren().add(ship5);
		paintShip(shipList, "/ship1_Destroyer.png");

		// End Graphics

		EventHandler<MouseEvent> event = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				if (!executing)
					return;
				Cell cell = (Cell) e.getSource();
				if (cellsSelected.contains(cell.row + "" + cell.col)) {
					return;
				}
				cellsSelected.add(cell.row + "" + cell.col);
				cell.setFill(Color.GREY);
				if (cell.targetHit)
					return;
				numberOfShots.add(cell);
				if (!twoPlayer) {
					if (normalGame) {
						shootNormalShip(numberOfShots, personStage);
						numberOfShots.clear();
					} else if (salvation) {
						if (hits == firstPlayerBoard.amountOfships) {
							shootSalvationShip(numberOfShots, personStage);
						} else
							hits++;
					} else if (suggSalvation) {
						shootNormalShip(numberOfShots, personStage);
						numberOfShots.clear();
					}
				} else {

					previoustime2 = Integer.parseInt(timer2.getText().split(":")[0]) * 60
							+ Integer.parseInt(timer2.getText().split(":")[1]);
					if (normalGame) {
						shootNormalShip(numberOfShots, personStage);
						numberOfShots.clear();
					} else if (salvation) {
						if (hits == firstPlayerBoard.amountOfships) {
							shootSalvationShip(numberOfShots, personStage);
						} else
							hits++;
					} else if (suggSalvation) {
						shootNormalShip(numberOfShots, personStage);
						numberOfShots.clear();
					}
				}

			}

		};

		selectedShip = ship1;
		installBoatListeners(ship1);
		installBoatListeners(ship2);
		installBoatListeners(ship3);
		installBoatListeners(ship4);
		installBoatListeners(ship5);

		
		opponentBoard = new Board(true, event);

		firstPlayerBoard = new Board(false, null);

		firstPlayerBoard.setLayoutX(250);
		firstPlayerBoard.setLayoutY(120);
		opponentBoard.setLayoutX(750);
		opponentBoard.setLayoutY(120);
		ai.reset();
		root.getChildren().add(firstPlayerBoard);
		root.getChildren().add(opponentBoard);
		root.setBackground(background);

		return root;
	}

	

	/**
	 * <p>
	 * This method registers every single ship with following listeners:
	 * </p>
	 * <ol>
	 * <li>MousePressed Listener to change cursor appearance and get current
	 * position coordinates when mouse is Pressed on the ship but not released</li>
	 * <li>MouseEntered Listener for changing the cursor appearance when cursor is
	 * hovered on the ship</li>
	 * <li>MouseClicked Listener for changing the orientation of the ship when mouse
	 * click happens on any ship</li>
	 * <li>MouseDragged Listener which handles the ship dragging when the ship
	 * rectangle is dragged</li>
	 * <li>MouseRelease Listener which handles the ship placement when the dragging
	 * operation ends</li>
	 * </ol>
	 * 
	 * @param boat
	 *            - It denotes the ship rectangle
	 */
	private void installBoatListeners(Node boat) {

		// Handle dragging, using help from
		// http://stackoverflow.com/questions/22139615/dragging-buttons-in-javafx
		boat.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {

				needToRotate = true;
				Rectangle rect = (Rectangle) boat;
				select(rect);
				if (boat.rotateProperty().getValue() == 0) {
					isRotated = false;
				} else {
					isRotated = true;
				}
				double localX = rect.getX();
				double localY = rect.getY();

				if (isRotated) {
					localX = localX + selectedShip.getWidth() / 2;
					localY = localY - selectedShip.getWidth() / 2 + cellSize;
				}

				boat.setCursor(Cursor.MOVE);
			}
		});
		boat.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {

				boat.setCursor(Cursor.HAND);

				boat.toFront();
				double localX = 0;
				double localY = 0;

				if (isRotated) {
					if (selectedShip.getWidth() > 120) {
						localX = mouseEvent.getX() - 200;
						localY = mouseEvent.getY() - 195;
					} else if (selectedShip.getWidth() <= 120 && selectedShip.getWidth() > 90) {
						localX = mouseEvent.getX() - 210;
						localY = mouseEvent.getY() - 175;
					} else if (selectedShip.getWidth() <= 90 && selectedShip.getWidth() > 60) {
						localX = mouseEvent.getX() - 220;
						localY = mouseEvent.getY() - 164;
					} else if (selectedShip.getWidth() <= 60 && selectedShip.getWidth() > 30) {
						localX = mouseEvent.getX() - 230;
						localY = mouseEvent.getY() - 145;
					}

				} else {
					localX = mouseEvent.getX() - 250;
					localY = mouseEvent.getY() - 120;
				}

				int size = (int) ((int) selectedShip.getWidth() / cellSize);

				int shipLength = 0;
				String shipName = "";
				if (firstPlayerBoard.contains(localX, localY)) {
					int x = (int) (localX / cellSize);
					int y = (int) (localY / cellSize);

					if (selectedShip.getWidth() > 120) {
						shipLength = 5;
						shipName = "ship1";
					} else if (selectedShip.getWidth() <= 120 && selectedShip.getWidth() > 90) {
						shipLength = 4;
						shipName = "ship2";
					} else if (selectedShip.getWidth() <= 90 && selectedShip.getWidth() > 60) {
						shipLength = 3;
						if (selectedShip == ship3)
							shipName = "ship3";
						else
							shipName = "ship4";
					} else if (selectedShip.getWidth() <= 60 && selectedShip.getWidth() > 30) {
						shipLength = 2;
						shipName = "ship5";
					}

					if (firstPlayerBoard.positionShip(new Ship(shipLength, isRotated == true), x, y, false)) {
						--numberOfShips;
						currentShip++;
						strToShip.put(shipName, selectedShip);

						dragAndDropShips.put(selectedShip, x + "-" + y + "-" + isRotated + "-" + shipLength);
						selectedShip.setDisable(true);
						selectedShip.setOpacity(0);

						Cell r = (Cell) firstPlayerBoard.getCell(x, y);

						if (isRotated) {
							selectedShip.setX(r.getLayoutX() + r.getParent().getTranslateX()
									- selectedShip.getWidth() / 2 + cellSize / 2);
							selectedShip.setY(r.getLayoutY() + r.getParent().getTranslateY() + size * cellSize
									- selectedShip.getWidth() / 2 - cellSize / 2 + 5);

						} else {

							selectedShip.setLayoutX(0);
							selectedShip.setLayoutY(0);

						}

					} else {
						try {
							if ( !(firstPlayerBoard.positionShip(new Ship(shipLength, isRotated == true), x, y, false)) ) {
								throw new InvalidShipPlacementException("Ship has not been placed Properly - Ships should not touch/overlap each other");
							}
						} catch (InvalidShipPlacementException e) {
							System.out.println("Checked Exception "+ e);
						}

						backtoHome(shipLength, selectedShip);

					}

				} else {
					try {
						if (! (firstPlayerBoard.contains(localX, localY)) ) {
							throw new BeyondGridBoundsException("Ship has not been released within the board Coordinate bounds");
						}
					} catch (BeyondGridBoundsException e) {
						System.out.println("Checked Exception "+ e);
					}
					backtoHome(shipLength, selectedShip);
				}
			}
		});
		boat.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {

				needToRotate = false;
				if (boat.rotateProperty().getValue() == 0) {
					isRotated = false;
				} else {
					isRotated = true;
				}
				boat.toFront();

				Rectangle temp = (Rectangle) boat;

				if (isRotated) {

					temp.setX(mouseEvent.getSceneX() - (temp.getBoundsInLocal().getWidth() / 2));
					temp.setY(mouseEvent.getSceneY() + (temp.getBoundsInLocal().getWidth() / 3));

				} else {

					temp.setX(mouseEvent.getSceneX());
					temp.setY(mouseEvent.getSceneY());
				}
			}
		});
		boat.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				boat.setCursor(Cursor.HAND);
			}
		});
		boat.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				if (needToRotate) {

					if (boat.rotateProperty().getValue() == 0) {
						boat.setRotate(90.0);
					} else
						boat.setRotate(0);
				}
			}

		});
	}

	/**
	 * <p>
	 * This method generates alert for re-adjusting the ship only during placement
	 * before the start of the game
	 * </p>
	 * <p>
	 * It also contains the logic for ship re-adjustment
	 * </p>
	 * 
	 */

	private void adjustShips() {
		Alert gameModeAlert = new Alert(AlertType.INFORMATION);

		ButtonType Ship1 = new ButtonType("Ship1");
		ButtonType Ship2 = new ButtonType("Ship2");
		ButtonType Ship3 = new ButtonType("Ship3");
		ButtonType Ship4 = new ButtonType("Ship4");
		ButtonType Ship5 = new ButtonType("Ship5");
		ButtonType cancelButtonType = new ButtonType("Close");

		gameModeAlert.setTitle("Ship Adjustment");
		gameModeAlert.setHeaderText("Select the ship you want to remove");
		gameModeAlert.setContentText("Ship 1 = Length 5 and so on");
		gameModeAlert.getButtonTypes().setAll(Ship1, Ship2, Ship3, Ship4, Ship5, cancelButtonType);
		Optional<ButtonType> result = gameModeAlert.showAndWait();

		String retShip = "";

		if (result.get() == Ship1)
			retShip = "ship1";
		else if (result.get() == Ship2)
			retShip = "ship2";
		else if (result.get() == Ship3)
			retShip = "ship3";
		else if (result.get() == Ship4)
			retShip = "ship4";
		else if (result.get() == Ship5)
			retShip = "ship5";

		String cordinates[];
		if (dragAndDropShips.containsKey(strToShip.get(retShip))) {
			numberOfShips++;
			cordinates = dragAndDropShips.get(strToShip.get(retShip)).split("-");
			firstPlayerBoard.positionShip(
					new Ship(Integer.parseInt(cordinates[3]), cordinates[2].equalsIgnoreCase("true") ? true : false),
					Integer.parseInt(cordinates[0]), Integer.parseInt(cordinates[1]), true);
			dragAndDropShips.remove(strToShip.get(retShip));
			backtoHome(Integer.parseInt(cordinates[3]), strToShip.get(retShip));

		}

	}

	/**
	 * <p>
	 * This method places the ships back to their original location if the user does
	 * not provide a valid placement position for them
	 * </p>
	 * 
	 * @param len
	 *            - Length of the ship { 5,4,3,2 }
	 * @param shipSelect
	 *            - the currently selected ship
	 */
	private void backtoHome(int len, Rectangle shipSelect) {
		shipSelect.setDisable(false);
		if (len == 5 || shipSelect.getWidth() > 120) {
			shipSelect.setX(50);
			shipSelect.setY(450);
			shipSelect.setOpacity(100);
		} else if (len == 4 || (shipSelect.getWidth() <= 120 && shipSelect.getWidth() > 90)) {
			shipSelect.setX(50);
			shipSelect.setY(490);
			shipSelect.setOpacity(100);
		} else if (len == 3 || (shipSelect.getWidth() <= 90 && shipSelect.getWidth() > 60)) {
			if (shipSelect == ship3) {
				shipSelect.setX(50);
				shipSelect.setY(530);
				shipSelect.setOpacity(100);
			} else if (shipSelect == ship4) {
				shipSelect.setX(50);
				shipSelect.setY(570);
				shipSelect.setOpacity(100);
			}

		} else if (len == 2 || (shipSelect.getWidth() <= 60 && shipSelect.getWidth() > 30)) {
			shipSelect.setX(50);
			shipSelect.setY(610);
			shipSelect.setOpacity(100);
		}
	}

	/**
	 * <p>
	 * This method selects/highlights the current rectangle on which the mouse has
	 * been pressed
	 * </p>
	 * 
	 * 
	 * @param boat
	 *            - the ship(rectangle) to be selected/highlighted
	 * @return - the currently selected ship/rectangle
	 */
	private Rectangle select(Rectangle boat) {
		selectedShip.setStroke(Color.WHITE);
		selectedShip.setStrokeWidth(1.0);
		if (boat != null) {
			boat.setStrokeWidth(2.5);
			boat.setStroke(Color.WHITE);
		}
		selectedShip = boat;
		return selectedShip;
	}

	/**
	 * <p>
	 * setting styling effects for different buttons like pause,start,rest,load on
	 * output screen. Showing code for start button.Done similarly for other buttons
	 * defined in program.
	 * </p>
	 * <code>
	 * st.setStyle("-fx-background-color: #000000;-fx-font-size: 2em;-fx-text-fill:#ffffff;");
	 * st.setMinHeight(80);
	 * st.setMinWidth(150);
	 * </code>
	 */
	private void buttonGeometry() {

		st.setStyle("-fx-background-color: #000000;-fx-font-size: 2em;-fx-text-fill:#ffffff;");
		reset.setStyle("-fx-background-color: #000000;-fx-font-size: 2em;-fx-text-fill:#ffffff;");
		adjust.setStyle("-fx-background-color: #000000;-fx-font-size: 2em;-fx-text-fill:#ffffff;");
		load.setStyle("-fx-background-color: #000000;-fx-font-size: 2em;-fx-text-fill:#ffffff;");
		save.setStyle("-fx-background-color: #000000;-fx-font-size: 2em;-fx-text-fill:#ffffff;");
		p2p.setStyle("-fx-background-color: #000000;-fx-font-size: 2em;-fx-text-fill:#ffffff;");

		exit.setStyle("-fx-background-color: #000000;-fx-font-size: 2em;-fx-text-fill:#ffffff;");
		doNotCheat.setStyle("-fx-background-color: #000000;-fx-font-size: 2em;-fx-text-fill:#ffffff;");

		st.setMinHeight(80);
		st.setMinWidth(100);

		reset.setMinHeight(80);
		reset.setMinWidth(150);

		adjust.setMinHeight(80);
		adjust.setMinWidth(150);

		load.setMinHeight(80);
		load.setMinWidth(150);

		save.setMinHeight(80);
		save.setMinWidth(150);

		exit.setMinHeight(80);
		exit.setMinWidth(100);

		p2p.setMinHeight(80);
		p2p.setMinWidth(100);

		doNotCheat.setMinHeight(80);
		doNotCheat.setMinWidth(150);

	}

	/**
	 * <p>
	 * This method updates the text field of scores when any player earns points
	 * </p>
	 * 
	 * @param player
	 *            - the player can be either user(firstPlayer) or opponent/Player 2
	 */
	private void displayScore(String player) {
		if (player.equalsIgnoreCase("player1"))
			player1ScoreDisplay.setText(player1Score + "");
		else
			player2ScoreDisplay.setText(player2Score + "");
	}

	/**
	 * <p>
	 * This function helps the FirstPlayer to shoot ships during Normal Mode
	 * </p>
	 * 
	 * @param cellHits
	 *            - the arrayList holding the selected cell ready for hitting
	 * @param personStage
	 *            - root ( JavaFX game Stage)
	 */
	public void shootNormalShip(ArrayList<Cell> cellHits, Stage personStage) {

		for (Cell cell : cellHits) {
			if (cell.targetHit)
				return;

			if(twoPlayer)
				udpSend("shootShip->(" + cell.row + "-" + cell.col + ")");
			
			opponentTurn = !cell.shoot();
			
			if (opponentTurn) {
				timelinePlayer1.pause();

				timelinePlayer2.play();

				if (suggSalvation) {
					for (Rectangle rect : dragAndDropShipsOpponent.keySet()) {
						String takeCordinates[] = dragAndDropShipsOpponent.get(rect).split("-");
						Cell temp = opponentBoard.getCell(Integer.parseInt(takeCordinates[0]),
								Integer.parseInt(takeCordinates[1]));
						temp.setFill(Color.WHITE);
						temp.setStroke(Color.BLACK);
					}

					checkTimeForSug = false;
				}

				if (!twoPlayer) {
					opponentNormalMove(personStage);
				} else {
					opponentBoard.setDisable(true);
				
				}
			} else {
				Rectangle deleteCell = null;
				if (suggSalvation) {
					if (checkForSugg) {
						for (Rectangle rect : dragAndDropShipsOpponent.keySet()) {
							String takeCordinates[] = dragAndDropShipsOpponent.get(rect).split("-");
							if (Integer.parseInt(takeCordinates[0]) == cell.row
									&& Integer.parseInt(takeCordinates[1]) == cell.col) {
								deleteCell = rect;
							} else {
								Cell temp = opponentBoard.getCell(Integer.parseInt(takeCordinates[0]),
										Integer.parseInt(takeCordinates[1]));
								temp.setFill(Color.WHITE);
								temp.setStroke(Color.BLACK);
							}
						}
						dragAndDropShipsOpponent.remove(deleteCell);
					}
				}

				currenttime = Integer.parseInt(timer1.getText().split(":")[0]) * 60
						+ Integer.parseInt(timer1.getText().split(":")[1]);
				
				if (currenttime - previoustime < 2)
					player1Score += 5;
				else if (currenttime - previoustime < 5 && currenttime - previoustime > 2)
					player1Score += 3;
				else if (currenttime - previoustime > 5 && currenttime - previoustime < 10)
					player1Score += 2;
				else if (currenttime - previoustime > 10)
					player1Score += 1;

				previoustime = currenttime;
				displayScore("player1");
			}

			if (opponentBoard.amountOfships == 0) {

				timelinePlayer1.pause();
				timelinePlayer2.pause();
				String s = "You Won This Game";
				finalResultDisplay(s, personStage);

			}
		}

	}

	/**
	 * <p>
	 * This function is used by the AI/ opponent to shoot the cells on FirstPlayer
	 * board during Salva Mode
	 * </p>
	 * 
	 * @param cellHits
	 *            -
	 * @param personStage - root / JavaFX Stage
	 */
	public void shootSalvationShip(ArrayList<Cell> cellHits, Stage personStage) {
		currenttime = Integer.parseInt(timer1.getText().split(":")[1]);
		String sendString ="shootShip->";

		for (Cell cell : cellHits) {
			sendString = sendString + "(" + cell.row + "-" + cell.col + "),";
		}

		if(twoPlayer)
			udpSend(sendString);

		for (Cell cell : cellHits) {

			
			opponentTurn = !cell.shoot();
			
			if (!opponentTurn) {
				if (currenttime - previoustime < 2)
					player1Score += 5;
				else if (currenttime - previoustime < 5 && currenttime - previoustime > 2)
					player1Score += 3;
				else if (currenttime - previoustime > 5 && currenttime - previoustime < 10)
					player1Score += 2;
				else if (currenttime - previoustime > 10)
					player1Score += 1;

			}
			if (opponentBoard.amountOfships == 0) {

				String s = "You Won This Game";
				finalResultDisplay(s, personStage);

			}
		}


		displayScore("player1");

		previoustime = currenttime;
		timelinePlayer1.pause();

		timelinePlayer2.play();

		hits = 1;
		numberOfShots.clear();
		if(!twoPlayer)
			opponentSalvationMove(personStage);
		else
			opponentBoard.setDisable(true);

	}

	/**
	 * This method is AI which will detect the move on Player 1 Board.
	 * 
	 * @param personStage
	 *            - root(JavaFX Game Stage)
	 */
	private void opponentNormalMove(Stage personStage) {
		int x, y;
		int oldValue, newValue;
		while (opponentTurn) {

			x = ai.nextX();
			y = ai.nextY();

			Cell cell = firstPlayerBoard.getCell(x, y);
			if (cell.targetHit) {
				ai.generate();
				continue;
			}
			oldValue = firstPlayerBoard.amountOfships;
			
			opponentTurn = cell.shoot();
			
			if (!opponentTurn) {
				ai.feedback(false, false);
				timelinePlayer2.pause();

				checkTimeForSug = true;
				if (suggSalvation) {
					Thread t2 = new Thread(checkTime, "T2");
					t2.start();
				}
				timelinePlayer1.play();

			} else {
				newValue = firstPlayerBoard.amountOfships;
				if (oldValue != newValue) { 	// if PC is guessing AND ship is destroyed
					ai.feedback(true, true);
				} else {
					ai.feedback(true, false);
				}
				player2Score += 5;
				displayScore("player2");
			}

			if (firstPlayerBoard.amountOfships == 0) {

				timelinePlayer1.pause();
				timelinePlayer2.pause();
				String s = "You Lost This Game to the Computer";
				finalResultDisplay(s, personStage);

			}

		}
	}

	/**
	 * This method handles the moves played by the AI / opponent in Salva Mode
	 * 
	 * @param personStage
	 *            - the game Stage (root)
	 */
	private void opponentSalvationMove(Stage personStage) {
		ArrayList<String> takenCellStrings = new ArrayList<String>();
		for (int i = 0; i < opponentBoard.amountOfships; i++) {
			int x = random.nextInt(10);
			int y = random.nextInt(10);
			if (takenCellStrings.contains(x + "" + y)) {
				i--;
				continue;

			}
			takenCellStrings.add(x + "" + y);

			Cell cell = firstPlayerBoard.getCell(x, y);

			if (cell.targetHit) {
				i--;
				continue;
			} else {
				numberOfShots.add(cell);
			}
		}

		for (Cell cell : numberOfShots) {

			opponentTurn = cell.shoot();
		
			if (opponentTurn) {
				player2Score += 5;
				displayScore("player2");
			}
			if (firstPlayerBoard.amountOfships == 0) {

				String s = "You Lost This Game to the Computer";
				finalResultDisplay(s, personStage);

			}

		}
		timelinePlayer2.pause();

		timelinePlayer1.play();
		numberOfShots.clear();
		hits = 1;
	}

	/**
	 * <p>
	 * This method generated an alert when a ship is sunk on either player board or
	 * opponent board
	 * </p>
	 * 
	 * @param s-this
	 *            is to denote player/opponent
	 * @param x-x
	 *            coordinate of the alert position
	 * @param y-y
	 *            coordinate of the alert position
	 */
	public static void shipDestructionMessage(String s, double x, double y) {
		if (twoPlayer) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert shipSinkAlert = new Alert(AlertType.WARNING);
					shipSinkAlert.setTitle("SHIP SUNK");
					shipSinkAlert.setHeaderText(null);
					shipSinkAlert.setContentText(s + " ship has been destroyed");
					shipSinkAlert.setX(x);
					shipSinkAlert.setY(y);
					shipSinkAlert.showAndWait();
				}
			});
		} else {
			Alert shipSinkAlert = new Alert(AlertType.WARNING);
			shipSinkAlert.setTitle("SHIP SUNK");
			shipSinkAlert.setHeaderText(null);
			shipSinkAlert.setContentText(s + " ship has been destroyed");
			shipSinkAlert.setX(x);
			shipSinkAlert.setY(y);
			shipSinkAlert.showAndWait();
		}
	}

	/**
	 * This method will Display the final result on the pop-up showing who the
	 * winner is.
	 * 
	 * @param s
	 *            -String that specifies a text notifying when one player wins.
	 *
	 * @param personStage
	 *            - root ( JavaFX game Stage)
	 */
	private void finalResultDisplay(String s, Stage personStage) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ButtonType buttonTypeOne = new ButtonType("YES");
				ButtonType buttonTypeTwo = new ButtonType("NO");

				Alert winOrLose = new Alert(AlertType.CONFIRMATION);
				winOrLose.setTitle("WINNER ANNOUCEMENT");
				winOrLose.setHeaderText(s);

				winOrLose.setContentText("Click YES to Restart the Game\nClick NO to Exit the Game");
				winOrLose.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

				Optional<ButtonType> result = winOrLose.showAndWait();

				if (result.get() == buttonTypeOne) {
					restart(personStage);
				} else if (result.get() == buttonTypeTwo) {
					System.exit(0);
				}
			}
		});

	}

	/**
	 * <p>
	 * This method will Start the Game once the Player Click the start button and
	 * Player 1 has set up all his ships.
	 * </p>
	 */
	private void startGame() {
		// place enemy ships
		if (!loadCheck && !twoPlayer) {
			numberOfShips = 5;
			for (int i = 0; i < shipLengths.size(); i++) {

				int x = random.nextInt(10);
				int y = random.nextInt(10);
				boolean direct = Math.random() < 0.5;
				if (opponentBoard.positionShip(new Ship(shipLengths.get(i), direct), x, y, false)) {
					if (!opponetShipDetails.containsKey(shipLengths.get(i))) {
						opponetShipDetails.put(shipLengths.get(i),
								x + "-" + y + "-" + direct + "-" + shipLengths.get(i));
					} else {
						opponetShipDetails.put(shipLengths.get(i) - 2,
								x + "-" + y + "-" + direct + "-" + shipLengths.get(i));

					}
					if (direct) {
						for (int k = y; k < y + shipLengths.get(i); k++) {
							dragAndDropShipsOpponent.put(opponentBoard.getCell(x, k), x + "-" + k);
						}
					} else {
						for (int k = x; k < x + shipLengths.get(i); k++) {
							dragAndDropShipsOpponent.put(opponentBoard.getCell(k, y), k + "-" + y);
						}
					}

					numberOfShips--;
				} else {
					i -= 1;
				}
			}
		}
		showGameMessage();

	}

	/**
	 * <p>this method shows the different gameplay options</p>
	 */
	private void showGameMessage() {
		
		Alert gameModeAlert = new Alert(AlertType.INFORMATION);

		ButtonType buttonSalva = new ButtonType("SALVA");
		ButtonType buttonSuggSalva = new ButtonType("SUGG SALVA");
		ButtonType buttonNormal = new ButtonType("NORMAL");

		gameModeAlert.setTitle("SELECT GAME MODE");

		gameModeAlert.setContentText("Click on the desired button to choose game mode");
		gameModeAlert.getButtonTypes().setAll(buttonSalva,buttonNormal);

		Optional<ButtonType> result = gameModeAlert.showAndWait();

		if (result.get() == buttonSalva) {

			salvation = true;
			normalGame = false;

		} else if (result.get() == buttonNormal) {

			normalGame = true;
			salvation = false;
		} else if (result.get() == buttonSuggSalva) {

			suggSalvation = true;
			normalGame = false;
			salvation = false;
			t1.start();
		}
		String playerBoardInfo = getShiPosition("player");
		
	
		udpSend(playerBoardInfo);

		executing = true;
		timelinePlayer1.play();
		previoustime = Integer.parseInt(timer1.getText().split(":")[0]) * 60
				+ Integer.parseInt(timer1.getText().split(":")[1]);
		;
	}

	/**
	 * <p>
	 * This method displays all the ships on the opponent board in Golden Color
	 * </p>
	 * 
	 * @param opponentBoard
	 *            - opponent players grid
	 */
	private void seeOpponentShips(Board opponentBoard) {
		for (int y = 0; y < 10; y++) {

			for (int x = 0; x < 10; x++) {

				Cell cell = opponentBoard.getCell(y, x);
				if (cell.ship != null) {
					if (!cell.targetHit) {
						if (isCheating)
							cell.setFill(Color.GOLD);
						else
							cell.setFill(Color.WHITE);

					}
				}

			}

		}
	}

	/**
	 * <p>
	 * Method will reset the Game by initializing all the related Nodes.
	 * </p>
	 * 
	 * @param primaryStage -- (root) JavaFX Stage
	 */
	private void restart(Stage primaryStage) {

		primaryStage.close();
		Platform.runLater(() -> {
			try {
				new Battle().start(new Stage());
			} catch (Exception e) {

				e.printStackTrace();
			}
		});

	}

	/**
	 * <p>
	 * This Method will initialize the Primary stage with the necessary elements in
	 * it
	 * </p>
	 * 
	 * @param primaryStage
	 *            - root (JAVAFX Game Stage)
	 */
	private void intialise(Stage primaryStage) {
		global_stage = primaryStage;
		File n = new File(".");
		String path = null;
		try {
			path = n.getCanonicalFile() + "/ship.png";
		} catch (IOException e) {

			e.printStackTrace();
		}
		Image image = new Image("file:///" + path);

		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);

		Background background = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size));

		Scene scene = new Scene(designBoard(primaryStage, background));
		primaryStage.setTitle("Battleship");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);

		exit.setOnAction(e -> {
			System.exit(0);
		});


		st.setOnAction(e -> {
			if (numberOfShips == 0) {
				startGame();
				adjust.setDisable(true);
				p2p.setDisable(true);
			}
			else {
				try {
					if(numberOfShips!=0) {
						throw new StartGameException("Cannot start single player game till player places all the ships");
					}
				} catch (Exception e1) {
					System.out.println("Checked Exception "+ e1);
				}
			}
		});

		reset.setOnAction(e -> {
			restart(primaryStage);
		});

		doNotCheat.setOnAction(e -> {

			seeOpponentShips(opponentBoard);

			isCheating = !isCheating;

		});
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT files (*.TXT)", "*.TXT"),
				new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt"));

		load.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					openFile(file);
				}
			}
		});

		save.setOnAction(event -> {
			if(numberOfShips ==5) {
				try {

					throw new InvalidButtonPressException("Save cannot be pressed when both boards are empty");


				} 	catch (InvalidButtonPressException e) {
					System.out.println("Checked Exception "+e);
				}

			}
			else {

				FileChooser fileChooser = new FileChooser();
				timelinePlayer1.pause();
				timelinePlayer2.pause();
				// Set extension filter for text files
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
				fileChooser.getExtensionFilters().add(extFilter);

				// Show save file dialog
				File file = fileChooser.showSaveDialog(primaryStage);

				if (file != null) {
					saveTextToFile(file);

				}
			}


		});

		adjust.setOnAction(e -> {
			adjustShips();
		});

		//starting 2 player mode
		p2p.setOnAction(e -> {
			if (numberOfShips == 0) {
				System.out.println("2 Player Mode Started");
				twoPlayer = true;
				runInit();
				startGame();
				adjust.setDisable(true);
				st.setDisable(true);
			}
			else {
				try {
					throw new StartGameException("Cannot start 2 player game till player 1 places all the ships");
				} catch (Exception e2) {
					System.out.println("Checked Exception "+e2);
				}
			}
		});

		primaryStage.show();
	}

	Runnable udp_task = new Runnable() {
		public void run() {
			System.out.println("System 1 Listening on 6000");
			udpReceive();
		}
	};

	/**
	 * <p>
	 * Starting the thread containing udpReceive method
	 * </p>
	 */
	public void runInit() {
		Thread thread = new Thread(udp_task);
		thread.start();
	}

	/**
	 * <p>
	 * This method is used  to receive the data from opponent player.
	 * Data contains -
	 * <ol>
	 * <li>Coordinates select by another player</li>
	 * <li>Feedback received from another player</li>
	 * </ol>
	 * </p>
	 */
	private void udpReceive() {
	
		byte[] buffer = new byte[1000];
		DatagramSocket aSocket = null;

		try {
			aSocket = new DatagramSocket(6001);

			while (true) {
				Arrays.fill(buffer, (byte) 0);

				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

				aSocket.receive(reply);
			
				System.out.println("player1 playing here");
				String receivedData[] = data(buffer).toString().split("->");

				if (receivedData[0].trim().equals("playerShips")) {
					String udpData[] = receivedData[1].split(",");
					for (int i = 0; i < udpData.length; i++) {
						String udpTemp[] = udpData[i].substring(1, udpData[i].length() - 1).split("-");
						callPostionShip("opponent", Integer.parseInt(udpTemp[0].trim()),
								Integer.parseInt(udpTemp[1].trim()), Boolean.parseBoolean(udpTemp[2].trim()),
								Integer.parseInt(udpTemp[3].trim()));
					}

				} else if(receivedData[0].trim().equals("shootShip")) {
					ArrayList<Cell> udpCell = new ArrayList<Cell>();
					String udpData[] = receivedData[1].split(",");

					for (int i = 0; i < udpData.length; i++) {
						String udpTemp[] = udpData[i].trim().substring(1, udpData[i].length() - 1).split("-");
						Cell c = firstPlayerBoard.getCell(Integer.parseInt(udpTemp[0].trim()),
								Integer.parseInt(udpTemp[1].trim()));
						udpCell.add(c);

					}
					if(udpCell.size()==1 && !salvation)
						shootMultiPlayer(udpCell);
					else{
						shootMutliPlayerSal(udpCell);
					}
				}
				else {
					throw new InvalidDataReceivedException("Data received is invalid");
				}

			}
		}
		catch (InvalidDataReceivedException e) {
			System.out.println("Checked Exception "+e);
		} 
		catch (SocketException e1) {
		
			e1.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally {
			aSocket.close();
		}
	}

	/**
	 * <p>
	 * This method is called when normal mode is ON.
	 * This method takes the cells to be shot as input and calls the shoot method.
	 * @param udpCell - The list of cells selected by another player.
	 * </p>
	 */
	
	private void shootMultiPlayer(ArrayList<Cell> udpCell) {
		
		for (Cell cell : udpCell) {
			if (cell.targetHit)
				return;

			
			opponentTurn = cell.shoot();
		

			if (!opponentTurn) {
				timelinePlayer1.play();

				timelinePlayer2.pause();
				opponentBoard.setDisable(false);
				
			} else {
				
				currenttime2 = Integer.parseInt(timer2.getText().split(":")[0]) * 60
						+ Integer.parseInt(timer2.getText().split(":")[1]);
				
				if (currenttime2 - previoustime2 < 2)
					player2Score += 5;
				else if (currenttime2 - previoustime2 < 5 && currenttime2 - previoustime2 > 2)
					player2Score += 3;
				else if (currenttime2 - previoustime2 > 5 && currenttime2 - previoustime2 < 10)
					player2Score += 2;
				else if (currenttime2 - previoustime2 > 10)
					player2Score += 1;

				previoustime2 = currenttime2;
				displayScore("player2");

				if (firstPlayerBoard.amountOfships == 0) {

					timelinePlayer1.pause();
					timelinePlayer2.pause();
					String s = "You Lost This Game to the Player 1";
					finalResultDisplay(s, global_stage);

				}
			}
		}
	}

	/**
	 * <p>
	 * This method is called when Salva mode is ON.
	 * This method takes the cells to be shot as input and calls the shoot method.
	 * @param udpCell - The list of cells selected by another player.
	 * </p>
	 */
	
	private void shootMutliPlayerSal(ArrayList<Cell> udpCell) {

		for (Cell cell : udpCell) {

			
			opponentTurn = cell.shoot();
		
			if (opponentTurn) {
				player2Score += 5;
				displayScore("player2");
			}
			if (firstPlayerBoard.amountOfships == 0) {

				String s = "You Lost This Game to the Computer";
				finalResultDisplay(s, global_stage);

			}

		}
		timelinePlayer2.pause();

		timelinePlayer1.play();
		numberOfShots.clear();
		hits = 1;
		opponentBoard.setDisable(false);

	}

	
	
	/**
	 * <p>
	 * This method is used to send the data to opponent player.
	 * Data contains -
	 * <ol>
	 * <li>Coordinates select by  player1</li>
	 * <li>Feedback to be sent to player 2</li>
	 * </ol>
	 * </p>
	 */



	
	private void udpSend(String textToSend) {
		
		byte[] message = textToSend.getBytes();
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket();

			InetAddress aHost = InetAddress.getByName("192.168.43.29");

			// Sequencer port number
			int serverPort = 6000;

			DatagramPacket request = new DatagramPacket(message, message.length, aHost, serverPort);// request packet
			
			// request sent out
			aSocket.send(request);

			if(request.getLength()==0) {
				throw new DataNotSentException("Data could not be sent over the connection");
			}
			if (textToSend.length() > 50) {
				System.out.println("Board info Sent");
			} else {
				System.out.println("HIT coordinates sent");
			}

		}
		catch (DataNotSentException e) {
			System.out.println("Checked Exception "+e);
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>
	 * This method converts received data from byte to string 
	 * @param a - message in bytes
	 * @return - message in string
	 *</p>
	 */
	public static StringBuilder data(byte[] a) {
		if (a == null)
			return null;
		StringBuilder ret = new StringBuilder();
		int i = 0;
		while (a[i] != 0) {
			ret.append((char) a[i]);
			i++;
		}
		return ret;
	}

	/**
	*<p>
	 * This method writes data into the opened file.
	 *The data is in string format
	 * @param file - file in which data is to be written.
	 *</p>
	 */
	private void saveTextToFile(File file) {
		String playerBoardInfo = getBoardInformation(firstPlayerBoard);
		String opponentBoardInfo = getBoardInformation(opponentBoard);

		String content = "Player_1 @ " + "Timer ->" + timer1.getText() + "; Score ->" + player1Score + ";"
				+ getShiPosition("player") + ";" + "Board ->" + playerBoardInfo + ";" + "\n" + "Player_2 @ "
				+ "Timer ->" + timer2.getText() + "; Score ->" + player2Score + ";" + getShiPosition("opponent") + ";"
				+ "Board ->" + opponentBoardInfo + ";";
		try {
			PrintWriter writer;
			writer = new PrintWriter(file);
			writer.println(content);
			writer.close();
			System.out.println("File Formed");
		} catch (IOException ex) {
			System.out.println("FIle not formed");
			Logger.getLogger(Battle.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	/**
	 * <p>
	 * This method takes player ship positions and appends them to a string.
	 * @param playerCheck - To identify player or opponent.
	 * @return - String containing ship position information.
	* </p>
	 */
	private String getShiPosition(String playerCheck) {
		String shipDetails = "";
		if (playerCheck.equals("player")) {
			shipDetails = "playerShips ->";
			for (Rectangle details : dragAndDropShips.keySet()) {
				shipDetails += "(" + dragAndDropShips.get(details) + "),";
			}
		} else {
			shipDetails = "Opponent ->";
			for (int details : opponetShipDetails.keySet()) {
				shipDetails += "(" + opponetShipDetails.get(details) + "),";
			}

		}
		
		return shipDetails;
	}
	/**
	*<p>
	 * This method takes the details of all cells in the board and converts them in to string.
	 *</p>
	 * @param boardDetails - player board and opponent board
	 * @return - string containing board details
	 
	 */
	public String getBoardInformation(Board boardDetails) {
		
		String finalDetails = "";
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				Cell temp = boardDetails.getCell(i, j);
				if (temp.targetHit)
					finalDetails += "(" + i + "-" + j + "-" + "hit),";
				else if (temp.ship != null)
					finalDetails += "(" + i + "-" + j + "-" + "ship),";
				else if (temp.getFill() == Color.BLACK)
					finalDetails += "(" + i + "-" + j + "-" + "miss),";
				else
					finalDetails += "(" + i + "-" + j + "-" + "normal),";

			}
		}

		return finalDetails;
	}
	
	/**
	*<p>
	 * This method opens file in which data is to be written.
	 *</p>
	 * @param file - file to be opened.
	 */
	private void openFile(File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			loadCheck = true;
			while ((line = reader.readLine()) != null) {
				
				loading(line);

			}
			loadCheck = true;
			ship1.setDisable(true);
			ship2.setDisable(true);
			ship3.setDisable(true);
			ship4.setDisable(true);
			ship5.setDisable(true);
			st.setText("Resume");
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
	
	/**
	*<p>
	 * This method loads the board details. 
	 *</p>
	 * @param line - string containing board information
	 */
	private void loading(String line) {
		
		String tme[] = line.split("@");
		if (tme[0].trim().equals("Player_1")) {
			tme = tme[1].split(";");
			for (String playDetails : tme) {
				String remainDetails[] = playDetails.split("->");
				if (remainDetails[0].trim().equals("Timer")) {
					timer1.setText(remainDetails[1].trim());
					String disintegrate[] = remainDetails[1].trim().split(":");
					millis = Integer.parseInt(disintegrate[2].trim());
					secs = Integer.parseInt(disintegrate[1].trim());
					mins = Integer.parseInt(disintegrate[0].trim());
				} else if (remainDetails[0].trim().equals("Score")) {
					player1Score = Integer.parseInt(remainDetails[1].trim());
					displayScore("player1");
				} else if (remainDetails[0].trim().equals("playerShips")) {
					String shipReceived[] = remainDetails[1].trim().split(",");
					for (String shipDetailsR : shipReceived) {
						shipDetailsR = shipDetailsR.substring(1, shipDetailsR.length() - 1);
						String shipDetailsRec[] = shipDetailsR.trim().split("-");
						callPostionShip("Player_1", Integer.parseInt(shipDetailsRec[0].trim()),
								Integer.parseInt(shipDetailsRec[1].trim()),
								Boolean.parseBoolean(shipDetailsRec[2].trim()),
								Integer.parseInt(shipDetailsRec[3].trim()));
					}
				} else if (remainDetails[0].trim().equals("Board")) {
					String shipReceived[] = remainDetails[1].trim().split(",");
					for (String shipDetailsR : shipReceived) {
						shipDetailsR = shipDetailsR.substring(1, shipDetailsR.length() - 1);
						String shipDetailsRec[] = shipDetailsR.trim().split("-");
						if (shipDetailsRec[2].trim().equals("miss")) {
							firstPlayerBoard.getCell(Integer.parseInt(shipDetailsRec[0].trim()),
									Integer.parseInt(shipDetailsRec[1].trim())).targetHit = false;
							firstPlayerBoard.getCell(Integer.parseInt(shipDetailsRec[0].trim()),
									Integer.parseInt(shipDetailsRec[1].trim())).setFill(Color.BLACK);
						} else if (shipDetailsRec[2].trim().equals("hit")) {
							firstPlayerBoard.getCell(Integer.parseInt(shipDetailsRec[0].trim()),
									Integer.parseInt(shipDetailsRec[1].trim())).shoot();

						}
					}
				}
			}
		} else {

			tme = tme[1].split(";");
			for (String playDetails : tme) {
				String remainDetails[] = playDetails.split("->");
				if (remainDetails[0].trim().equals("Timer")) {
					timer2.setText(remainDetails[1].trim());
					String disintegrate[] = remainDetails[1].trim().split(":");
					millis1 = Integer.parseInt(disintegrate[2].trim());
					secs1 = Integer.parseInt(disintegrate[1].trim());
					mins1 = Integer.parseInt(disintegrate[0].trim());
				} else if (remainDetails[0].trim().equals("Score")) {
					player2Score = Integer.parseInt(remainDetails[1].trim());
					displayScore("Opponent");
				} else if (remainDetails[0].trim().equals("Opponent")) {
					String shipReceived[] = remainDetails[1].trim().split(",");
					for (String shipDetailsR : shipReceived) {
						shipDetailsR = shipDetailsR.substring(1, shipDetailsR.length() - 1);
						String shipDetailsRec[] = shipDetailsR.trim().split("-");
						callPostionShip("Opponent", Integer.parseInt(shipDetailsRec[0].trim()),
								Integer.parseInt(shipDetailsRec[1].trim()),
								Boolean.parseBoolean(shipDetailsRec[2].trim()),
								Integer.parseInt(shipDetailsRec[3].trim()));
					}
				} else if (remainDetails[0].trim().equals("Board")) {
					String shipReceived[] = remainDetails[1].trim().split(",");
					for (String shipDetailsR : shipReceived) {
						shipDetailsR = shipDetailsR.substring(1, shipDetailsR.length() - 1);
						String shipDetailsRec[] = shipDetailsR.trim().split("-");
						if (shipDetailsRec[2].trim().equals("miss")) {
							opponentBoard.getCell(Integer.parseInt(shipDetailsRec[0].trim()),
									Integer.parseInt(shipDetailsRec[1].trim())).targetHit = false;
							opponentBoard.getCell(Integer.parseInt(shipDetailsRec[0].trim()),
									Integer.parseInt(shipDetailsRec[1].trim())).setFill(Color.BLACK);
						} else if (shipDetailsRec[2].trim().equals("hit")) {
							opponentBoard.getCell(Integer.parseInt(shipDetailsRec[0].trim()),
									Integer.parseInt(shipDetailsRec[1].trim())).shoot();

						}
					}
				}
			}

		}
	}

	/**
	 * This method calls the positionShip method.
	 * @param name - player 1 and player 2
	 * @param xCor - x coordinate of the ship
	 * @param yCor - y coordinate of the ship
	 * @param orient -  horizontal or vertical
	 *@param len - length of the ship
	 */
	private void callPostionShip(String name, int xCor, int yCor, boolean orient, int len) {
		if (name.trim().equals("Player_1")) {
			firstPlayerBoard.positionShip(new Ship(len, orient), xCor, yCor, false);
			numberOfShips--;
		} else {
			opponentBoard.positionShip(new Ship(len, orient), xCor, yCor, false);

		}

	}
	
	/**
	 * This method paints the ship rectangles.
	 * @param ships - selected ship
	 * @param imagePath - path of the image
	 */
	public void paintShip(ArrayList<Rectangle> ships, String imagePath) {
		File shipImg = new File(".");

		Image ShipImage = null;
		try {
			ShipImage = new Image("file:///" + shipImg.getCanonicalFile() + imagePath);

			/**
			 * @throws io exception
			 *             
			 */
		} catch (IOException e) {

			e.printStackTrace();
		}
		for (Rectangle rectangle : ships) {
			rectangle.setFill(new ImagePattern(ShipImage));
		}

	}

	/**
	 * Providing suggestions to the user after some time if the user does not
	 * perform any event temporaryCell gets the cell according to the coordinate and
	 * filled with blue color
	 * 
	 */
	public void callSuggestionMethod() {
		int count = 1;
		for (Rectangle rect : dragAndDropShipsOpponent.keySet()) {
			if (count > 5)
				break;
			String takeCordinates[] = dragAndDropShipsOpponent.get(rect).split("-");
			Cell temporaryCell = opponentBoard.getCell(Integer.parseInt(takeCordinates[0]),
					Integer.parseInt(takeCordinates[1]));
			temporaryCell.setFill(Color.BLUE);
			count++;

		}
		checkForSugg = true;

	}

	/**
	 * 
	 * This is the timer Class which will help us to determine the difference
	 * between the player events and calling necessary events
	 * <p>
	 * Other tasks are like:
	 * </p>
	 * <ol>
	 * <li>Handles the thread and calls suggestion method.</li>
	 * <li>Determines the time</li>
	 * </ol>
	 * 
	 * @author K3
	 *
	 */
	class checkTimer implements Runnable {
		private volatile boolean exit = false;
		private int prevTime = 0;
		private int curTime = 0;

		public void run() {
			exit = false;
			System.out.println(exit);
			prevTime = Integer.parseInt(timer1.getText().split(":")[0]) * 60
					+ Integer.parseInt(timer1.getText().split(":")[1]);
			while (!exit) {

				curTime = Integer.parseInt(timer1.getText().split(":")[0]) * 60
						+ Integer.parseInt(timer1.getText().split(":")[1]);
				
				if (curTime - prevTime > 5) {
					System.out.println("After 5 and check");
					exit = true;
				}
				if (!checkTimeForSug) {
					System.out.println("On Click");
					exit = true;
				}
			}
			if (exit && checkTimeForSug) {
				callSuggestionMethod();
			}
		}

		public void stop() {
			exit = true;
		}
	}

	/**
	 * This Method Will call the initialize method to set up the Stage.
	 * 
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		intialise(primaryStage);

	}

	/**
	 * Launches the application
	 * 
	 * @param args-takes
	 *            the default Arguments
	 * 
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
