#include <stdio.h>

int main() {
    int num1, num2;
    
    printf("Enter the first integer: ");
    scanf("%d", &num1);
    
    printf("Enter the second integer: ");
    scanf("%d", &num2);

    // Arithmetic operators
    printf("Arithmetic Operators:\n");
    printf("%d + %d = %d\n", num1, num2, num1 + num2);
    printf("%d - %d = %d\n", num1, num2, num1 - num2);
    printf("%d * %d = %d\n", num1, num2, num1 * num2);
    printf("%d / %d = %d\n", num1, num2, num1 / num2);
    printf("%d %% %d = %d\n", num1, num2, num1 % num2);

    // Relational operators
    printf("Relational Operators:\n");
    printf("%d == %d: %d\n", num1, num2, num1 == num2);
    printf("%d != %d: %d\n", num1, num2, num1 != num2);
    printf("%d > %d: %d\n", num1, num2, num1 > num2);
    printf("%d < %d: %d\n", num1, num2, num1 < num2);
    printf("%d >= %d: %d\n", num1, num2, num1 >= num2);
    printf("%d <= %d: %d\n", num1, num2, num1 <= num2);

    // Logical operators
    printf("Logical Operators:\n");
    printf("%d && %d: %d\n", num1, num2, num1 && num2);
    printf("%d || %d: %d\n", num1, num2, num1 || num2);
    printf("!%d: %d\n", num1, !num1);
    printf("!%d: %d\n", num2, !num2);

    // Bitwise operators
    printf("Bitwise Operators:\n");
    printf("%d & %d = %d\n", num1, num2, num1 & num2);
    printf("%d | %d = %d\n", num1, num2, num1 | num2);
    printf("%d ^ %d = %d\n", num1, num2, num1 ^ num2);
    printf("~%d = %d\n", num1, ~num1);
    printf("~%d = %d\n", num2, ~num2);
    printf("%d << 1 = %d\n", num1, num1 << 1);
    printf("%d >> 1 = %d\n", num1, num1 >> 1);

    // Increment and decrement operators
    printf("Increment and Decrement Operators:\n");
    printf("++%d = %d\n", num1, ++num1);
    printf("--%d = %d\n", num2, --num2);
    printf("%d++ = %d\n", num1, num1++);
    printf("%d-- = %d\n", num2, num2--);

    return 0;
}
