TARGET=		btree
OBJECTS=	btree.o binarytree.o
CC=		g++
CCFLAGS=	-g
LINK=		gcc
LINKFLAGS=	-lstdc++
RM=		rm

# Targets
all:		$(TARGET)

clean:
	-$(RM) $(OBJECTS)

realclean:	clean
	-$(RM) $(TARGET)
	-$(RM) $(TARGET).core

$(TARGET):	$(OBJECTS)
	$(LINK) $(LINKFLAGS) -o $@ $(OBJECTS)


# Suffix Rules
.SUFFIXES:
.SUFFIXES:	.c .o
.c.o:
	$(CC) $(CCFLAGS) -c $<
