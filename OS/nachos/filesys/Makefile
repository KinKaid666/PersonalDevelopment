# NOTE: this is a GNU Makefile.  You must use "gmake" rather than "make".
#
# Makefile for the file system assignment
#    Defines set up assuming multiprogramming and virtual memory done first.
#    If not, use the "bare bones"  defines below. 
#
# Copyright (c) 1992 The Regents of the University of California.
# All rights reserved.  See copyright.h for copyright notice and limitation 
# of liability and disclaimer of warranty provisions.

DEFINES = -DUSER_PROGRAM -DVM -DUSE_TLB -DFILESYS -DFILESYS_NEEDED
INCPATH = -I../filesys -I../bin -I../vm -I../userprog -I../threads -I../machine
HFILES = $(THREAD_H) $(VM_H) $(FILESYS_H)
CFILES = $(THREAD_C) $(VM_C) $(FILESYS_C)
C_OFILES = $(THREAD_O) $(VM_O) $(FILESYS_O)

# bare bones version
# DEFINES =-DTHREADS -DFILESYS_NEEDED -DFILESYS
# INCPATH = -I../filesys -I../threads -I../machine
# HFILES = $(THREAD_H) $(FILESYS_H)
# CFILES = $(THREAD_C) $(FILESYS_C)
# C_OFILES = $(THREAD_O) $(FILESYS_O)

include ../Makefile.common
include ../Makefile.dep
#-----------------------------------------------------------------
# DO NOT DELETE THIS LINE -- make depend uses it
# DEPENDENCIES MUST END AT END OF FILE
main.o: ../threads/main.cc ../threads/copyright.h ../threads/utility.h \
 ../machine/sysdep.h /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/system.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/list.h ../threads/synch.h \
 ../userprog/argdefs.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h ../threads/threadtest.h
alarms.o: ../threads/alarms.cc ../threads/system.h \
 ../threads/copyright.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/list.h ../threads/synch.h \
 ../userprog/argdefs.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
list.o: ../threads/list.cc ../threads/copyright.h ../threads/list.h \
 ../threads/utility.h ../machine/sysdep.h /usr/include/stdio.h \
 /usr/include/iso/stdio_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h /usr/include/sys/va_list.h \
 /usr/include/stdio_tag.h /usr/include/stdio_impl.h \
 /usr/include/string.h /usr/include/iso/string_iso.h
dklist.o: ../threads/dklist.cc ../threads/dklist.h \
 ../threads/utility.h ../threads/copyright.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h
scheduler.o: ../threads/scheduler.cc ../threads/copyright.h \
 ../threads/scheduler.h ../threads/list.h ../threads/utility.h \
 ../machine/sysdep.h /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/synch.h ../userprog/argdefs.h \
 ../threads/system.h ../machine/interrupt.h ../machine/stats.h \
 ../machine/timer.h ../filesys/filesys.h ../filesys/etree.h \
 ../filesys/NodeCache.h ../filesys/Cache.h ../filesys/DataCache.h \
 ../userprog/ProcMan.h ../userprog/syscall.h ../userprog/ProcessInfo.h \
 ../filesys/filestat.h ../threads/alarms.h ../userprog/SConsole.h \
 ../machine/console.h ../vm/KSwap.h ../threads/dklist.h \
 ../vm/SwapFile.h ../vm/bintree.h ../vm/KSwapInfo.h ../bin/noff.h \
 ../vm/VMStats.h ../filesys/synchdisk.h
