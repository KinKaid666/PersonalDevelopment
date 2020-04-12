TARGET=		lsd
CC=		gcc
CCFLAGS=	-g -ansi
LINK=		gcc
LINKFLAGS=	
UNLINK=		rm
COPY=		cp

# Targets
all:		$(TARGET)

install:	
	make CCFLAGS= clean nodebuginstall
	$(COPY) $(TARGET) ~/bin

nodebuginstall:	$(TARGET)
	$(COPY) $(TARGET) ~/bin

clean:
	-$(UNLINK) $(TARGET).o

realclean:	clean
	-$(UNLINK) $(TARGET)
	-$(UNLINK) $(TARGET).core

$(TARGET):	$(TARGET).o
	$(LINK) $(LINKFLAGS) -o $@ $?


# Suffix Rules
.SUFFIXES:
.SUFFIXES: 	.c .o
.c.o:
	$(CC) $(CCFLAGS) -c $<
