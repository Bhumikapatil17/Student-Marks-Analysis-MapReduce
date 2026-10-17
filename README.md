# Student Marks Analysis Using Hadoop MapReduce

## Project Description

This project analyzes student marks using Hadoop MapReduce. The system processes student marks stored in a CSV file and calculates the total marks, average marks, highest marks, lowest marks, and grade for each student.

## Objectives

- Analyze student academic performance
- Calculate total marks for each student
- Calculate average marks
- Find highest and lowest marks
- Assign grades based on average marks
- Demonstrate Hadoop MapReduce processing

## Technologies Used

- Java
- Apache Hadoop
- Hadoop MapReduce
- HDFS

## Input Dataset

The input file `student_marks.csv` contains:

- Student ID
- Student Name
- Subject
- Marks

## How It Works

The Mapper reads student records and sends the student name as the key and marks as the value.

The Shuffle and Sort phase groups all marks belonging to the same student.

The Reducer processes the grouped marks and calculates the total, average, highest marks, lowest marks, and grade.

## Output

The program generates student-wise:

- Total Marks
- Average Marks
- Highest Marks
- Lowest Marks
- Grade

## Project Files

- `StudentMarksAnalysis.java` – Hadoop MapReduce source code
- `student_marks.csv` – Input dataset
- `README.md` – Project documentation