synch.o: ../threads/synch.cc ../threads/copyright.h ../threads/synch.h \
 ../threads/thread.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../machine/machine.h \
 ../machine/translate.h ../machine/disk.h ../userprog/addrspace.h \
 ../userprog/bitmap.h ../filesys/openfile.h ../filesys/mtree.h \
 /usr/include/stdlib.h /usr/include/iso/stdlib_iso.h \
 ../userprog/vminfo.h ../userprog/Process.h ../threads/list.h \
 ../userprog/argdefs.h ../threads/system.h ../threads/scheduler.h \
 ../machine/interrupt.h ../machine/stats.h ../machine/timer.h \
 ../filesys/filesys.h ../filesys/etree.h ../filesys/NodeCache.h \
 ../filesys/Cache.h ../filesys/DataCache.h ../userprog/ProcMan.h \
 ../userprog/syscall.h ../userprog/ProcessInfo.h ../filesys/filestat.h \
 ../threads/alarms.h ../userprog/SConsole.h ../machine/console.h \
 ../vm/KSwap.h ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
synchlist.o: ../threads/synchlist.cc ../threads/copyright.h \
 ../threads/synchlist.h ../threads/list.h ../threads/utility.h \
 ../machine/sysdep.h /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/synch.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../userprog/argdefs.h
system.o: ../threads/system.cc ../threads/copyright.h \
 ../threads/system.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/list.h ../threads/synch.h \
 ../userprog/argdefs.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
thread.o: ../threads/thread.cc ../threads/copyright.h \
 ../threads/thread.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../machine/machine.h \
 ../machine/translate.h ../machine/disk.h ../userprog/addrspace.h \
 ../userprog/bitmap.h ../filesys/openfile.h ../filesys/mtree.h \
 /usr/include/stdlib.h /usr/include/iso/stdlib_iso.h \
 ../userprog/vminfo.h ../userprog/Process.h ../threads/list.h \
 ../threads/synch.h ../userprog/argdefs.h ../threads/switch.h \
 ../threads/system.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
utility.o: ../threads/utility.cc ../threads/copyright.h \
 ../threads/utility.h ../machine/sysdep.h /usr/include/stdio.h \
 /usr/include/iso/stdio_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h /usr/include/sys/va_list.h \
 /usr/include/stdio_tag.h /usr/include/stdio_impl.h \
 /usr/include/string.h /usr/include/iso/string_iso.h \
 /usr/local/gnu/lib/gcc-lib/sparc-sun-solaris2.8/2.95.3/include/stdarg.h \
 /usr/local/gnu/lib/gcc-lib/sparc-sun-solaris2.8/2.95.3/include/va-sparc.h
threadtest.o: ../threads/threadtest.cc ../threads/copyright.h \
 ../threads/system.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/list.h ../threads/synch.h \
 ../userprog/argdefs.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h ../threads/threadtest.h
interrupt.o: ../machine/interrupt.cc ../threads/copyright.h \
 ../machine/interrupt.h ../threads/list.h ../threads/utility.h \
 ../machine/sysdep.h /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/system.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/synch.h ../userprog/argdefs.h \
 ../threads/scheduler.h ../machine/stats.h ../machine/timer.h \
 ../filesys/filesys.h ../filesys/etree.h ../filesys/NodeCache.h \
 ../filesys/Cache.h ../filesys/DataCache.h ../userprog/ProcMan.h \
 ../userprog/syscall.h ../userprog/ProcessInfo.h ../filesys/filestat.h \
 ../threads/alarms.h ../userprog/SConsole.h ../machine/console.h \
 ../vm/KSwap.h ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
