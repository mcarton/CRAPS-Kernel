all: ProjectCharter.pdf

export TEXC = latexmk -pdflatex="lualatex %O %S" -pdf -dvi- -ps-

.PHONY: FORCE

%.pdf: %.tex FORCE
	$(TEXC) -latexoption=-shell-escape $<

clean: FORCE
	$(TEXC) -C
	$(RM) *.nav *.snm *.vrb *.aux *.log *.fls *.fdb_latexmk *.out *.toc

clean_all: clean FORCE
	$(RM) *.pdf
