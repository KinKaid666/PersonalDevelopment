TARGET=		hello
AS=		as
ASFLAGS=	--gstab
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
.SUFFIXES: 	.s .o
.s.o:
	$(AS) $(ASFLAGS) -o $@ $<
