#compile C program
gcc -o pizza p3210150-p3210122-pizzeria.c -lm -pthread

#execute the compiled program with arguments 100 and 1000
./pizza 100 1000