sysdep.o: ../machine/sysdep.cc ../threads/copyright.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h /usr/include/signal.h \
 /usr/include/sys/types.h /usr/include/sys/machtypes.h \
 /usr/include/sys/int_types.h /usr/include/sys/select.h \
 /usr/include/sys/time.h /usr/include/time.h \
 /usr/include/iso/time_iso.h /usr/include/sys/time_impl.h \
 /usr/include/iso/signal_iso.h /usr/include/sys/iso/signal_iso.h \
 /usr/include/sys/unistd.h /usr/include/sys/signal.h \
 /usr/include/sys/siginfo.h /usr/include/sys/machsig.h \
 /usr/include/sys/procset.h /usr/include/sys/socket.h \
 /usr/include/sys/uio.h /usr/include/sys/netconfig.h \
 /usr/include/netinet/in.h \
 /usr/local/gnu/lib/gcc-lib/sparc-sun-solaris2.8/2.95.3/include/sys/stream.h \
 /usr/include/sys/stream.h /usr/include/sys/vnode.h \
 /usr/include/sys/t_lock.h /usr/include/sys/machlock.h \
 /usr/include/sys/param.h /usr/include/sys/mutex.h \
 /usr/include/sys/rwlock.h /usr/include/sys/semaphore.h \
 /usr/include/sys/condvar.h /usr/include/sys/cred.h \
 /usr/include/sys/resource.h /usr/include/vm/seg_enum.h \
 /usr/include/sys/poll.h /usr/include/sys/strmdep.h \
 /usr/include/sys/model.h /usr/include/sys/strft.h \
 /usr/include/sys/byteorder.h /usr/include/sys/un.h \
 /usr/include/net/if_dl.h /usr/include/sys/file.h \
 /usr/include/sys/mman.h /usr/include/fcntl.h /usr/include/sys/fcntl.h \
 ../machine/interrupt.h ../threads/list.h ../threads/utility.h \
 ../machine/sysdep.h ../threads/system.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/synch.h ../userprog/argdefs.h \
 ../threads/scheduler.h ../machine/stats.h ../machine/timer.h \
 ../filesys/filesys.h ../filesys/etree.h ../filesys/NodeCache.h \
 ../filesys/Cache.h ../filesys/DataCache.h ../userprog/ProcMan.h \
 ../userprog/syscall.h ../userprog/ProcessInfo.h ../filesys/filestat.h \
 ../threads/alarms.h ../userprog/SConsole.h ../machine/console.h \
 ../vm/KSwap.h ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
stats.o: ../machine/stats.cc ../threads/copyright.h \
 ../threads/utility.h ../machine/sysdep.h /usr/include/stdio.h \
 /usr/include/iso/stdio_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h /usr/include/sys/va_list.h \
 /usr/include/stdio_tag.h /usr/include/stdio_impl.h \
 /usr/include/string.h /usr/include/iso/string_iso.h \
 ../machine/stats.h
timer.o: ../machine/timer.cc ../threads/copyright.h ../machine/timer.h \
 ../threads/utility.h ../machine/sysdep.h /usr/include/stdio.h \
 /usr/include/iso/stdio_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h /usr/include/sys/va_list.h \
 /usr/include/stdio_tag.h /usr/include/stdio_impl.h \
 /usr/include/string.h /usr/include/iso/string_iso.h \
 ../threads/system.h ../threads/thread.h ../machine/machine.h \
 ../machine/translate.h ../machine/disk.h ../userprog/addrspace.h \
 ../userprog/bitmap.h ../filesys/openfile.h ../filesys/mtree.h \
 /usr/include/stdlib.h /usr/include/iso/stdlib_iso.h \
 ../userprog/vminfo.h ../userprog/Process.h ../threads/list.h \
 ../threads/synch.h ../userprog/argdefs.h ../threads/scheduler.h \
 ../machine/interrupt.h ../machine/stats.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
KSwap.o: ../vm/KSwap.cc /usr/include/strings.h \
 /usr/include/sys/types.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/machtypes.h \
 /usr/include/sys/int_types.h /usr/include/sys/select.h \
 /usr/include/sys/time.h /usr/include/time.h \
 /usr/include/iso/time_iso.h /usr/include/sys/time_impl.h \
 /usr/include/string.h /usr/include/iso/string_iso.h ../vm/KSwap.h \
 ../threads/list.h ../threads/copyright.h ../threads/utility.h \
 ../machine/sysdep.h /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h ../threads/thread.h ../machine/machine.h \
 ../machine/translate.h ../machine/disk.h ../userprog/addrspace.h \
 ../userprog/bitmap.h ../filesys/openfile.h ../filesys/mtree.h \
 /usr/include/stdlib.h /usr/include/iso/stdlib_iso.h \
 ../userprog/vminfo.h ../userprog/Process.h ../threads/synch.h \
 ../userprog/argdefs.h ../threads/dklist.h ../vm/SwapFile.h \
 ../vm/bintree.h ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../threads/system.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../filesys/synchdisk.h
