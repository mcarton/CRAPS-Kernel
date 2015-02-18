char* strcpy(char* dest, char* src)
{
    int i;

    for (i = 0; src[i] != '\0'; i = i+1) {
        dest[i] = src[i];
    }

    return dest;
}

int strlen(char* str) {
    int i = 0;
    while (str[i] != '\0') {
        i = i+1;
    }
    return i;
}

bool strcmp(char* str1, char* str2)
{
    int i = 0;
    while(str1[i] != '\0' && str2[i] != '\0' && str1[i] == str2[i])
    {
        i = i + 1;
    }
    return str1[i] == str2[i];
}

// convert an int to an hexadecimal string like 0xDEADBEEF
void dtoa(int number, char* string) {
    string[0] = '0';
    string[1] = 'x';
    int i = 2;
    int n = number;
    while ((bool)n) {
        n = n >> 4;
        i = i + 1;
    }

    if (i == 2) {
        i = 3; // if number == 0, we print a zero after 0x.
    }

    // i contains the number of chars
    string[i+1] = (char)0;
    while (i > 2) {
        n = number & 0xF;
        if (n < 10) {
            n = n + (int)'0';
        }
        else {
            n = n - 10 + (int)'A';
        }
        i = i - 1;
        string[i] = (char)n;
        number = number >> 4;
    }
}

