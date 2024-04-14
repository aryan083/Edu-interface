#include <stdio.h>
#include <stdlib.h>

int main() {
    int num_elements;
    int *arr;

    printf("Enter the number of elements: ");
    scanf("%d", &num_elements);

    // Allocate memory for an integer array using calloc
    arr = (int *)calloc(num_elements, sizeof(int));

    if (arr == NULL) {
        printf("Memory allocation failed.\n");
        return 1;
    }

    printf("Memory allocated successfully using calloc.\n");

    // Free the allocated memory
    free(arr);

    return 0;
}
