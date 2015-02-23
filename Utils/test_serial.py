#!/usr/bin/env python3
import serial
import time

s = serial.Serial('/dev/ttyUSB0', 9600, parity=serial.PARITY_ODD, timeout=0)

while True:
    try:
        line = input('> ')
    except KeyboardInterrupt:
        line = '\x03' # ^C

    s.write(line.encode('ascii') + b'\n')
    #for c in line.encode('ascii') + b'\n':
    #    time.sleep(0.01)
    #    s.write(bytes((c,)))
    time.sleep(0.5)

    line = s.readline().decode('ascii').strip()

    if line == '\x04': # ^D
        break
    elif line:
        print(line)
