all: crapsc crapsmon

EGGJAR = eggc-5.3.1.jar
JAVA_Sources = src
crapsmon_Sources = $(JAVA_Sources)/org/jcb/craps
crapsc_Sources = $(JAVA_Sources)/org/jcb/craps/crapsc
crapsc_utils_Sources = $(JAVA_Sources)/org/mmek/craps/crapsc
JAVA_Classes = bin
JAVA_Dest = build

build:
	mkdir -p $(JAVA_Dest)

crapsmon: build crapsc
	javac -d $(JAVA_Dest) -classpath $(EGGJAR):$(JAVA_Classes):$(JAVA_Sources) $(crapsmon_Sources)/*.java

crapsc_egg: build
	cd $(crapsc_Sources) && \
	java -jar ../../../../../$(EGGJAR) CRAPS.egg

crapsc: build crapsc_egg
	javac -d $(JAVA_Dest) -classpath $(EGGJAR):$(JAVA_Sources) $(crapsc_utils_Sources)/*.java $(crapsc_Sources)/java/*.java
	cp $(crapsc_utils_Sources)/messages.properties $(JAVA_Dest)/org/mmek/craps/crapsc
	cp $(crapsc_Sources)/CRAPS.properties $(JAVA_Dest)/org/jcb/craps/crapsc


clean_dest:
	rm  -rf $(JAVA_Dest)

clean_crapsc_egg:
	rm -f $(crapsc_Sources)/CRAPS.properties
	rm -f $(crapsc_Sources)/java/CRAPS.java
	rm -f $(crapsc_Sources)/java/*CRAPSMessages.java
	rm -f $(crapsc_Sources)/java/*LEX_CRAPS.java
	rm -f $(crapsc_Sources)/java/S_*.java
	rm -f $(crapsc_Sources)/java/T_*.java

clean: clean_dest clean_crapsc_egg
