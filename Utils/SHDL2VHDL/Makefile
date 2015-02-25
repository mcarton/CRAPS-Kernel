all: archive

JAVA_Sources = src/
shdl_Sources = $(JAVA_Sources)org/jcb/shdl/
JAVA_Classes = bin/
JAVA_Dest = build
shdl2vhdl_JAR_Name = shdl2vhdl2.1.10.jar

compile:
	mkdir -p $(JAVA_Dest)
	javac -d $(JAVA_Dest) -cp $(JAVA_Classes):$(JAVA_Sources) $(shdl_Sources)/*.java

archive: compile
	cp -r resources $(JAVA_Dest)
	cp -r $(JAVA_Classes)* $(JAVA_Dest)
	jar cfe $(shdl2vhdl_JAR_Name) org.jcb.shdl.SHDL2VHDLConverter -C $(JAVA_Dest) .

compile_shdl2vhdl:
	mkdir -p shdl2vhdl_build

clean_dest:
	rm  -rf $(JAVA_Dest)

clean: clean_dest
	rm -f $(shdl2vhdl_JAR_Name)