SwapFile.o: ../vm/SwapFile.cc ../vm/SwapFile.h ../vm/bintree.h \
 ../threads/list.h ../threads/copyright.h ../threads/utility.h \
 ../machine/sysdep.h /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/synch.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../userprog/argdefs.h ../threads/system.h \
 ../threads/scheduler.h ../machine/interrupt.h ../machine/stats.h \
 ../machine/timer.h ../filesys/filesys.h ../filesys/etree.h \
 ../filesys/NodeCache.h ../filesys/Cache.h ../filesys/DataCache.h \
 ../userprog/ProcMan.h ../userprog/syscall.h ../userprog/ProcessInfo.h \
 ../filesys/filestat.h ../threads/alarms.h ../userprog/SConsole.h \
 ../machine/console.h ../vm/KSwap.h ../threads/dklist.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
bintree.o: ../vm/bintree.cc /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h ../vm/bintree.h ../threads/system.h \
 ../threads/copyright.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h ../userprog/vminfo.h ../userprog/Process.h \
 ../threads/list.h ../threads/synch.h ../userprog/argdefs.h \
 ../threads/scheduler.h ../machine/interrupt.h ../machine/stats.h \
 ../machine/timer.h ../filesys/filesys.h ../filesys/etree.h \
 ../filesys/NodeCache.h ../filesys/Cache.h ../filesys/DataCache.h \
 ../userprog/ProcMan.h ../userprog/syscall.h ../userprog/ProcessInfo.h \
 ../filesys/filestat.h ../threads/alarms.h ../userprog/SConsole.h \
 ../machine/console.h ../vm/KSwap.h ../threads/dklist.h \
 ../vm/SwapFile.h ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
addrspace.o: ../userprog/addrspace.cc ../threads/copyright.h \
 ../threads/system.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/list.h ../threads/synch.h \
 ../userprog/argdefs.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h /usr/include/strings.h \
 /usr/include/sys/types.h /usr/include/sys/machtypes.h \
 /usr/include/sys/int_types.h /usr/include/sys/select.h \
 /usr/include/sys/time.h /usr/include/time.h \
 /usr/include/iso/time_iso.h /usr/include/sys/time_impl.h
bitmap.o: ../userprog/bitmap.cc ../threads/copyright.h \
 ../userprog/bitmap.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h
Process.o: ../userprog/Process.cc ../userprog/Process.h \
 ../threads/list.h ../threads/copyright.h ../threads/utility.h \
 ../machine/sysdep.h /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/addrspace.h \
 ../machine/translate.h ../userprog/bitmap.h ../userprog/vminfo.h \
 ../threads/thread.h ../machine/machine.h ../machine/disk.h \
 ../threads/synch.h ../userprog/argdefs.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/system.h \
 ../threads/scheduler.h ../machine/interrupt.h ../machine/stats.h \
 ../machine/timer.h ../userprog/ProcMan.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
