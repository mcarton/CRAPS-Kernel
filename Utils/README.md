Installation
============

Linux
-----

Firstly, you will need some packages. On Archlinux, simply run
`yaourt adept-runtime adept-utilities`

You may need to reboot in order to reload udev rules.

Then, go to the directory libusbComm, and run `make` to build libusbComm1.3.so

To use commUSB or CrapsMon, you will need to update the Adept device table.
Use `dadutil enum` to list all found devices.

Then, you can add the nexys2 in the device table by running:
`dadutil tbladd --alias nexys2 --conn SN:10054D273494 --dtp USB`

Where 10054D273494 is the serial number of the device.

You can print the device table with `dadutil enum -t`

You should now be able to run CrapsMon and commUSB!

Windows
-------

Just download usbComm1.3.dll from http://diabeto.enseeiht.fr/download/archi/projet_long/javacomm/usbComm1.3.dll
and put it in this directory.
