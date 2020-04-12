TARGET	= lab2
CC	= gcc
CFLAGS	= -g
INCLUDE = -Wdeprecated-declarations
LDLIBS =  -lm -framework OpenGL -framework GLUT
LDFLAGS = 

$(TARGET):$(TARGET).c
	$(CC) $(INCLUDE) $(CFLAGS) $(TARGET).c $(LDFLAGS) $(LDLIBS) -o $(TARGET)

clean:
	-rm $(TARGET)
