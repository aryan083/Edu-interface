#include <stdio.h>

int main() {
    int num;

    printf("Enter a number: ");
    scanf("%d", &num);

    // Simple if statement
    if (num > 0) {
        printf("%d is a positive number.\n", num);
    }

    // If-else statement
    if (num % 2 == 0) {
        printf("%d is an even number.\n", num);
    } else {
        printf("%d is an odd number.\n", num);
    }

    // Nested if-else statement
    if (num >= 0) {
        if (num == 0) {
            printf("The number is zero.\n");
        } else {
            printf("The number is positive.\n");
        }
    } else {
        printf("The number is negative.\n");
    }

    // Cascaded if-else statement
    if (num > 100) {
        printf("%d is greater than 100.\n", num);
    } else if (num > 50) {
        printf("%d is greater than 50 but less than or equal to 100.\n", num);
    } else if (num > 0) {
        printf("%d is greater than 0 but less than or equal to 50.\n", num);
    } else {
        printf("%d is less than or equal to 0.\n", num);
    }

    return 0;
}
