include header.mak
TARGET=		sendfile-tcp-server
CC=		gcc
RM=		rm
CFLAGS=         -Wimplicit-function-declaration

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
