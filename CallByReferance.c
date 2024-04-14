#include <stdio.h>

void modifyValue(int *ptr) {
    *ptr = *ptr + 10;
}

int main() {
    int num = 5;
    printf("Before calling modifyValue: num = %d\n", num);

    modifyValue(&num);

    printf("After calling modifyValue: num = %d\n", num);

    return 0;
}

