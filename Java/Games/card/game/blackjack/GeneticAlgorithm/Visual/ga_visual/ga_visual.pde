import card.* ;
import card.game.blackjack.* ;
import card.game.blackjack.GeneticAlgorithm.* ;

import java.util.* ;
import java.util.stream.* ;
import java.io.* ;
import java.text.DecimalFormat ;
import java.math.RoundingMode ;

BlackjackStrategy bookStrategy ;
BlackjackStrategy bestStrategy ;
BlackjackRules    rules ;
List<BlackjackEvolutionObject> strategies ;
List<Double> strategiesFitnessValues ;
String bookStrategyFilename = String.valueOf("/Users/ericferguson/Development/Java/Games/card/game/blackjack/Strategies/perfect.stg") ;
Random r = new Random(System.currentTimeMillis()) ;
int numStrategies   = 1_000 ;
int numHands        = 10_000 ;
int currentGenerationProgress = 0 ;
double mutationRate = 0.001 ;

// Continuation
String bestStrategyFilename = "/Users/ericferguson/Development/Java/Games/card/game/blackjack/GeneticAlgorithm/Visual/ga_visual/data/strategy-1000s-10000h-0.001m-535i.stg" ;
int generation = 535 ;

int textSize = 11 ;
int cellSize = 13 ;

int stopButtonX, stopButtonY ;
int startButtonX, startButtonY ;
int buttonWidth = 90, buttonHeight = cellSize ;
color buttonColor, highlightColor ;
boolean overStopButton = false;
boolean overStartButton = false;
boolean gaOn = false ;
boolean simulating = false ;

void setup() {
  try {
    bookStrategy = BlackjackStrategyStatic.createStrategyFromFile(bookStrategyFilename) ;
    //bestStrategy = BlackjackStrategy.createRandom() ;
    bestStrategy = BlackjackStrategyStatic.createStrategyFromFile(bestStrategyFilename) ;
    rules = new BlackjackRules(6, // Decks
      true, // split Aces
      true, // dealer hit soft 17
      true, // allow surrender
      true, // allow late surrender
      true, // replit pairs
      false, // resplit aces
      true, // double after split
      false, // double on only 10 or 11
      true   // insurance
      ) ;
    strategies = new ArrayList<>() ;
    strategiesFitnessValues = new LinkedList<>() ;

    for ( int i = 0; i < numStrategies; i++ ) {
      strategies.add(new BlackjackEvolutionObject(bestStrategy)) ;
    }
  }
  catch (Exception e) {
    System.err.println("caught exception: " + e.getMessage()) ;
    System.exit(1) ;
  }
  size(550, 873) ;
  displayStrategy("Book", bookStrategy, cellSize, cellSize/2) ;
  displayStrategy("Random", bestStrategy, (cellSize * 14), cellSize/2) ;

  textSize(textSize) ;
  fill(0, 0, 0) ;
  textAlign(LEFT, CENTER);
  int keyX = cellSize * 27 ;
  int keyY = cellSize ;

  text("Population = " + numStrategies, keyX, keyY) ;
  keyY += cellSize;
  text("Hands / sim = " + numHands, keyX, keyY) ;
  keyY += cellSize;
  text(String.format("Mutation rate = %.3f", mutationRate), keyX, keyY) ;
  keyY += cellSize;
  text("H – Hit", keyX, keyY) ;
  keyY += textSize ;
  text("P – Split", keyX, keyY) ;
  keyY += textSize ;
  text("S – Stand", keyX, keyY) ;
  keyY += textSize ;
  text("D – Double (if you can).", keyX, keyY) ;
  keyY += textSize ;
  text("R – Surrender (if you can).", keyX, keyY) ;
  keyY += textSize ;
  text("X – No Surrender.", keyX, keyY) ;
  keyY += textSize ;
  
  buttonColor = color(255) ;
  highlightColor = color(204) ;
  startButtonX = cellSize * 27 ;
  startButtonY = 200 ;
  stopButtonX = startButtonX + buttonWidth + cellSize ;
  stopButtonY = 200 ;
}

