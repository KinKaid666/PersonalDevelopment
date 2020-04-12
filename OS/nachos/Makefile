# Copyright (c) 1992 The Regents of the University of California.
# All rights reserved.  See copyright.h for copyright notice and limitation 
# of liability and disclaimer of warranty provisions.

# $Id: Makefile,v 3.1 2001/11/12 01:50:27 trc2876 Exp $

# $Log: Makefile,v $
# Revision 3.1  2001/11/12 01:50:27  trc2876
# *** empty log message ***
#
# Revision 3.0  2001/11/04 19:45:24  trc2876
# force revisions to 3.0
#
# Revision 2.1  2001/10/28 20:09:11  trc2876
# Fixed error in kernToUser and userToKern....these were only working if you
# copied a single page of data...they work on page-spanning data now
#
# we can have more args and longer args..set these in userprog/argdefs.h
# currently, these are set to 8 and 32 respectivly
#
# Revision 2.0  2001/10/11 02:51:06  trc2876
# changing revision numbers to the 2.0 tree
#
# Revision 1.1  2001/09/23 20:27:07  trc2876
# Initial revision
#

THREADSSUBMIT=threads/Makefile threads/*.cc threads/*.h threads/TESTCASES
TESTSUBMIT=test/Makefile test/*.c test/start.s test/script
USERPROGSUBMIT=$(THREADSSUBMIT) userprog/Makefile userprog/*.cc \
	userprog/*.h userprog/TESTCASES $(TESTSUBMIT)
VMSUBMIT=$(THREADSSUBMIT) $(USERPROGSUBMIT) vm/*.cc vm/*.h vm/TESTCASES \
	$(TESTSUBMIT)
FILESYSSUBMIT=$(THREADSSUBMIT) $(USERPROGSUBMIT) $(VMSUBMIT) \
	filesys/*.cc filesys/*.h filesys/TESTCASES $(TESTSUBMIT)

MAKE = make

all: 
	cd threads; $(MAKE) depend
	cd threads; $(MAKE) nachos
	cd userprog; $(MAKE) depend 
	cd userprog; $(MAKE) nachos 
	cd vm; $(MAKE) depend
	cd vm; $(MAKE) nachos 
	cd filesys; $(MAKE) depend
	cd filesys; $(MAKE) nachos 

depend:
	cd threads; $(MAKE) depend
	cd userprog; $(MAKE) depend 
	cd vm; $(MAKE) depend
	cd filesys; $(MAKE) depend

tags:
	ctags --recurse
	cd threads; ctags --recurse
	cd userprog; ctags --recurse
	cd vm; ctags --recurse
	cd filesys; ctags --recurse

threadscode:
	cd threads; $(MAKE) nachos

threadsdep:
	cd threads; $(MAKE) depend

threadsclean:
	-/bin/csh -c "rm -f tags threads/{core,nachos,DISK,*.o,swtch.s}

threadstar:
	tar cvf threadssubmit.tar Makefile.common Makefile.dep \
		$(THREADSSUBMIT)

userprogcode:
	cd userprog; $(MAKE) nachos

userprogdep:
	cd userprog; $(MAKE) depend

userprogclean:
	-/bin/csh -c "rm -f tags userprog/{core,nachos,DISK,*.o,swtch.s}

userprogtar:
	tar cvf userprogsubmit.tar Makefile.common Makefile.dep \
		$(USERPROGSUBMIT)

vmcode:
	cd vm; $(MAKE) nachos 

vmdep:
	cd vm; $(MAKE) depend

vmclean:
	-/bin/csh -c "rm -f tags vm/{core,nachos,DISK,*.o,swtch.s}

vmtar:
	tar cvf vmsubmit.tar Makefile.common Makefile.dep $(VMSUBMIT)

filesyscode:
	cd filesys; $(MAKE) nachos 

filesysdep:
	cd filesys; $(MAKE) depend

filesysclean:
	-/bin/csh -c "rm -f tags filesys/{core,nachos,DISK,*.o,swtch.s}

filesystar:
	tar cvf filesyssubmit.tar Makefile.common Makefile.dep \
		$(FILESYSSUBMIT)

testcode:
	cd test; $(MAKE)

testclean:
	cd test; $(MAKE) clean

allclean: threadsclean userprogclean testclean filesysclean vmclean







