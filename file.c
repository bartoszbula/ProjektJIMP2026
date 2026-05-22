#include <stdio.h>
#include <stdlib.h>
#include "file.h"
#define MAX_EDGES 100

Edge *parse(FILE *file, int *out_count)
{
	Edge *edge = malloc(sizeof(Edge) * MAX_EDGES);
	int count = 0;

	while(count < MAX_EDGES && !feof(file))
	{
		edge[count].name = malloc(sizeof(char) * 2);
		edge[count].A = malloc(sizeof(Point));
		edge[count].B = malloc(sizeof(Point));
		if(fscanf(file, "%s %d %d %lf", edge[count].name, &edge[count].A->id, &edge[count].B->id, &edge[count].weight) == 4)
		{
			count++;
		}
		else
		{
			free(edge[count].name);
			free(edge[count].A);
			free(edge[count].B);
			break;
		}
	}
	*out_count = count;
	return edge;
}

void f_out(Edge *edge, FILE *file, int edge_count, int binary)
{
	if(edge == NULL || file == NULL)
	{
		return;
	}

	int max_id = 0;
	for(int i = 0; i < edge_count; i++)
	{
		if(edge[i].A->id > max_id)
		{
			max_id = edge[i].A->id;
		}
		if(edge[i].B->id > max_id)
		{
			max_id = edge[i].B->id;
		}
	}

	int *visited = calloc(max_id + 1, sizeof(int));

	if(binary == 0)
	{
		for(int i = 0; i < edge_count; i++)
		{
			if(visited[edge[i].A->id] == 0)
			{
				fprintf(file, "%d %f %f\n", edge[i].A->id, edge[i].A->cords.x, edge[i].A->cords.y);
				visited[edge[i].A->id] = 1;
			}
			if(visited[edge[i].B->id] == 0)
			{
				fprintf(file, "%d %f %f\n", edge[i].B->id, edge[i].B->cords.x, edge[i].B->cords.y);
				visited[edge[i].B->id] = 1;
			}
		}
	}

	if(binary == 1)
	{
		for(int i = 0; i < edge_count; i++)
		{
			if(visited[edge[i].A->id] == 0)
			{
				fwrite(&edge[i].A->id, sizeof(int), 1, file);
				fwrite(&edge[i].A->cords.x, sizeof(double), 1, file);
				fwrite(&edge[i].A->cords.y, sizeof(double), 1, file);
				visited[edge[i].A->id] = 1;
			}
			if(visited[edge[i].B->id] == 0)
			{
				fwrite(&edge[i].B->id, sizeof(int), 1, file);
				fwrite(&edge[i].B->cords.x, sizeof(double), 1, file);
				fwrite(&edge[i].B->cords.y, sizeof(double), 1, file);
				visited[edge[i].B->id] = 1;
			}
		}
	}
	free(visited);
}


void free_edges(Edge *edges, int edge_count)
{
	if(edges == NULL)
	{
		return;
	}

	for(int i = 0; i < edge_count; i++)
	{
		free(edges[i].name);
		free(edges[i].A);
		free(edges[i].B);
	}
	free(edges);
}