void draw() {
  updateButtons() ;

  if (overStartButton) {
    fill(highlightColor);
  } else {
    fill(buttonColor);
  }
  stroke(255);
  rect(startButtonX, startButtonY, buttonWidth, buttonHeight) ;
  fill(0) ;
  textAlign(CENTER, CENTER) ;
  text("Start ▶", startButtonX + (buttonWidth/2), startButtonY + (buttonHeight/2)) ;

  if (overStopButton) {
    fill(highlightColor);
  } else {
    fill(buttonColor);
  }
  stroke(255);
  rect(stopButtonX, stopButtonY, buttonWidth, buttonHeight) ;
  fill(0) ;
  text("Stop ⏹", stopButtonX + (buttonWidth/2), stopButtonY + (buttonHeight/2)) ;

  fill(255);
  rect(startButtonX, stopButtonY - buttonHeight*2, buttonWidth*2 + cellSize, buttonHeight*1.2) ;
  fill(0) ;
  textAlign(LEFT, TOP) ;
  String status = gaOn ? "Running" : (simulating ? "Finishing run" : "Not Running") ;
  if (simulating) {
    status += " " + (currentGenerationProgress*100.0f / numStrategies*1.0f) + " %" ;
  }
  text(status, startButtonX, stopButtonY - buttonHeight*2) ;

  if (gaOn) {
    if (!simulating) {
      thread("evolveOneGeneration") ;
    }
  }
  displayStrategy("Best after " + generation + " generations", bestStrategy, (cellSize * 14), cellSize/2) ;

  // Given the way processing works, the
  try {
    List<Double> values = List.copyOf(strategiesFitnessValues) ;
    drawGraph(List.copyOf(strategiesFitnessValues),
      startButtonX,
      startButtonY + buttonHeight*2,
      buttonWidth*2 + cellSize,
      buttonWidth*2 + cellSize) ;
  }
  catch (Exception e) {
    System.err.println("Caught exception while copying list: " + e.getMessage()) ;
  }
}

void drawGraph(List<Double> values, int x, int y, int width, int height) {
  //System.out.printf("Calling drawGraph(values: %d, x: %d, y: %d, width: %d, height: %d)\n", values.size(), x, y, width, height) ;
  //System.out.println("Values: " + values) ;

  // Get us a box
  fill(255) ;
  rect(x, y, width, height) ;
  fill(0) ;
  text("Fitness Function", x + width/2, y + 10) ;

  int originOffset = 20 ;
  pushMatrix() ;

  // Move us to the 0,0 in the box and rotate the world so x, y is as expected
  translate(x + originOffset, y + height - originOffset) ;

  // rotate 270°
  // The world is 270° rotated so X = Y && Y = X
  rotate(PI * 3/2) ;

  int graphWidth = width - originOffset*2 ;
  int graphHeight = height - originOffset*2 ;

  // draw X, Y axis
  line(0, 0, graphWidth, 0) ;
  line(0, 0, 0, graphHeight) ;

  if (values.size() == 0) {
    popMatrix() ;
    return ;
  }

  // x will go from 0 -> min(5, # values)
  // y will go from 0 -> max(values)
  // create some simple hashing that's logical to humans
  int currentX = 0 ;
  float prevX = -1, prevY = -1 ;
  double maxY = values.stream()
    .mapToDouble(d->d)
    .max()
    .orElse(0.0d) ;

  int numberOfXHashMarks = 5 ;
  int maxX = Math.max(values.size(), numberOfXHashMarks) ;
  int xStep = (values.size() < numberOfXHashMarks ?
    1 : values.size() / numberOfXHashMarks) ;
  int numberOfYHashMarks = 5 ;
  DecimalFormat df = new DecimalFormat("#.##");
  df.setRoundingMode(RoundingMode.HALF_UP);
  Double yStep = Double.valueOf(df.format(maxY / numberOfYHashMarks)) ;

  textSize(8) ;
  fill(0) ;
  // X Hashes
  for (int i = 0; i < maxX; i += xStep) {
    float valx = map((float)i, 0f, (float)maxX, 0f, (float)graphWidth) ;
    if (valx > 0) {
      line(5, valx, -5, valx) ;
    }
    text(String.format("%d", i), -12, valx) ;
  }
  // Y Hashes
  for (Double i = 0d; i < maxY; i += yStep) {
    float valy = map(i.floatValue(), 0f, (float)maxY, 0f, (float)graphHeight) ;
    if (valy > 0) {
      line(valy, 5, valy, -5) ;
    }
    text(String.format("%.2f", i), valy, -12) ;
  }
  textSize(textSize) ;

  for (Double d : values) {
    // translate (map) the values to the graph space
    float valx = map((float)currentX, 0f, (float)maxX, 0f, (float)graphWidth) ;
    float valy = map( d.floatValue(), 0f, (float)maxY, 0f, (float)graphHeight) ;

    stroke(color(231, 76, 60)) ;
    circle(valy, valx, 1) ;
    if (prevX >= 0 && prevY >= 0) {
      line(prevY, prevX, valy, valx) ;
    }
    prevX = valx ;
    prevY = valy ;
    currentX += 1 ;
  }
  popMatrix() ;
}

