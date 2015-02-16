all: FinalReport.pdf ProjectCharter.pdf Specifications.pdf Gantt.pdf

export TEXC = latexmk -pdflatex="lualatex %O %S" -pdf -dvi- -ps-

.PHONY: FORCE

ProjectCharter.pdf: ProjectCharter.tex titlepage.tex Gantt.pdf
	$(TEXC) -latexoption=-shell-escape $<

%.pdf: %.tex FORCE
	$(TEXC) -latexoption=-shell-escape $<

clean: FORCE
	$(TEXC) -C
	$(RM) *.nav *.snm *.vrb *.aux *.log *.fls *.fdb_latexmk *.out *.toc

clean_all: clean FORCE
	$(RM) *.pdf
