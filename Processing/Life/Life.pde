// https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life

int CELL_SIZE = 10 ;

int ROWS = 100 ;
int COLS = 100 ;
double alivePct = .5 ;
LifeGame life ;
int generation = 0 ;

void setup() {
  size(1000,1000) ;
  frameRate(5);
  //stroke(1) ;
  noStroke() ;
  life = new LifeGame(ROWS, COLS, alivePct) ;
}

void draw() {
  List<List<Boolean>> board = life.getBoard() ;
  for(int i = 0 ; i < board.size() ; i++) {
    for(int j = 0 ; j < board.get(i).size() ; j++) {
      Boolean alive = board.get(i).get(j) ;
      if(alive) {
        fill(0) ;   // life is black
      } else {
        fill(255) ; // death is white
      }
      // first dimension is rows, which is i, which is y on the x/y
      square(j*CELL_SIZE, i*CELL_SIZE, CELL_SIZE);
    }
  }

  life.nextGeneration() ;
}
