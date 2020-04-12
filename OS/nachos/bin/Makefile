# Use normal make for this Makefile
#
# Makefile for:
#	coff2noff -- converts a normal MIPS executable into a Nachos executable
#
# Copyright (c) 1992 The Regents of the University of California.
# All rights reserved.  See copyright.h for copyright notice and limitation 
# of liability and disclaimer of warranty provisions.

# If the host is big endian (SPARC, SNAKE, etc):
# change to (disassemble and coff2flat don't support big endian yet):
# CFLAGS= -I./ -I../threads -DHOST_IS_BIG_ENDIAN
# all: coff2noff

CC=gcc
CFLAGS= -I./ -I../threads -DHOST_IS_BIG_ENDIAN
LD=gcc
PROGS=coff2noff

all: $(PROGS)

# converts a COFF file to Nachos object format
coff2noff: coff2noff.o
	$(LD) coff2noff.o -o coff2noff

clean:
	rm -f *.o *.coff $(PROGS)
