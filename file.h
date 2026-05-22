#ifndef FILE_H
#define FILE_H

typedef struct{
	double x;
	double y;
} Coordinates;

typedef struct{
	int id;
	Coordinates cords;
} Point;

typedef struct{
	char *name;
	double weight;
	Point *A;
	Point *B;
} Edge;

Edge *parse(FILE *file, int *out_count);

void f_out(Edge *edge, FILE *file, int edge_count, int binary);

void free_edges(Edge *edges, int edge_count);

#endif