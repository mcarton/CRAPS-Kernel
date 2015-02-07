all: archive

Mon_dir = ./CrapsMon5
JAVA_Sources = $(Mon_dir)/src/
craps_Sources = $(JAVA_Sources)org/jcb/craps
crapsc_Sources = $(craps_Sources)/crapsc/java
JAVA_Classes = $(Mon_dir)/bin/
JAVA_Dest = $(Mon_dir)/compiled
JAR_Name =CrapsMon5.jar

compile: 
	javac -d $(JAVA_Dest) -cp $(JAVA_Classes) $(crapsc_Sources)/*.java
	javac -d $(JAVA_Dest) -cp $(JAVA_Classes):$(JAVA_Sources) $(craps_Sources)/*.java

archive: compile
	cp -r $(JAVA_Classes)* $(JAVA_Dest) 
	jar cfe $(JAR_Name) org.jcb.craps.CrapsEmu -C $(JAVA_Dest) .
	rm -r $(JAVA_Dest)/*

clean_dest:
	rm  -r $(JAVA_Dest)/*

clean: clean_dest
	rm $(JAR_Name)



