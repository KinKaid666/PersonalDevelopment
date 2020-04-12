TARGETS=	right wrong
CC=		g++
CCFLAGS=	-g 
LINK=		g++
LINKFLAGS=	
RM=		rm

# Targets
all:		$(TARGETS)

clean:
	-$(RM) *.o
	-$(RM) $(TARGETS)

realclean:	clean
	-$(RM) $(TARGETS)
	-$(RM) core

right:	right.C
	$(LINK) $(LINKFLAGS) -o $@ $?
wrong:	wrong.C
	$(LINK) $(LINKFLAGS) -o $@ $?


# Suffix Rules
.SUFFIXES:
.SUFFIXES: 	.C .o
.C.o:
	$(CC) $(CCFLAGS) -c $<
