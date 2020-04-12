TARGET=		polymorphism
CC=		g++
CCFLAGS=	-g 
LINK=		g++
LINKFLAGS=	
RM=		rm

# Dependencies
polymorphism:	polymorphism.o
polymorphism.o:	polymorphism.C shapes.h

# Targets
all:		$(TARGET)

clean:
	-$(RM) $(TARGET).o

realclean:	clean
	-$(RM) $(TARGET)
	-$(RM) $(TARGET).core

$(TARGET):	$(TARGET).o
	$(LINK) $(LINKFLAGS) -o $@ $?


# Suffix Rules
.SUFFIXES:
.SUFFIXES: 	.C .o
.C.o:
	$(CC) $(CCFLAGS) -c $<
