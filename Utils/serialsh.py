#!/usr/bin/env python3
import serial
import time
import re
import os
import struct

s = serial.Serial('/dev/ttyUSB0', 9600, parity=serial.PARITY_ODD, timeout=0)


def parse_obj(f):
    ''' read a .obj file and returns the list of 32 bits words '''
    words = {}
    last_pos = 0

    for line in f.readlines():
        if line.startswith('word '):
            e = line.strip().split()
            pos = int(e[1])
            words[pos] = int(e[2], 2)
            last_pos = max(last_pos, pos)

    return map(lambda p: words[p] if p in words else 0, range(last_pos + 1))


while True:
    try:
        line = input('> ')
    except KeyboardInterrupt:
        line = '\x03' # ^C
    except EOFError:
        break

    # special case for exec command
    if line.startswith('exec '):
        m = re.match(r'^exec\s+(\S+)\.obj$', line)
        if not m:
            print('Error: the file must end with .obj')
            continue
        else:
            path = m.group(1) + '.obj'
            if not os.path.exists(path):
                print('Error: cannot find file %s' % path)
                continue
            else:
                with open(path, 'r') as f:
                    words = list(parse_obj(f))
                    message = struct.pack('>I', len(words)) # length
                    message += b''.join(struct.pack('>I', word) for word in words)
                    s.write(line.encode('ascii') + b'\n')
                    s.write(message)
    else:
        s.write(line.encode('ascii') + b'\n')

    byte = None
    while byte != b'\0': # end of message
        byte = s.read(1)
        if byte == b'\x04':
            exit(0)
        elif byte != b'\0':
            print(byte.decode('ascii'), end='')
