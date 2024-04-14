#include <stdio.h>
#include <stdlib.h>

int main() {
    int initial_size = 5;
    int new_size = 10;
    int *arr;

    // Allocate memory for an integer array using malloc
    arr = (int *)malloc(initial_size * sizeof(int));

    if (arr == NULL) {
        printf("Memory allocation failed.\n");
        return 1;
    }

    printf("Memory allocated successfully using malloc.\n");

    // Reallocate memory for the integer array using realloc
    arr = (int *)realloc(arr, new_size * sizeof(int));

    if (arr == NULL) {
        printf("Memory reallocation failed.\n");
        return 1;
    }

    printf("Memory reallocated successfully using realloc.\n");

    // Free the allocated memory
    free(arr);

    return 0;
}
