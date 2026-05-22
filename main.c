#include <stdio.h>
#include <stdlib.h>
#include "file.h"
#include "3con.h"
#include "alg.h"
#include "valid.h"
#include "javafile.h"

int main(int argc, char **argv)
{
	if(argc != 4)
	{
		printf("Nie podano odpowiedniej liczby argumentow");
		return 1;
	}
	
	int binary = atoi(argv[1]);
	FILE *file = fopen(argv[2], "r");
	FILE *output = fopen(argv[3], "w");

	if(binary != 0 && binary != 1)
	{
		printf("Trzeci argument musi byc 0 lub 1");
		return 1;
	}

	if(file == NULL)
	{
		printf("Nie znaleziono pliku wejsciowego");
		return 1;
	}

	if(output == NULL)
	{
		printf("Nie znaleziono pliku wyjsciowego");
		return 1;
	}

	int edge_count;
	Edge *edge = parse(file, &edge_count);
	
	if(euler(edge, edge_count) == 1)
	{
		free_edges(edge, edge_count);
		fclose(file);
		fclose(output);
		printf("Graf nie spelnia rownania Eulera");
		return 1;
	}
	
	if(connectivity(edge, edge_count) == 1)
	{
		free_edges(edge, edge_count);
		fclose(file);
		fclose(output);
		printf("Graf nie jest 3-spojny");
		return 1;
	}
	
	tutte(edge, edge_count);
	f_out(edge, output, edge_count, binary);
	write_edges(edge, edge_count);

	fclose(file);
	fclose(output);
	free_edges(edge, edge_count);
	
	return 0;
}