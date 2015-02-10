EGGJAR=eggc-5.3.1.jar
JDIR=/usr/bin
JAVA=$(JDIR)/java
JAVAC=$(JDIR)/javac

all: egg att

egg:
	cd org/jcb/craps/crapsc && \
	$(JAVA) -jar ../../../../$(EGGJAR) CRAPS.egg

att:
	$(JAVAC) -classpath $(EGGJAR):. org/mmek/craps/crapsc/*.java org/jcb/craps/crapsc/java/*.java

clean:
	rm -f org/jcb/craps/crapsc/CRAPS.properties
	rm -f org/jcb/craps/crapsc/java/CRAPS.java
	rm -f org/jcb/craps/crapsc/java/CRAPSMessages.java
	rm -f org/jcb/craps/crapsc/java/ICRAPSMessages.java
	rm -f org/jcb/craps/crapsc/java/*LEX_CRAPS.java
	rm -f org/jcb/craps/crapsc/java/S_*.java
	rm -f org/jcb/craps/crapsc/java/T_*.java
	rm -f org/jcb/craps/crapsc/java/*.class
	rm -f org/mmek/craps/crapsc/*.class
