TARGET=		sendfile-tcp
CC=		gcc
CCFLAGS =	-g -library=iostream
CFLAGS =	-g
LIBFLAGS =	
RM=		rm

all:		$(TARGET)

clean:
	-$(RM) $(TARGET).o 

realclean:	clean
	-$(RM) $(TARGET)
	-$(RM) core

$(TARGET):	$(TARGET).o
	$(CC) $(CFLAGS) -o $@ $? $(LIBFLAGS)


# Suffix Rules
.SUFFIXES:
.SUFFIXES:      .c .o
.c.o:
	$(CC) $(CFLAGS) -c $<
