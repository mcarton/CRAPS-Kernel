import serial

s = serial.Serial('/dev/ttyUSB0', 9600, parity=serial.PARITY_ODD)

while True:
    try:
        line = input('> ')
    except KeyboardInterrupt:
        line = '\x03' # ^C

    s.write(line.encode('ascii') + b'\n')

    line = s.readline().decode('ascii').strip()

    if line == '\x04': # ^D
        break
    else:
        print(line)
