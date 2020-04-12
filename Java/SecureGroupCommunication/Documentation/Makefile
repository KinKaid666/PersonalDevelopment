#
# Stripped down Makefile for building LaTeX documents 
#
# Joe Binder (23-Jan-03)
#

# Application definitions
LATEX=/usr/local/bin/latex2e
# LATEX2HTML=/home/stu2/s19/jsb7384/pub/latex2html/latex2html

# pdftex is the conversion utility; pdflatex contains the format defines
PDFTEX=ps2pdf
# PDFLATEX=/home/stu2/s19/jsb7384/pub/pdflatex

# References utitilities
BIBTEX=/usr/local/bin/bibtex
# BIB2HTML=/home/stu2/s19/jsb7384/bin/bib2html

DVIPS=/usr/local/bin/dvips

# Document-specific definitions
# YOU WILL HAVE TO CHANGE THESE
SOURCE=design    
# HTMLBASE=/home/stu2/s19/jsb7384/public_html/thesis/ 
# HTMLTARGET=/home/stu2/s19/jsb7384/public_html/thesis/proposal/


# Document types
T=.tex 		# Source documents
P=.ps		# Final output format
D=.dvi		# Intermediate format
H=.html		# Web format
B=.bbl		# References (BiBTeX format)
C=.toc		# Table of contents format
A=.aux      # Aux format (required for references, I believe)
F=.pdf      # PDF Format

# Suffix defines. 
.SUFFIXES: $T $P $D $B $F

$T$D:
	$(LATEX) $< 
$D$P:
	$(DVIPS) -o $@ $<
$T$F:
	$(PDFTEX) $(SOURCE)$P $(SOURCE)$F


DOC_SOURCE= $(SOURCE)$T
DOC_DVI=$(SOURCE)$D
DOC_PS=$(SOURCE)$P
DOC_PDF=$(SOURCE)$F


all::   compile 

# Build the DVI, PS, and PDF files
compile: $(SOURCE)$D $(SOURCE)$P $(SOURCE)$F

refs:: references 

# Build the Bibliography.  This should *not* be changed.
references: 
	$(LATEX) $(SOURCE)$T
	# $(BIBTEX) $(SOURCE)
	$(LATEX) $(SOURCE)$T
	$(LATEX) $(SOURCE)$T

# Export the document to the web.  This is probably overkill
web:	
	# $(LATEX2HTML) $(SOURCE)$T 
	# $(BIB2HTML) long references
	# @ cp -R $(SOURCE) $(HTMLTARGET); \
	  # cp bib.html $(HTMLBASE)working/; \
	  # rm -f $(HTMLTARGET)*.p*; \
	  # cp $(DOC_DVI) $(HTMLTARGET); \
	  # cp $(DOC_PS) $(HTMLTARGET); \
	  # cp $(DOC_PDF) $(HTMLTARGET); \
	  # chmod 755 $(HTMLTARGET)*; \
	  # chmod -R 755  $(HTMLTARGET)$(SOURCE)/*; \
	  # chmod 755 $(HTMLBASE)working/*; \
	  # chmod -R 755 $(HTMLTARGET); 

# Hose all but the tex sources
clean:
	@ rm -f $(SOURCE)$A $(SOURCE)$P $(SOURCE)$D $(SOURCE)$C $(SOURCE)$F; \
	  rm -rf $(SOURCE); rm -f *bbl *log *blg 