ProcMan.o: ../userprog/ProcMan.cc ../userprog/ProcMan.h \
 ../userprog/syscall.h ../threads/copyright.h \
 ../userprog/ProcessInfo.h ../userprog/argdefs.h ../filesys/filestat.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../userprog/vminfo.h ../threads/list.h \
 ../threads/synch.h ../threads/thread.h ../machine/machine.h \
 ../machine/translate.h ../machine/disk.h ../userprog/addrspace.h \
 ../userprog/bitmap.h ../filesys/openfile.h ../userprog/Process.h \
 ../threads/system.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../threads/alarms.h ../userprog/SConsole.h \
 ../machine/console.h ../vm/KSwap.h ../threads/dklist.h \
 ../vm/SwapFile.h ../vm/bintree.h ../vm/KSwapInfo.h ../bin/noff.h \
 ../vm/VMStats.h ../filesys/synchdisk.h /usr/include/strings.h \
 /usr/include/sys/types.h /usr/include/sys/machtypes.h \
 /usr/include/sys/int_types.h /usr/include/sys/select.h \
 /usr/include/sys/time.h /usr/include/time.h \
 /usr/include/iso/time_iso.h /usr/include/sys/time_impl.h
exception.o: ../userprog/exception.cc ../threads/copyright.h \
 ../threads/system.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/list.h ../threads/synch.h \
 ../userprog/argdefs.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
progtest.o: ../userprog/progtest.cc ../threads/copyright.h \
 ../threads/system.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/list.h ../threads/synch.h \
 ../userprog/argdefs.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h /usr/include/strings.h \
 /usr/include/sys/types.h /usr/include/sys/machtypes.h \
 /usr/include/sys/int_types.h /usr/include/sys/select.h \
 /usr/include/sys/time.h /usr/include/time.h \
 /usr/include/iso/time_iso.h /usr/include/sys/time_impl.h
SConsole.o: ../userprog/SConsole.cc /usr/include/strings.h \
 /usr/include/sys/types.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/machtypes.h \
 /usr/include/sys/int_types.h /usr/include/sys/select.h \
 /usr/include/sys/time.h /usr/include/time.h \
 /usr/include/iso/time_iso.h /usr/include/sys/time_impl.h \
 /usr/include/string.h /usr/include/iso/string_iso.h \
 ../userprog/SConsole.h ../machine/console.h ../threads/copyright.h \
 ../threads/utility.h ../machine/sysdep.h /usr/include/stdio.h \
 /usr/include/iso/stdio_iso.h /usr/include/sys/va_list.h \
 /usr/include/stdio_tag.h /usr/include/stdio_impl.h ../threads/synch.h \
 ../threads/thread.h ../machine/machine.h ../machine/translate.h \
 ../machine/disk.h ../userprog/addrspace.h ../userprog/bitmap.h \
 ../filesys/openfile.h ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/list.h ../userprog/argdefs.h
console.o: ../machine/console.cc ../threads/copyright.h \
 ../machine/console.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/system.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/list.h ../threads/synch.h \
 ../userprog/argdefs.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../vm/KSwap.h ../threads/dklist.h \
 ../vm/SwapFile.h ../vm/bintree.h ../vm/KSwapInfo.h ../bin/noff.h \
 ../vm/VMStats.h ../filesys/synchdisk.h
machine.o: ../machine/machine.cc /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h ../threads/copyright.h \
 ../machine/machine.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../machine/translate.h \
 ../machine/disk.h ../threads/system.h ../threads/thread.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h ../userprog/vminfo.h ../userprog/Process.h \
 ../threads/list.h ../threads/synch.h ../userprog/argdefs.h \
 ../threads/scheduler.h ../machine/interrupt.h ../machine/stats.h \
 ../machine/timer.h ../filesys/filesys.h ../filesys/etree.h \
 ../filesys/NodeCache.h ../filesys/Cache.h ../filesys/DataCache.h \
 ../userprog/ProcMan.h ../userprog/syscall.h ../userprog/ProcessInfo.h \
 ../filesys/filestat.h ../threads/alarms.h ../userprog/SConsole.h \
 ../machine/console.h ../vm/KSwap.h ../threads/dklist.h \
 ../vm/SwapFile.h ../vm/bintree.h ../vm/KSwapInfo.h ../bin/noff.h \
 ../vm/VMStats.h ../filesys/synchdisk.h
