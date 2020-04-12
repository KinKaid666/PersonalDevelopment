TARGET=		proximity
OBJECTS=	GameBoard.o proximity.o GamePiece.o GameBoardPrinter.o GamePieceFactory.o GameStrategy.o
CC=		g++
CCFLAGS=	-g 
LINK=		g++
LINKFLAGS=	
LIBRARIES=	-lcurses
RM=		rm

# Targets
all:		$(TARGET)

clean:
	-$(RM) $(TARGET) $(OBJECTS)

realclean:	clean
	-$(RM) $(TARGET)
	-$(RM) $(TARGET).core

$(TARGET):	$(OBJECTS)
	$(LINK) $(LINKFLAGS) -o $@ $(OBJECTS) $(LIBRARIES)


# Suffix Rules
.SUFFIXES:
.SUFFIXES: 	.cpp .o
.cpp.o:
	$(CC) $(CCFLAGS) -c $<

#dependencies
GameBoard.o: GameBoard.cpp GameBoard.h
GameBoardPrinter.o: GameBoardPrinter.cpp GameBoardPrinter.h GameBoard.h
GamePiece.o: GamePiece.cpp GamePiece.h
GamePieceFactory.o: GamePieceFactory.cpp GamePieceFactory.h GamePiece.h
GameStrategy.o: GameStrategy.cpp GameStrategy.h GameBoard.h GamePiece.h
proximity.o: proximity.cpp GamePieceFactory.h GamePiece.h GameStrategy.h \
  GameBoard.h GameBoardPrinter.h