void evolveOneGeneration() {
  simulating = true ;
  // foreach strategies, play blackjack N number and get it's fitness
  for (currentGenerationProgress = 0; currentGenerationProgress < numStrategies; currentGenerationProgress++) {
    BlackjackSimulator sim = new BlackjackSimulator(strategies.get(currentGenerationProgress).getStrategy(), numHands) ;
    sim.simulate() ;
    double fitness = sim.getFitness() ;
    strategies.get(currentGenerationProgress).setFitness(fitness) ;
  }

  Collections.sort(strategies) ;
  bestStrategy = strategies.get(strategies.size() - 1).getStrategy() ;
  strategiesFitnessValues.add(bestStrategy.getFitness()) ;

  // Create a mating pool
  List<BlackjackEvolutionObject> matingPool = null ;
  if ( true ) {
    // Try top 50
    int n = 50 ;
    matingPool = strategies.stream()
      .skip(numStrategies - n)
      .collect(Collectors.toList()) ;
  }
  // Rescale the fitness the array such that the worst strategy gets a 0
  Double minFitness = matingPool.stream()
    .map(v -> v.getFitness())
    .mapToDouble(d->d)
    .min()
    .orElse(0.0d) ;
  for (int j = 0; j < matingPool.size(); j++) {
    matingPool.get(j).setNormalizedFitness(
      matingPool.get(j).getFitness() - minFitness) ;
  }

  Double totalFitness = matingPool.stream()
    .map(v -> v.getNormalizedFitness())
    .mapToDouble(d->d)
    .sum() ;

  for (int j = 0; j < matingPool.size(); j++) {
    matingPool.get(j).setNormalizedFitness(
      matingPool.get(j).getNormalizedFitness()/totalFitness) ;
  }


  // generate new strategy population
  ArrayList<BlackjackEvolutionObject> newStrategies = new ArrayList<BlackjackEvolutionObject>() ;
  for ( int j = 0; j < numStrategies; j++ ) {
    BlackjackStrategy a = BlackjackEvolutionObject.pickStrategy(matingPool, r.nextDouble()) ;
    BlackjackStrategy b = BlackjackEvolutionObject.pickStrategy(matingPool, r.nextDouble()) ;

    // crossover creates new strategy object that zeros it's statistics
    BlackjackStrategy c = a.crossover(b) ;
    // random modify the configuration based on the mutation rate
    c.mutate(mutationRate) ;

    // Add it to the new mating pool
    newStrategies.add(new BlackjackEvolutionObject(c)) ;
  }
  strategies = newStrategies ;
  simulating = false ;
  System.out.printf("After %d generations, the max fitness %.3f\n", ++generation, bestStrategy.getFitness()) ;
}

