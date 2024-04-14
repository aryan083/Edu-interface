#include <stdio.h>

// Function with no arguments and no return value
void greet() {
    printf("Hello! Welcome to the world of functions!\n");
}

// Function with arguments and no return value
void addNumbers(int num1, int num2) {
    int sum = num1 + num2;
    printf("The sum of %d and %d is: %d\n", num1, num2, sum);
}

// Function with arguments and a return value
int multiplyNumbers(int num1, int num2) {
    return num1 * num2;
}

int main() {
    // Calling functions
    greet();
    
    int num1 = 10, num2 = 5;
    addNumbers(num1, num2);

    int result = multiplyNumbers(num1, num2);
    printf("The product of %d and %d is: %d\n", num1, num2, result);

    return 0;
}

