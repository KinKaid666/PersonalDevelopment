TARGET=		sumset
CC=		g++
CCFLAGS=	-g -ansi 
LINK=		g++
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
.SUFFIXES: 	.cpp .o
.cpp.o:
	$(CC) $(CCFLAGS) -c $<