void displayStrategy(String header, BlackjackStrategy strategy, int x, int y) {
  stroke(0);
  // Header
  textSize(textSize) ;
  textAlign(CENTER, CENTER);
  fill(200) ;
  quad(x, y, x + (cellSize*12), y, x + (cellSize*12), y + cellSize, x, y + cellSize) ;
  fill(0) ;
  text(header, x + (cellSize*11)/2, y + cellSize/2) ;
  y += cellSize ;

  // Setup the cells
  fill(255) ;

  HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> hardTotalStrategies = strategy.getHardTotalStrategies() ;
  displayStrategies("Player Hard Totals", hardTotalStrategies, x, y) ;
  y += hardTotalStrategies.keySet().size() * cellSize ;
  y += 2.5 * cellSize ;

  HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> softTotalStrategies = strategy.getSoftTotalStrategies() ;
  displayStrategies("Player Soft Totals", softTotalStrategies, x, y) ;
  y += softTotalStrategies.keySet().size() * cellSize ;
  y += 2.5 * cellSize ;

  HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> pairStrategies = strategy.getPairStrategies() ;
  displayStrategies("Player Pairs", pairStrategies, x, y) ;
  y += pairStrategies.keySet().size() * cellSize ;
  y += 2.5 * cellSize ;

  HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> surrenderStrategies = strategy.getSurrenderStrategies() ;
  displayStrategies("Player Surrender", surrenderStrategies, x, y) ;
  y += pairStrategies.keySet().size() * cellSize ;
}

void displayStrategies(String header,
  HashMap<Integer,
  HashMap<Card,
  BlackjackStrategy.StrategicMove>> strategies,
  int x,
  int y) {
  List<Integer> playerHands = new ArrayList<>(strategies.keySet()) ;
  List<Card> dealerCards = new ArrayList<>(strategies.get(playerHands.get(0)).keySet()) ;
  Collections.sort(dealerCards) ;
  Collections.sort(playerHands) ;

  // For player hand of '1', make it after 10
  // We'll make it an "A" later
  if (playerHands.get(0).equals(1)) {
    playerHands.remove(0) ;
    playerHands.add(1) ;
  }

  // Column Header
  fill(255) ;
  quad(x, y, x + cellSize, y, x + cellSize, y + (cellSize * (playerHands.size() + 2)), x, y + (cellSize * (playerHands.size()+2))) ;

  pushMatrix();
  translate(x, y) ;
  fill(0) ;
  rotate(radians(270)) ;
  textAlign(CENTER, TOP);
  text(header, - (cellSize * (playerHands.size() + 2))/2, -1) ;
  popMatrix();

  x += cellSize ;
  fill(255) ;

  textAlign(CENTER, CENTER);
  // upper left empty cell for asthetics
  quad(x, y, x + cellSize, y, x + cellSize, y + (cellSize*2), x, y + (cellSize*2)) ;

  // Row Header
  quad(x + cellSize, y, x + (cellSize*(dealerCards.size()+1)), y, x + (cellSize*(dealerCards.size()+1)), y + cellSize, x + cellSize, y + cellSize) ;
  fill(0) ;
  text("Dealers Card", x + (cellSize*(dealerCards.size()+1))/2, y + cellSize/2) ;

  // Store the X before using it
  int storeX = x ;
  x += cellSize ;
  y += cellSize ;
  int storeY = y ;

  for (Card c : dealerCards) {
    fill(255) ;
    quad(x, y, x + cellSize, y, x + cellSize, y + cellSize, x, y + cellSize) ;
    fill(0) ;
    text(c.getRankString(), x + cellSize/2, y + cellSize/2) ;
    x += cellSize ;
  }

  // reset it, and push it down one row
  x = storeX ;
  y = storeY + cellSize ;

  storeX = x ;
  storeY = y ;

  for (Integer playerHand : playerHands) {
    fill(255) ;
    quad(x, y, x + cellSize, y, x + cellSize, y + cellSize, x, y + cellSize) ;
    fill(0) ;
    text(String.valueOf(playerHand.equals(1) ? "A" : playerHand), x + cellSize/2, y + cellSize/2) ;
    x += cellSize ;

    for (Card c : dealerCards) {
      try {
        BlackjackStrategy.StrategicMove move = strategies.get(playerHand).get(c) ;
        setFillForMove(move) ;
        quad(x, y, x + cellSize, y, x + cellSize, y + cellSize, x, y + cellSize) ;
        fill(0) ;
        text(createMoveKey(move), x + cellSize/2, y + cellSize/2) ;
        x += cellSize ;
      }
      catch(Exception e) {
        System.out.println("Caught exception: " + e.getMessage()) ;
      }
    }
    x = storeX ;
    y += cellSize ;
  }
}

