#ifndef ALG_H
#define ALG_H
#include "file.h"

void tutte(Edge *edge, int e_count);

void triangle(int **adj, int *degree, int n, int *v1, int *v2, int *v3);

#endif