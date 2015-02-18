import serial

s = serial.Serial('/dev/ttyUSB0', 9600, parity=serial.PARITY_ODD)

while True:
    s.write(input('> ').encode('ascii') + b'\n')
    line = s.readline()
    print(line.decode('ascii').strip())
