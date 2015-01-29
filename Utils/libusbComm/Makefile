all: libusbComm1.3.so

JAVA_INCLUDE_DIR = /usr/lib/jvm/java-7-openjdk/include
ADEPT_LIBRARY_DIR = /usr/lib/digilent/adept
ADEPT_JDK_URL = http://www.digilentinc.com/Data/Products/ADEPT2/digilent.adept.sdk_2.3.1.tar.gz

usbComm.o: usbComm.cpp adept-sdk
	g++ -c -fPIC -I$(JAVA_INCLUDE_DIR) -I$(JAVA_INCLUDE_DIR)/linux -Iadept-sdk/inc usbComm.cpp

libusbComm1.3.so: usbComm.o
	g++ -shared -L$(ADEPT_LIBRARY_DIR) -ldpcutil -o libusbComm1.3.so usbComm.o

adept-sdk:
	curl -fLC - --retry 3 --retry-delay 3 -o adept-sdk.tar.gz $(ADEPT_JDK_URL)
	tar -xf adept-sdk.tar.gz
	mv digilent.adept.sdk_2.3.1 adept-sdk

clean:
	rm -f *.o *.so
	rm -f adept-sdk.tar.gz
	rm -rf adept-sdk
