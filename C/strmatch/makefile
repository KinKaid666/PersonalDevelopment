TARGET=		strmatch
CC=		gcc
CCFLAGS=	-g -ansi -DTIMES=1000000 -DWHICH=2
LINK=		gcc
LINKFLAGS=	
RM=		rm

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
.SUFFIXES: 	.c .o
.c.o:
	$(CC) $(CCFLAGS) -c $<
