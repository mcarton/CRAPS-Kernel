export int task1() {
    int *rs232 = (int*) 0xD0000001;
    while (true) {
        /*
        int got = (int)getc();
        if (got != -1) {
            *rs232 = got;
        }
        */
    }
}

export int task2() {
    int *ssegs = (int*) 0xA0000000;
    while (true) {
        *(ssegs + 1) = 0b1111;
        *ssegs = rs232_read_buf_end-rs232_read_buf_begin;
    }
}

#include "shell.moc"

export int task3() {
    while (true) {
        char* line = getline();
        shell(line);
        free((void*)line);
    }
}

export int task4() {
    int *ssegs = (int*) 0xA0000000;
    *(ssegs + 1) = 0b1111;
    while (true) {
        void* i = malloc(2);
        void* old_i = i;
        *((int*)i) = 7;
        *((int*)i+1) = 6;
        i = realloc(i, 6);
        if(memcmp(i, old_i, 2))
        {
            *(ssegs + 1) = 0b1111;
        }
        else
        {
            *(ssegs + 1) = 0b0000;
        }
        *ssegs = (int)i;
        free(i);
        i = malloc(10);
        i = realloc(i, 5);
        void* j = malloc(10);
        free(i);
        void* k = malloc(5);
        free(j);
        free(k);
    }
}
