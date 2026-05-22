#include <stdio.h>
#include <stdlib.h>
#include "file.h"
#include "3con.h"
#include "valid.h"

int euler(Edge *edges, int edge_count)
{
	int n;
	int *degree;
	int **adj = build_adjacency(edges, edge_count, &n, &degree);
	int m = edge_count;

	if(n >= 3 && m > 3*n - 6)
	{
		free_adjacency(adj, degree, n);
		return 1;
	}

	free_adjacency(adj, degree, n);
	return 0;
}

int dfs(int n, int **adj, int *degree)
{
	if(n <= 0) 
	{
		return 0;
	}

	int *visited = calloc(n + 1, sizeof(int));
	int *stack = malloc((n + 1) * sizeof(int));
	int top = 0;

	stack[top++] = 1;
	visited[1] = 1;

	while(top > 0)
	{
		int u = stack[--top];
		for(int i = 0; i < degree[u]; i++)
		{
			int v = adj[u][i];
			if(!visited[v])
			{
				visited[v] = 1;
				stack[top++] = v;
			}
		}
	}

	int conn = 1;
	for(int i = 1; i <= n; i++)
	{
		if(!visited[i])
		{
			conn = 0;
			break;
		}
	}

	free(visited);
	free(stack);

	return conn;
}