mipssim.o: ../machine/mipssim.cc ../threads/copyright.h \
 ../machine/machine.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../machine/translate.h \
 ../machine/disk.h ../machine/mipssim.h ../threads/system.h \
 ../threads/thread.h ../userprog/addrspace.h ../userprog/bitmap.h \
 ../filesys/openfile.h ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/list.h ../threads/synch.h \
 ../userprog/argdefs.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
translate.o: ../machine/translate.cc ../threads/copyright.h \
 ../machine/machine.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../machine/translate.h \
 ../machine/disk.h ../userprog/addrspace.h ../userprog/bitmap.h \
 ../filesys/openfile.h ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../threads/thread.h ../userprog/Process.h ../threads/list.h \
 ../threads/synch.h ../userprog/argdefs.h ../threads/system.h \
 ../threads/scheduler.h ../machine/interrupt.h ../machine/stats.h \
 ../machine/timer.h ../filesys/filesys.h ../filesys/etree.h \
 ../filesys/NodeCache.h ../filesys/Cache.h ../filesys/DataCache.h \
 ../userprog/ProcMan.h ../userprog/syscall.h ../userprog/ProcessInfo.h \
 ../filesys/filestat.h ../threads/alarms.h ../userprog/SConsole.h \
 ../machine/console.h ../vm/KSwap.h ../threads/dklist.h \
 ../vm/SwapFile.h ../vm/bintree.h ../vm/KSwapInfo.h ../bin/noff.h \
 ../vm/VMStats.h ../filesys/synchdisk.h
filesys.o: ../filesys/filesys.cc /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/copyright.h \
 ../machine/disk.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h ../filesys/filesys.h ../filesys/openfile.h \
 ../filesys/mtree.h ../filesys/etree.h ../machine/machine.h \
 ../machine/translate.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../threads/list.h ../threads/synch.h ../threads/thread.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../userprog/vminfo.h \
 ../userprog/Process.h ../userprog/argdefs.h ../filesys/DataCache.h
fstest.o: ../filesys/fstest.cc ../threads/copyright.h \
 ../threads/utility.h ../machine/sysdep.h /usr/include/stdio.h \
 /usr/include/iso/stdio_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h /usr/include/sys/va_list.h \
 /usr/include/stdio_tag.h /usr/include/stdio_impl.h \
 /usr/include/string.h /usr/include/iso/string_iso.h \
 ../filesys/filesys.h ../filesys/openfile.h ../filesys/mtree.h \
 /usr/include/stdlib.h /usr/include/iso/stdlib_iso.h \
 ../filesys/etree.h ../machine/machine.h ../machine/translate.h \
 ../machine/disk.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../threads/list.h ../threads/synch.h ../threads/thread.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../userprog/vminfo.h \
 ../userprog/Process.h ../userprog/argdefs.h ../filesys/DataCache.h \
 ../threads/system.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../userprog/ProcMan.h \
 ../userprog/syscall.h ../userprog/ProcessInfo.h ../filesys/filestat.h \
 ../threads/alarms.h ../userprog/SConsole.h ../machine/console.h \
 ../vm/KSwap.h ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h /usr/local/gnu/include/g++/iostream.h \
 /usr/local/gnu/include/g++/streambuf.h \
 /usr/local/gnu/include/g++/libio.h \
 /usr/local/gnu/include/g++/_G_config.h \
 /usr/local/gnu/lib/gcc-lib/sparc-sun-solaris2.8/2.95.3/include/stddef.h
