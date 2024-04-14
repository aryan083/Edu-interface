#include <stdio.h>

// Recursive function to calculate factorial
int factorial(int n) {
    if (n == 0 || n == 1) {
        return 1;
    } else {
        return n * factorial(n - 1);
    }
}

// Recursive function to calculate Fibonacci number
int fibonacci(int n) {
    if (n == 0) {
        return 0;
    } else if (n == 1) {
        return 1;
    } else {
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}

int main() {
    int num;

    printf("Enter a positive integer: ");
    scanf("%d", &num);

    // Calculate and print factorial
    printf("Factorial of %d is: %d\n", num, factorial(num));

    // Calculate and print Fibonacci number
    printf("Fibonacci number at position %d is: %d\n", num, fibonacci(num));

    return 0;
}

