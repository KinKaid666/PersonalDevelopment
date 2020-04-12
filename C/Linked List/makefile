TARGET=		test
OBJECTS=	list.o test.o
CC=		gcc
CCFLAGS=	-g -ansi
LINK=		gcc
LINKFLAGS=	
RM=		rm

# Targets

all:	$(TARGET)

clean:
	-$(RM) $(TARGET) $(OBJECTS)

realclean:	clean
	-$(RM) $(TARGET)
	-$(RM) $(TARGET).core

$(TARGET):	$(OBJECTS)
	$(LINK) $(LINKFLAGS) -o $@ $(OBJECTS)


# Suffix Rules
.SUFFIXES:
.SUFFIXES: 	.c .o
.c.o:
	$(CC) $(CCFLAGS) -c $<

# Dependencies
list.o:	list.h
