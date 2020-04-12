design.dvi: design.toc \
	    multiprogramming_design.tex \
	    virtualmemory_design.tex \
	    filesys_design.tex
	latex design.tex
design.toc: multiprogramming_design.tex \
	    virtualmemory_design.tex \
	    filesys_design.tex
	latex design.tex
design.ps: design.dvi
	dvips design.dvi -o design.ps
clean:
	-rm -f *.aux *.toc *.dvi *.ps *.log