openfile.o: ../filesys/openfile.cc ../threads/copyright.h \
 ../filesys/openfile.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../filesys/mtree.h \
 /usr/include/stdlib.h /usr/include/iso/stdlib_iso.h \
 ../threads/system.h ../threads/thread.h ../machine/machine.h \
 ../machine/translate.h ../machine/disk.h ../userprog/addrspace.h \
 ../userprog/bitmap.h ../userprog/vminfo.h ../userprog/Process.h \
 ../threads/list.h ../threads/synch.h ../userprog/argdefs.h \
 ../threads/scheduler.h ../machine/interrupt.h ../machine/stats.h \
 ../machine/timer.h ../filesys/filesys.h ../filesys/etree.h \
 ../filesys/NodeCache.h ../filesys/Cache.h ../filesys/DataCache.h \
 ../userprog/ProcMan.h ../userprog/syscall.h ../userprog/ProcessInfo.h \
 ../filesys/filestat.h ../threads/alarms.h ../userprog/SConsole.h \
 ../machine/console.h ../vm/KSwap.h ../threads/dklist.h \
 ../vm/SwapFile.h ../vm/bintree.h ../vm/KSwapInfo.h ../bin/noff.h \
 ../vm/VMStats.h ../filesys/synchdisk.h
synchdisk.o: ../filesys/synchdisk.cc ../threads/copyright.h \
 ../filesys/synchdisk.h ../machine/disk.h ../threads/utility.h \
 ../machine/sysdep.h /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/synch.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../userprog/addrspace.h \
 ../userprog/bitmap.h ../filesys/openfile.h ../filesys/mtree.h \
 /usr/include/stdlib.h /usr/include/iso/stdlib_iso.h \
 ../userprog/vminfo.h ../userprog/Process.h ../threads/list.h \
 ../userprog/argdefs.h
disk.o: ../machine/disk.cc ../threads/copyright.h ../machine/disk.h \
 ../threads/utility.h ../machine/sysdep.h /usr/include/stdio.h \
 /usr/include/iso/stdio_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h /usr/include/sys/va_list.h \
 /usr/include/stdio_tag.h /usr/include/stdio_impl.h \
 /usr/include/string.h /usr/include/iso/string_iso.h \
 ../threads/system.h ../threads/thread.h ../machine/machine.h \
 ../machine/translate.h ../userprog/addrspace.h ../userprog/bitmap.h \
 ../filesys/openfile.h ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/list.h ../threads/synch.h \
 ../userprog/argdefs.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
mtree.o: ../filesys/mtree.cc \
 /usr/local/gnu/lib/gcc-lib/sparc-sun-solaris2.8/2.95.3/../../../../sparc-sun-solaris2.8/include/assert.h \
 /usr/include/string.h /usr/include/iso/string_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 ../threads/utility.h ../threads/copyright.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h ../machine/machine.h ../machine/translate.h \
 ../machine/disk.h ../threads/system.h ../threads/thread.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../threads/list.h ../threads/synch.h \
 ../userprog/argdefs.h ../threads/scheduler.h ../machine/interrupt.h \
 ../machine/stats.h ../machine/timer.h ../filesys/filesys.h \
 ../filesys/etree.h ../filesys/NodeCache.h ../filesys/Cache.h \
 ../filesys/DataCache.h ../userprog/ProcMan.h ../userprog/syscall.h \
 ../userprog/ProcessInfo.h ../filesys/filestat.h ../threads/alarms.h \
 ../userprog/SConsole.h ../machine/console.h ../vm/KSwap.h \
 ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
etree.o: ../filesys/etree.cc /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h ../filesys/etree.h ../machine/machine.h \
 ../threads/copyright.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../machine/translate.h \
 ../machine/disk.h ../filesys/mtree.h ../threads/system.h \
 ../threads/thread.h ../userprog/addrspace.h ../userprog/bitmap.h \
 ../filesys/openfile.h ../userprog/vminfo.h ../userprog/Process.h \
 ../threads/list.h ../threads/synch.h ../userprog/argdefs.h \
 ../threads/scheduler.h ../machine/interrupt.h ../machine/stats.h \
 ../machine/timer.h ../filesys/filesys.h ../filesys/NodeCache.h \
 ../filesys/Cache.h ../filesys/DataCache.h ../userprog/ProcMan.h \
 ../userprog/syscall.h ../userprog/ProcessInfo.h ../filesys/filestat.h \
 ../threads/alarms.h ../userprog/SConsole.h ../machine/console.h \
 ../vm/KSwap.h ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
