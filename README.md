# CPSC213

This repository contains all of the code that I created (or worked in a team to create) at the University of British Columbia for the course CPSC213: Introduction to Computer Systems, from January 2023 to April 2023. The projects are in a mix of Java, C, and an assembly like language called SM213 that was used in the course. The ISA for SM213 can be found [here](https://www.cs.ubc.ca/~tmm/courses/213-12F/resources/isa.pdf).

## A1

Assignment 1 focused on finding the endianness of any given number. Skeleton functions were given to me, and I implemented all of the functions in both [Endianness.java](/a1/Endianness.java) and [HelloWorld.java](a1/HelloWorld.java). All code in this assignment was written by me. The final task of Assignment 1 was to implement the main memory of our assembly simulator in the file [MainMemory.java](sm213/arch/sm213/machine/student/MainMemory.java).

## A2

In Assignment 2, I began working with assembly code (SM213). I was given programs in C and tasked with converting them into assembly code. The first part, [swap.s](a2/swap.s) swaps two array elements. The second part, [math.s](a2/math.s) performs various math operations. The final assembly part, [calc.s](a2/calc.s) combines elements from swap and math, performing various mathematical operations to a value gotten from an array. The final task of Assignment 2 was to implement various CPU functions in the file [cpu.java](sm213/arch/sm213/machine/student/CPU.java).

## A3

From Assignment 3 onwards, I worked in a group with another student. In A3, I worked on converting a [given bubble sort algorithm in C](/a3/bubble_sort_static.c), to use dynamic arrays in [bubble_sort_dynamic.c](/a3/bubble_sort_dynamic.c), and then eventually to only use pointer arithmetic and no square brackets in [bubble_sort_awesome.c](/a3/bubble_sort_awesome.c). All other work in a3 was done by my partner.

## A4

In A4, I worked on [Part 2](/a4/p2) and [Part 3](/a4/p3). My partner completed [Part 1](/a4/p1). In Part 2, I converted a Java program that implemented a simple binary tree ([BinaryTree.java](/a4/p2/BinaryTree.java)) to a C program that had the exact same functionality, [BinaryTree.c](/a4/p2/BinaryTree.c). In Part 3, the main goal was to convert a C program, [q3.c](a4/p3/q3.c), into an equivalent assembly program [q3.s](a4/p3/q3.s). The C program only assigned values to variables, but used multiple levels of structs, arrays and pointers.

## A5

In A5, I completed an implementation of C's `malloc` function. My partner completed all other parts. My implementation is in [myheap.c](a5/myheap.c). The following 5 functions were implemented by me: `myheap_malloc`, `myheap_free`, `get_size_to_allocate`, `coalesce`, and `split_and_mark_used`. These functions simulate how malloc works in C, storing, freeing, and splitting memory blocks on the heap as needed.
