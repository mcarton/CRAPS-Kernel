char[256] rs232_read_buf;
int rs232_read_buf_begin;
int rs232_read_buf_end;
asm("
rs232_handler_read:
    push %r1
    push %r2
    push %r3

    push %r28
    call f_rs232_handler_read
    pop %r28

    pop %r3
    pop %r2
    pop %r1
    reti
")

export void rs232_handler_read() {
    rs232_read_buf[rs232_read_buf_end] = *((char*)0xD0000000);

    rs232_read_buf_end = (rs232_read_buf_end+1) & 0xff;

    rs232_read_buf_begin = rs232_read_buf_begin
                         + (int)(rs232_read_buf_begin == rs232_read_buf_end);
}

char getc() {
    if (rs232_read_buf_begin == rs232_read_buf_end) {
        return (char)-1;
    }
    else {
        char result = rs232_read_buf[rs232_read_buf_begin];
        rs232_read_buf_begin = (rs232_read_buf_begin+1) & 0xff;
        return result;
    }
}

char* getline() {
    int i;
    char c = (char)-1;
    int size = 32;
    char* buf = (char*)malloc(size);
    char* new_buf;

    for (i = 0; c != '\n' && c != '\0'; i=i+1) {
        c = getc();

        if ((int)c == -1) {
            continue;
        }

        if (i == size) {
            size = size*2;
            buf = (char*)realloc((void*)buf, size);
        }

        buf[i] = c;
    }

    buf[i-1] = '\0';

    return buf;
}

void putc(char c) {
    *((char*)0xD0000001) = c;

    int i;
    for(i = 0; i < 500; i=i+1) {
        // wait for the byte to be sent
    }
}

void write(char* string) {
    int i;
    for(i = 0; (bool)string[i]; i=i+1) {
        putc(string[i]);
    }
}
