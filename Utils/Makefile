all: archive

Mon_dir = ./CrapsMon5
JAVA_Sources = $(Mon_dir)/src/
craps_Sources = $(JAVA_Sources)org/jcb/craps
crapsc_Sources = $(craps_Sources)/crapsc/java
JAVA_Classes = $(Mon_dir)/bin/
JAVA_Dest = $(Mon_dir)/build
JAR_Name = CrapsMon5.jar

compile: 
	mkdir -p $(JAVA_Dest)
	javac -d $(JAVA_Dest) -cp $(JAVA_Classes):$(JAVA_Sources) $(craps_Sources)/*.java

archive: compile
	cp -r $(JAVA_Classes)* $(JAVA_Dest) 
	jar cfe $(JAR_Name) org.jcb.craps.CrapsEmu -C $(JAVA_Dest) .

clean_dest:
	rm  -rf $(JAVA_Dest)

clean: clean_dest
	rm -f $(JAR_Name)
