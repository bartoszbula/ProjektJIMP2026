CC = gcc
CFLAGS = -Wall -Wextra -std=c11
LDFLAGS = -lm

OBJ = main.o file.o alg.o 3con.o valid.o
TARGET = graf.exe

all: $(TARGET)

$(TARGET): $(OBJ)
	$(CC) -o $@ $^ $(LDFLAGS)

%.o: %.c
	$(CC) -c -o $@ $< $(CFLAGS)

clean:
	del $(OBJ) $(TARGET)