#include <stdio.h>
#include <string.h>

int main() {
    FILE *file;
    char data[100];
    char filename[50];

    // Create a file and write data into it
    printf("Enter the filename to create and write data: ");
    scanf("%s", filename);

    file = fopen(filename, "w");
    if (file == NULL) {
        printf("Error creating the file.\n");
        return 1;
    }

    printf("Enter data to write into the file: ");
    getchar(); // Consume the newline character left in the buffer
    fgets(data, sizeof(data), stdin);
    fprintf(file, "%s", data);

    fclose(file);
    printf("Data successfully written to the file.\n");

    // Read data from the file
    file = fopen(filename, "r");
    if (file == NULL) {
        printf("Error opening the file for reading.\n");
        return 1;
    }

    printf("Data read from the file:\n");
    while (fgets(data, sizeof(data), file) != NULL) {
        printf("%s", data);
    }

    fclose(file);

    return 0;
}