// H – Hit
// P – Split
// S – Stand
// Ds – Double if you can, otherwise stand.
// Dh – Double if you can, otherwise hit.
// Ph – Split if its allowed, otherwise hit.
// Pd – Split if its allowed; if not, double.
// Rh – Surrender if you can; if not, hit.
// Rs – Surrender if you can; if not, stand.
// Rp – Surrender if you can; if not, split.
String createMoveKey(BlackjackStrategy.StrategicMove m) {
  String key = "" ;
  switch(m) {
  case Hit:
    key = "H" ;
    break ;
  case Stand:
    key = "S" ;
    break ;
  case DoubleElseHit:
    key = "D" ;
    break ;
  case DoubleElseStand:
    key = "D" ;
    break ;
  case Split:
    key = "P" ;
    break ;
  case DontSplit:
    key = "S" ;
    break ;
  case SplitIfDouble:
    key = "P" ;
    break ;
  case Surrender:
    key = "R" ;
    break ;
  case NoSurrender:
    key = "X" ;
    break ;
  }
  return key ;
}

void setFillForMove(BlackjackStrategy.StrategicMove m) {
  color green  = color(39, 174, 96) ;
  color red    = color(231, 76, 60) ;
  color blue   = color(52, 152, 219) ;

  switch(m) {
  case Hit:
    // Green
    fill(green) ;
    break ;
  case Stand:
    // Red
    fill(red) ;
    break ;
  case DoubleElseHit:
    // Blue
    fill(blue) ;
    break ;
  case DoubleElseStand:
    // Red
    fill(blue) ;
    break ;
  case Split:
    // Green
    fill(green) ;
    break ;
  case DontSplit:
    // Red
    fill(red) ;
    break ;
  case SplitIfDouble:
    // Red
    fill(green) ;
    break ;
  case Surrender:
    // Red
    fill(red) ;
    break ;
  case NoSurrender:
    // Green
    fill(green) ;
    break ;
  }
}

boolean overButton(int x, int y, int width, int height) {
  if (mouseX >= x && mouseX <= x+width &&
    mouseY >= y && mouseY <= y+height) {
    return true;
  } else {
    return false;
  }
}

void updateButtons() {
  if (overButton(startButtonX, startButtonY, buttonWidth, buttonHeight)) {
    overStartButton = true ;
  } else {
    overStartButton = false ;
  }

  if (overButton(stopButtonX, stopButtonY, buttonWidth, buttonHeight)) {
    overStopButton = true ;
  } else {
    overStopButton = false ;
  }
}

// Input
void mousePressed() {
  if (overStopButton) {
    gaOn = false ;
  }
  if (overStartButton) {
    gaOn = true ;
  }
}

void keyPressed() {
  if (key == 'S' || key == 's') {
    String saveInfo = String.format("%ds-%dh-%.3fm-%di",
      numStrategies,
      numHands,
      mutationRate,
      generation) ;

    try {
      // Creates a FileOutputStream
      FileOutputStream file = new FileOutputStream(dataPath("") + "/strategy-" + saveInfo + ".stg");

      // Creates a PrintStream
      PrintStream output = new PrintStream(file, true);

      // Save strategy file
      bestStrategy.printStrategy(output) ;

      // Save image
      save(dataPath("") + "/image-" + saveInfo + ".jpg");
    }
    catch(Exception e) {
      System.out.println("Failed to save: " + e.getMessage()) ;
    }
    System.out.println("Strategy saved!") ;
  }
}
