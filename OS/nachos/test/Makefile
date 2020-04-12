# use normal make for this Makefile
#
# Makefile for building user programs to run on top of Nachos
#
# Several things to be aware of:
#
#    Nachos assumes that the location of the program startup routine (the
# 	location the kernel jumps to when the program initially starts up)
#       is at location 0.  This means: start.o must be the first .o passed 
# 	to ld, in order for the routine "Start" to be loaded at location 0
#

# if you are cross-compiling, you need to point to the right executables
# and change the flags to ld and the build procedure for as
GCCDIR = /home/course/icss544/pub/bin
LDFLAGS = -T script -N
ASFLAGS = -mips1
CPPFLAGS = $(INCDIR)


# if you aren't cross-compiling:
#GCCDIR =
#LDFLAGS = -N -T 0
#ASFLAGS =
#CPPFLAGS = -P $(INCDIR)


CC = $(GCCDIR)/rungcc
AS = $(GCCDIR)/as
LD = $(GCCDIR)/ld

CPP = /lib/cpp
INCDIR =-I../userprog -I../threads -I../filesys -DTEST

CFLAGS = -G 0 -c $(INCDIR)
PROGS = halt matmult sort echo shell fork procCount write exec cat touch \
	yield ps bigarray toobigarray ls mv rm cp chmod cd mkdir echoHW

COPY = shell halt cd cp rm mv ls chmod mkdir cat touch

% : %.o start.o
	$(LD) $(LDFLAGS) start.o $*.o -o $*.coff
	$(GCCDIR)/coff2noff $*.coff $*
	rm *.coff

%.o : %.c
	$(CC) $(CFLAGS) -c $<

all: $(PROGS) swapfile

start.o: start.s ../userprog/syscall.h
	$(CPP) $(CPPFLAGS) start.s > strt.s
	$(AS) $(ASFLAGS) -o start.o strt.s
	rm strt.s
halt: halt.o start.o
sort: sort.o start.o
matmult: matmult.o start.o
echo: echo.o start.o 
shell: shell.o start.o
procCount:	procCount.o start.o
exec:	exec.o start.o
fork:	fork.o start.o
write:	write.o start.o
cat:	cat.o start.o
exit:	exit.o start.o
touch:	touch.o start.o
yield:	yield.o start.o
ps:	ps.o start.o
infLoop: infLoop.o start.o
kill:	kill.o start.o
bigarray: bigarray.o start.o
toobigarray: toobigarray.o start.o
ls: ls.o start.o
cp: cp.o start.o
mv: mv.o start.o
rm: rm.o start.o
chmod: chmod.o start.o
#stat: stat.o start.o
mkdir: mkdir.o start.o
cd: cd.o start.o
echoHW: echoHW.o start.o

copy: $(COPY)
	cp DISK.clean DISK
	@for x in $(COPY); do \
	    echo nachos -cp $$x bin/$$x; \
	    ../filesys/nachos -cp $$x bin/$$x; \
	done

clean:
	rm -f core nachosSwap.swp *.o *.coff $(PROGS)

swapfile: nachosSwap.swp

nachosSwap.swp:
	dd if=/dev/zero of=nachosSwap.swp bs=1k count=30

bigfile: 
	dd if=/dev/zero of=bigfile bs=128 count=767
