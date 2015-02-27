all:                                  \
	build/FinalReport.pdf         \
	build/Gantt.pdf               \
	build/ProjectCharter.pdf      \
	build/Slides.pdf              \
	build/Specifications.pdf      \
	build/TestPlan.pdf

export TEXC = latexmk -output-directory=build -pdflatex="lualatex %O %S" -pdf -dvi- -ps-

.PHONY: FORCE

build/ProjectCharter.pdf: ProjectCharter.tex titlepage.tex build/Gantt.pdf
	$(TEXC) -latexoption=-shell-escape $<

build/FinalReport.pdf: FinalReport.tex build/Gantt.pdf
	$(TEXC) -latexoption=-shell-escape $<

build/%.pdf: %.tex FORCE
	$(TEXC) -latexoption=-shell-escape $<

clean: FORCE
	$(TEXC) -C

clean_all: FORCE
	rm -r build
