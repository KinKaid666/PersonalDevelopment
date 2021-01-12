TARGETS = fog lightmaterial lightposition projection shapes texture \
	  transformation
OBJS	= glm.o sgi.o
CC	= gcc
CFLAGS	= -g
INCLUDE = -I/usr/local/glut/include -I/usr/openwin/include \
	  -I/usr/local/X11/include
LDLIBS  = -lglut -lGLU -lGLw -lGL -lGLw -lXmu -lXext -lX11 -lm
LDPATH  = -L/usr/X11R6/lib

all:	$(TARGETS)

fog:	fog.o glm.o
	$(CC) $(CFLAGS) $(INCLUDE) -o $@ fog.o $(OBJS) $(LDPATH) $(LDLIBS)

lightmaterial:	lightmaterial.o glm.o
	$(CC) $(CFLAGS) $(INCLUDE) -o $@ lightmaterial.o \
	      $(OBJS) $(LDPATH) $(LDLIBS)

lightposition:	lightposition.o glm.o
	$(CC) $(CFLAGS) $(INCLUDE) -o $@ lightposition.o \
	      $(OBJS) $(LDPATH) $(LDLIBS)

projection:	projection.o glm.o sgi.o
	$(CC) $(CFLAGS) $(INCLUDE) -o $@ projection.o \
	      $(OBJS) $(LDPATH) $(LDLIBS)

shapes:	shapes.o glm.o sgi.o
	$(CC) $(CFLAGS) $(INCLUDE) -o $@ shapes.o $(OBJS) $(LDPATH) $(LDLIBS)

texture:	texture.o glm.o
	$(CC) $(CFLAGS) $(INCLUDE) -o $@ texture.o $(OBJS) $(LDPATH) $(LDLIBS)

transformation:	transformation.o glm.o
	$(CC) $(CFLAGS) $(INCLUDE) -o $@ transformation.o \
	      $(OBJS) $(LDPATH) $(LDLIBS)

.c.o:
	$(CC) $(CFLAGS) $(INCLUDE) -c $<
clean:
	-rm $(TARGET)
