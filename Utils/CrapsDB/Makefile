all: crapsusb/libcrapsusb.so crapsc crapsusb crapsdb

EGGJAR = eggc-5.3.1.jar
JAVA_Sources = src
crapsusb_Sources = $(JAVA_Sources)/org/mmek/craps/crapsusb
crapsdb_Sources = $(JAVA_Sources)/org/mmek/craps/crapsdb
crapsc_Sources = $(JAVA_Sources)/org/jcb/craps/crapsc
JAVA_Dest = build

build:
	mkdir -p $(JAVA_Dest)

crapsc: build
	javac -d $(JAVA_Dest) $(crapsc_Sources)/java/*.java

crapsusb: build
	javac -d $(JAVA_Dest) -classpath $(JAVA_Sources) $(crapsusb_Sources)/*.java

crapsdb: build
	javac -d $(JAVA_Dest) -classpath $(JAVA_Sources) $(crapsdb_Sources)/*.java

crapsusb/libcrapsusb.so:
	cd crapsusb && make

clean_dest:
	rm  -rf $(JAVA_Dest)

clean_libcrapsusb:
	cd crapsusb && make clean

clean: clean_dest clean_libcrapsusb
