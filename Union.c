#include <stdio.h>

union Data {
    int i;
    float f;
    char str[20];
};

int main() {
    union Data data;

    data.i = 10;
    printf("Value of integer data: %d\n", data.i);

    data.f = 3.14;
    printf("Value of float data: %.2f\n", data.f);

    strcpy(data.str, "Hello");
    printf("Value of string data: %s\n", data.str);

    printf("Size of union Data: %lu bytes\n", sizeof(data));

    return 0;
}

