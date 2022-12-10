import card.* ;
import card.game.blackjack.* ;
import card.game.blackjack.GeneticAlgorithm.* ;

import java.util.* ;
import java.util.stream.* ;

BlackjackStrategy bookStrategy ;
BlackjackStrategy bestStrategy ;
BlackjackRules    rules ;
List<BlackjackEvolutionObject> strategies ;
String bookStrategyFilename = String.valueOf("/Users/ericferguson/Development/Java/Games/card/game/blackjack/Strategies/perfect.stg") ;
Random r = new Random(System.currentTimeMillis()) ;
int numStrategies   = 1_000 ;
int numGames        = 100_000 ;
int generation      = 1 ;
double mutationRate = 0.001 ;

int textSize = 12 ;
int cellSize = 15 ;

void setup() {
  try {
    bookStrategy = BlackjackStrategyStatic.createStrategyFromFile(bookStrategyFilename) ;
    bestStrategy = BlackjackStrategy.createRandom() ;
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
    strategies = new ArrayList<BlackjackEvolutionObject>() ;
    for ( int i = 0; i < numStrategies; i++ ) {
      strategies.add(new BlackjackEvolutionObject(BlackjackStrategy.createRandom())) ;
    }
  }
  catch (Exception e) {
    System.err.println("caught exception: " + e.getMessage()) ;
    System.exit(1) ;
  }
  size(630, 900) ;
  displayStrategy("Book", bookStrategy, cellSize, cellSize) ;
  displayStrategy("Best after 0 generations", bestStrategy, (cellSize * 14), cellSize) ;

  textSize(textSize) ;
  fill(0, 0, 0) ;
  textAlign(LEFT, CENTER);
  int keyX = cellSize * 27 ;
  int keyY = cellSize + cellSize;
  text("H – Hit", keyX, keyY) ;
  keyY += textSize ;
  text("P – Split", keyX, keyY) ;
  keyY += textSize ;
  text("S – Stand", keyX, keyY) ;
  keyY += textSize ;
  text("Ds – Double if you can, otherwise stand.", keyX, keyY) ;
  keyY += textSize ;
  text("Dh – Double if you can, otherwise hit.", keyX, keyY) ;
  keyY += textSize ;
  text("Ph – Split if its allowed, otherwise hit.", keyX, keyY) ;
  keyY += textSize ;
  text("Pd – Split if its allowed; if not, double.", keyX, keyY) ;
  keyY += textSize ;
  text("Rh – Surrender if you can; if not, hit.", keyX, keyY) ;
  keyY += textSize ;
  text("Rs – Surrender if you can; if not, stand.", keyX, keyY) ;
  keyY += textSize ;
  text("Rp – Surrender if you can; if not, split.", keyX, keyY) ;
  noLoop() ;
}

void draw() {
  displayStrategy("Best after " + generation + " generations", bestStrategy, (cellSize * 14), cellSize) ;
  bestStrategy = evolveOneGeneration() ;
  System.out.printf("After %d generations, the max fitness %.3f\n", generation++, bestStrategy.getFitness()) ;
}

BlackjackStrategy evolveOneGeneration() {
  // foreach strategies, play blackjack N number and get it's fitness
  for ( int j = 0; j < numStrategies; j++ ) {
    BlackjackSimulator sim = new BlackjackSimulator(strategies.get(j).getStrategy(), numGames) ;
    sim.simulate() ;
    double fitness = sim.getFitness() ;
    strategies.get(j).setFitness(fitness) ;
  }

  Collections.sort(strategies) ;
  bestStrategy = strategies.get(strategies.size() - 1).getStrategy() ;

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
  return bestStrategy ;
}

void displayStrategy(String header, BlackjackStrategy strategy, int x, int y) {
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
  displayStrategies("Hard Totals", hardTotalStrategies, x, y) ;
  y += hardTotalStrategies.keySet().size() * cellSize ;
  y += 3 * cellSize ;

  HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> softTotalStrategies = strategy.getSoftTotalStrategies() ;
  displayStrategies("Soft Totals", softTotalStrategies, x, y) ;
  y += softTotalStrategies.keySet().size() * cellSize ;
  y += 3 * cellSize ;

  HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> pairStrategies = strategy.getPairStrategies() ;
  displayStrategies("Pairs", pairStrategies, x, y) ;
  y += pairStrategies.keySet().size() * cellSize ;
  y += 3 * cellSize ;

  HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> surrenderStrategies = strategy.getSurrenderStrategies() ;
  displayStrategies("Surrender", surrenderStrategies, x, y) ;
  y += pairStrategies.keySet().size() * cellSize ;
}

void displayStrategies(String header, HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> strategies, int x, int y) {
  List<Integer> playerHands = new ArrayList<>(strategies.keySet()) ;
  List<Card> dealerCards = new ArrayList<>(strategies.get(playerHands.get(0)).keySet()) ;
  Collections.sort(dealerCards) ;
  Collections.sort(playerHands) ;

  // Column Header
  quad(x, y, x + cellSize, y, x + cellSize, y + (cellSize * (playerHands.size() + 2)), x, y + (cellSize * (playerHands.size()+2))) ;

  pushMatrix();
  translate(x, y) ;
  fill(0) ;
  rotate(radians(270)) ;
  textAlign(CENTER, TOP);
  text(header, - (cellSize * (playerHands.size() + 2))/2, 0) ;
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
    text(String.valueOf(playerHand), x + cellSize/2, y + cellSize/2) ;
    x += cellSize ;

    for (Card c : dealerCards) {
      try {
        BlackjackStrategy.StrategicMove move = strategies.get(playerHand).get(c) ;
        setFillForMove(move) ;
        quad(x, y, x + cellSize, y, x + cellSize, y + cellSize, x, y + cellSize) ;
        fill(255) ;
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
    key = "Dh" ;
    break ;
  case DoubleElseStand:
    key = "Ds" ;
    break ;
  case Split:
    key = "P" ;
    break ;
  case DontSplit:
    key = "X" ;
    break ;
  case SplitIfDouble:
    key = "Pd" ;
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
  switch(m) {
  case Hit:
    fill(0, 255, 0) ;
    break ;
  case Stand:
    fill(255, 0, 0) ;
    break ;
  case DoubleElseHit:
    fill(0, 80, 250) ;
    break ;
  case DoubleElseStand:
    fill(255, 0, 0) ;
    break ;
  case Split:
    fill(0, 255, 0) ;
    break ;
  case DontSplit:
    fill(255, 0, 0) ;
    break ;
  case SplitIfDouble:
    fill(255, 0, 0) ;
    break ;
  case Surrender:
    fill(255, 0, 0) ;
    break ;
  case NoSurrender:
    fill(0, 255, 0) ;
    break ;
  }
}
