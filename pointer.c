#include <stdio.h>

int main() {
    int num = 10;
    int *ptr;

    // Assigning the address of num to the pointer ptr
    ptr = &num;

    // Printing the value of num and the address of num
    printf("Value of num: %d\n", num);
    printf("Address of num: %p\n", &num);

    // Printing the value of ptr (the address of num)
    printf("Value of ptr (address of num): %p\n", ptr);

    // Accessing the value of num using the pointer
    printf("Value of num using pointer: %d\n", *ptr);

    // Modifying the value of num using the pointer
    *ptr = 20;
    printf("Modified value of num: %d\n", num);

    return 0;
}

