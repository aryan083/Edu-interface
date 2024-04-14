#include <stdio.h>

int linearSearch(int arr[], int n, int key) {
    for (int i = 0; i < n; i++) {
        if (arr[i] == key) {
            return i; // Return the index where key is found
        }
    }
    return -1; // Return -1 if key is not found
}

int main() {
    int arr[] = {2, 3, 4, 10, 40};
    int n = sizeof(arr) / sizeof(arr[0]);
    int key = 10;

    int result = linearSearch(arr, n, key);
    if (result != -1) {
        printf("Key found at index: %d\n", result);
    } else {
        printf("Key not found\n");
    }

    return 0;
}
