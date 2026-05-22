#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include "file.h"
#include "3con.h"
#include "alg.h"
#define ITERATIONS 1000

void triangle(int **adj, int *degree, int n, int *v1, int *v2, int *v3)
{
	*v1 = 1;
	*v2 = 2;
	*v3 = 3;

	for(int u = 1; u <= n; u++)
	{
		for(int i = 0; i < degree[u]; i++)
		{
			int v = adj[u][i];
			for(int j = 0; j < degree[v]; j++)
			{
				int w = adj[v][j];
				if(w == u)
				{
					continue;
				}

				for(int k = 0; k < degree[u]; k++)
				{
					if(adj[u][k] == w)
					{
						*v1 = u;
						*v2 = v;
						*v3 = w;
						return;
					}
				}
			}
		}
	}
}

void tutte(Edge *edge, int e_count)
{
	int n;
	int *degree;
	int **adj = build_adjacency(edge, e_count, &n, &degree);

	if(n < 3)
	{
		free_adjacency(adj, degree, n);
		return;
	}

	int v1, v2, v3;
	triangle(adj, degree, n, &v1, &v2, &v3);

	double *x = calloc(n + 1, sizeof(double));
	double *y = calloc(n + 1, sizeof(double));
	double *new_x = calloc(n + 1, sizeof(double));
	double *new_y = calloc(n + 1, sizeof(double));

	x[v1] = 0.0;	y[v1] = 0.0;
	x[v2] = 1.0;	y[v2] = 0.0;
	x[v3] = 0.5;	y[v3] = sqrt(3.0) / 2.0;

	for(int iter = 0; iter < ITERATIONS; iter++)
	{
		for(int i = 1; i <= n; i++)
		{
			if(i == v1 || i == v2 || i == v3)
			{
				new_x[i] = x[i];
				new_y[i] = y[i];
				continue;
			}

			double sum_x = 0.0;
			double sum_y = 0.0;

			for(int k = 0; k < degree[i]; k++)
			{
				int neighbor = adj[i][k];
				sum_x += x[neighbor];
				sum_y += y[neighbor];
			}

			if(degree[i] > 0)
			{
				new_x[i] = sum_x / degree[i];
				new_y[i] = sum_y / degree[i];
			}
		}
		double *tmp_x = x; x = new_x; new_x = tmp_x;
		double *tmp_y = y; y = new_y; new_y = tmp_y;
	}

	for(int i = 0; i < e_count; i++)
	{
		edge[i].A->cords.x = x[edge[i].A->id];
		edge[i].A->cords.y = y[edge[i].A->id];
		edge[i].B->cords.x = x[edge[i].B->id];
		edge[i].B->cords.y = y[edge[i].B->id];
	}

	free(x);
	free(y);
	free(new_x);
	free(new_y);
	free_adjacency(adj, degree, n);
}