#include <stdio.h>

int main() {
    // Integer data types
    char charVar = 'A';
    int intVar = 1234;
    short shortVar = 100;
    long longVar = 1234567890L;
    long long longLongVar = 123456789012345LL;

    // Floating-point data types
    float floatVar = 3.14f;
    double doubleVar = 3.14159;
    long double longDoubleVar = 3.141592653589793238L;

    // Other data types
    _Complex complexVar = 1 + 2i;
    _Imaginary imaginaryVar = 2i;

    // Printing the values of different data types
    printf("Character Variable (char): %c\n", charVar);
    printf("Integer Variable (int): %d\n", intVar);
    printf("Short Integer Variable (short): %hd\n", shortVar);
    printf("Long Integer Variable (long): %ld\n", longVar);
    printf("Long Long Integer Variable (long long): %lld\n", longLongVar);
    printf("Floating-Point Variable (float): %f\n", floatVar);
    printf("Double Variable (double): %lf\n", doubleVar);
    printf("Long Double Variable (long double): %Lf\n", longDoubleVar);
    printf("Complex Variable (_Complex): %f + %fi\n", creal(complexVar), cimag(complexVar));
    printf("Imaginary Variable (_Imaginary): %fi\n", cimag(imaginaryVar));

    return 0;
}

