#ifndef VALID_H
#define VALID_H
#include "file.h"

int euler(Edge *edges, int edge_count);

int dfs(int n, int **adj, int *degree);

#endif