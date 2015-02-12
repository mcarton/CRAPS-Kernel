all: libcrapsusb.so

JAVA_INCLUDE_DIR = /usr/lib/jvm/java-7-openjdk/include
ADEPT_LIBRARY_DIR = /usr/lib/digilent/adept
ADEPT_JDK_URL = http://www.digilentinc.com/Data/Products/ADEPT2/digilent.adept.sdk_2.3.1.tar.gz

crapsusb.o: crapsusb.cpp adept-sdk
	g++ -c -fPIC -Wall -I$(JAVA_INCLUDE_DIR) -I$(JAVA_INCLUDE_DIR)/linux -Iadept-sdk/inc crapsusb.cpp

libcrapsusb.so: crapsusb.o
	g++ -shared -L$(ADEPT_LIBRARY_DIR) -ldpcutil -o libcrapsusb.so crapsusb.o

adept-sdk:
	curl -fLC - --retry 3 --retry-delay 3 -o adept-sdk.tar.gz $(ADEPT_JDK_URL)
	tar -xf adept-sdk.tar.gz
	mv digilent.adept.sdk_2.3.1 adept-sdk

clean-adept:
	rm -rf adept-sdk

clean:
	rm -f *.o *.so
	rm -f adept-sdk.tar.gz