Cache.o: ../filesys/Cache.cc ../filesys/Cache.h ../threads/list.h \
 ../threads/copyright.h ../threads/utility.h ../machine/sysdep.h \
 /usr/include/stdio.h /usr/include/iso/stdio_iso.h \
 /usr/include/sys/feature_tests.h /usr/include/sys/isa_defs.h \
 /usr/include/sys/va_list.h /usr/include/stdio_tag.h \
 /usr/include/stdio_impl.h /usr/include/string.h \
 /usr/include/iso/string_iso.h ../threads/synch.h ../threads/thread.h \
 ../machine/machine.h ../machine/translate.h ../machine/disk.h \
 ../userprog/addrspace.h ../userprog/bitmap.h ../filesys/openfile.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h ../userprog/vminfo.h \
 ../userprog/Process.h ../userprog/argdefs.h ../threads/system.h \
 ../threads/scheduler.h ../machine/interrupt.h ../machine/stats.h \
 ../machine/timer.h ../filesys/filesys.h ../filesys/etree.h \
 ../filesys/NodeCache.h ../filesys/DataCache.h ../userprog/ProcMan.h \
 ../userprog/syscall.h ../userprog/ProcessInfo.h ../filesys/filestat.h \
 ../threads/alarms.h ../userprog/SConsole.h ../machine/console.h \
 ../vm/KSwap.h ../threads/dklist.h ../vm/SwapFile.h ../vm/bintree.h \
 ../vm/KSwapInfo.h ../bin/noff.h ../vm/VMStats.h \
 ../filesys/synchdisk.h
DataCache.o: ../filesys/DataCache.cc ../filesys/DataCache.h \
 ../filesys/Cache.h ../threads/list.h ../threads/copyright.h \
 ../threads/utility.h ../machine/sysdep.h /usr/include/stdio.h \
 /usr/include/iso/stdio_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h /usr/include/sys/va_list.h \
 /usr/include/stdio_tag.h /usr/include/stdio_impl.h \
 /usr/include/string.h /usr/include/iso/string_iso.h \
 ../threads/synch.h ../threads/thread.h ../machine/machine.h \
 ../machine/translate.h ../machine/disk.h ../userprog/addrspace.h \
 ../userprog/bitmap.h ../filesys/openfile.h ../filesys/mtree.h \
 /usr/include/stdlib.h /usr/include/iso/stdlib_iso.h \
 ../userprog/vminfo.h ../userprog/Process.h ../userprog/argdefs.h
NodeCache.o: ../filesys/NodeCache.cc ../filesys/NodeCache.h \
 ../filesys/mtree.h /usr/include/stdlib.h \
 /usr/include/iso/stdlib_iso.h /usr/include/sys/feature_tests.h \
 /usr/include/sys/isa_defs.h ../threads/utility.h \
 ../threads/copyright.h ../machine/sysdep.h /usr/include/stdio.h \
 /usr/include/iso/stdio_iso.h /usr/include/sys/va_list.h \
 /usr/include/stdio_tag.h /usr/include/stdio_impl.h \
 /usr/include/string.h /usr/include/iso/string_iso.h \
 ../filesys/Cache.h ../threads/list.h ../threads/synch.h \
 ../threads/thread.h ../machine/machine.h ../machine/translate.h \
 ../machine/disk.h ../userprog/addrspace.h ../userprog/bitmap.h \
 ../filesys/openfile.h ../userprog/vminfo.h ../userprog/Process.h \
 ../userprog/argdefs.h
# DEPENDENCIES MUST END AT END OF FILE
# IF YOU PUT STUFF HERE IT WILL GO AWAY
# see make depend above
