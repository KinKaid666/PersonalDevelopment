# declarations
CC=		gcc 
CCFLAGS=	-ansi -ggdb
LINKFLAGS=	
TARGETS=	ping helloworld pingMutEx philosophers
OBJS=		ping.o helloworld.o pingMutEx.o philosophers.o

all:	$(TARGETS)

.c.o:
	$(CC) $(CCFLAGS) -c $<

# rules
ping:	ping.o
	$(CC) $(LINKFLAGS) -o $@ $?

helloworld:	helloworld.o
	$(CC) $(LINKFLAGS) -o $@ $?

pingMutEx:	pingMutEx.o
	$(CC) $(LINKFLAGS) -o $@ $?

philosophers:	philosophers.o
	$(CC) $(LINKFLAGS) -o $@ $?

clean:
	-rm $(OBJS)
realclean:clean
	-rm $(TARGETS)
	-rm $(TARGETS:=.core)
