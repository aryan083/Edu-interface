#include <stdio.h>

void modifyValue(int num) {
    num = num + 10;
    printf("Inside modifyValue: num = %d\n", num);
}
void modifyValueByReference(int *ptr) {
    *ptr = *ptr + 10;
}

int main() {
    int num = 5;
    printf("Call By Value \n\n");
	printf("Before calling modifyValue: num = %d\n", num);

    modifyValue(num);

    printf("After calling modifyValue: num = %d\n", num);
    
    printf("\n\n\n\nCall By Reference ");
    
    int num2 = 7;
    printf("Before calling modifyValue: num2 = %d\n", num);

    modifyValueByReference(&num2);

    printf("After calling modifyValue: num2 = %d\n", num);

    return 0;
}

