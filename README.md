# CPSC213

This repository contains all of the code that I created (or worked in a team to create) at the University of British Columbia for the course CPSC213: Introduction to Computer Systems, from January 2023 to April 2023. The projects are in a mix of Java, C, and an assembly like language called SM213 that was used in the course. The ISA for SM213 can be found [here](https://www.cs.ubc.ca/~tmm/courses/213-12F/resources/isa.pdf).

## A1

Assignment 1 focused on finding the endianness of any given number. Skeleton functions were given to me, and I implemented all of the functions in both [Endianness.java](/a1/Endianness.java) and [HelloWorld.java](a1/HelloWorld.java). All code in this assignment was written by me. The final task of Assignment 1 was to implement the main memory of our assembly simulator in the file [MainMemory.java](sm213/arch/sm213/machine/student/MainMemory.java).

## A2

In Assignment 2, I began working with assembly code (SM213). I was given programs in C and tasked with converting them into assembly code. The first part, [swap.s](a2/swap.s) swaps two array elements. The second part, [math.s](a2/math.s) performs various math operations. The final assembly part, [calc.s](a2/calc.s) combines elements from swap and math, performing various mathematical operations to a value gotten from an array. The final task of Assignment 2 was to implement various CPU functions in the file [cpu.java](sm213/arch/sm213/machine/student/CPU.java).
