#include <stdio.h>
#include <stdlib.h>
#include "file.h"

void write_edges(Edge *edges, int edge_count)
{
	if(edges == NULL)
	{
		return;
	}

	FILE *file = fopen("edge.txt", "w");
	if(file == NULL)
	{
		printf("Nie mozna utworzyc pliku krawedzie.txt\n");
		return;
	}

	for(int i = 0; i < edge_count; i++)
	{
		fprintf(file, "%d %d\n", edges[i].A->id, edges[i].B->id);
	}

	fclose(file);
}