import java.util.* ;

static public final class LifeGame {
  static final Random random_ = new Random(System.currentTimeMillis()) ;
  
  private List<List<Boolean>> board_ ;

  public LifeGame(int rows, int cols) {
    initBoard(rows, cols) ;
  }
  
  public LifeGame(List<List<Boolean>> board) {
    board_ = board ;
  }

  public void initBoard(int rows, int cols) {
    board_ = new ArrayList<List<Boolean>>(rows) ;
    for(int i = 0 ; i < rows ; i++) {
      List<Boolean> row = new ArrayList<Boolean>(cols) ;
      for(int j = 0 ; j < cols ; j++) {
        row.add(random_.nextBoolean()) ;
      }
      board_.add(row) ;
    }
  }
  
  public void nextGeneration() {
    // we only really need to save 3 rows at a time
    List<List<Boolean>> newBoard = cloneBoard() ;
    
    for(int i = 0 ; i < board_.size() ; i++) {
      List<Boolean> newRow = newBoard.get(i) ;
      for(int j = 0 ; j < board_.get(i).size() ; j++) {
        newRow.set(j, LifeGame.computeTransition(board_,i,j)) ;
      }
      newBoard.set(i,newRow) ;
    }
    board_ = newBoard ;
  }
  
  public static Boolean computeTransition(List<List<Boolean>> board, int row, int column) {
    int aliveNeighborCount = 0 ;
    for(int i = -1 ; i <= 1 ; i++) {
      int curRow = row + i ;
      // bounds check
      if(curRow < 0 || curRow >= board.size()) {
        continue ;
      }
      
      for(int j = -1 ; j <= 1 ; j++) {
        int curCol = column + j ;
        // bounds check    
        if(curCol < 0 || curCol >= board.get(curRow).size()) {
          continue ;
        } 
        
        // don't check self
        if(i == 0 & j == 0) {
          continue ;
        }
        
        if(board.get(curRow).get(curCol)) {
          aliveNeighborCount += 1 ;
        }
      }
    }
    
    //Any live cell with two or three live neighbours survives.
    if(board.get(row).get(column) && (aliveNeighborCount == 2 || aliveNeighborCount == 3)) {
        return true ;
    } 
    
    //Any dead cell with three live neighbours becomes a live cell. 
    if(!board.get(row).get(column) && aliveNeighborCount == 3) {
      return true ;
    }
      
    //All other live cells die in the next generation. Similarly, all other dead cells stay dead.
    return false ;
  }
  
  public void consoleDump() {
    int iWidth = String.valueOf(board_.size()).length();
    int jWidth = board_.size() > 0 ? String.valueOf(board_.get(0).size()).length() : 0 ;
    for(int i = 0 ; i < board_.size() ; i++) {
      if(i == 0) {
        // leading space for the total length including [ and ]
        for(int x = 0 ; x < (jWidth+2) ; x++) {
          System.out.print(" ") ;
        }
        for(int j = 0 ; j < board_.get(i).size() ; j++) {
          System.out.printf("[%" + jWidth + "d]", j) ;
        }
        System.out.println() ;
      }

      System.out.printf("[%" + iWidth + "d]", i) ;
      for(int j = 0 ; j < board_.get(i).size() ; j++) {
        System.out.printf("[%" + jWidth + "s]", board_.get(i).get(j) ? "1" : "0") ;
      }
      System.out.println() ;
    }
  }
  
  public List<List<Boolean>> getBoard() { return board_ ; }
  public void setBoard(List<List<Boolean>> board) {  board_ = board ; }
  
  public List<List<Boolean>> cloneBoard() { 
    List<List<Boolean>> newBoard = new ArrayList<List<Boolean>>(board_.size()) ;
    for(int i = 0 ; i < board_.size() ; i++) {
      List<Boolean> row = new ArrayList<Boolean>(board_.get(i)) ;
      newBoard.add(row) ;
    }
    return newBoard ;
  }
} ;
