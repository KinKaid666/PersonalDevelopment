CC=		g++
CCFLAGS=	-g 
LINK=		g++
LINKFLAGS=	
RM=		rm

# Targets
all:		non_virtual virtual

clean:
	-$(RM) *.o

realclean:	clean
	-$(RM) non_virtual virtual
	-$(RM) core

non_virtual:	non_virtual.o
	$(LINK) $(LINKFLAGS) -o $@ $?

virtual:	virtual.o
	$(LINK) $(LINKFLAGS) -o $@ $?


# Suffix Rules
.SUFFIXES:
.SUFFIXES: 	.C .o
.C.o:
	$(CC) $(CCFLAGS) -c $<
