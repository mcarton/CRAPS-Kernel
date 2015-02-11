all: archive

JAVA_Sources = src
crapsmon_Sources = $(JAVA_Sources)/org/jcb/craps
crapsc_Sources = $(JAVA_Sources)/org/jcb/craps/crapsc/java
JAVA_Classes = bin
JAVA_Dest = build
JAR_Name = CrapsMon5.jar

build:
	mkdir -p $(JAVA_Dest)

crapsmon: build
	javac -d $(JAVA_Dest) -classpath $(JAVA_Classes):$(JAVA_Sources) $(crapsmon_Sources)/*.java

archive: crapsmon
	cp -r $(JAVA_Classes)/* $(JAVA_Dest)
	jar cfe $(JAR_Name) org.jcb.craps.CrapsEmu -C $(JAVA_Dest) .

clean_dest:
	rm  -rf $(JAVA_Dest)

clean: clean_dest
	rm -f $(JAR_Name)
