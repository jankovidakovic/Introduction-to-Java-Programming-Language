# Introduction to Java Programming Language

Solutions to the given assignments within the Introduction to Java Programming Language course.

## Course description

Introduction to Java Programming Language is a skill course at Faculty of Electrical Engineering and Computing. Through the extensive usage of object-oriented programming and design patterns, the course covers an abundance of topics such as:
- Java Collection Framework
- Java Generics
- Multithreading and multithreaded applications
- Working with files
- GUI development using Swing and AWT
- Web applications and Web Server development
- Java Servlets and Java Server Pages(JSP)
- Data persistence - JDBC, JPA, Hibernate

Practical application of the course topics is done through the homework assignments, in which small desktop and web applications are implemented within the Java ecosystem, mostly using the test-driven development approach.

## Assignment description
Below is a list of all course assignments and its descriptions. More notable examples are better documented and include usage instructions. Assignments are implemented using the following technologies:
- Java 14
- Maven
- JUnit 5

### Assignment 1 - baby steps

### Assignment 2 - classes

### Assignment 3 - Interpreter implemenation (Classes, Interfaces)

### Assignment 4 - Java Collection Framework Implementation (Generics)

### Assignment 5 - Lindermayer Fractal Producer, Database Emulator
First part of the assignment includes implementing a Lindermayer fractal producer. Configuration parser is implemented for loading the information about the Lindermayer system. Then, a simple turtle graphics system is implemented and the fractal is produced using the turtle commands and course-provided JAR libraries.
Second part is the implementation of a simple database emulator. It's a database containing the data about faculty students, and the data is stored in a text file.  A query parser is implemented, and most notable functionalities include relational and logical operators, regex-based string matching, and indexed searching.

### Assignment 6 - File cryptography, Command-line Shell implementation
First part of assignment includes implementing a digest engine that calculates file digests using the SHA-256 algorithm, and file encryption/decryption engine that works using the AES.
Second part is an implementation of a command-line shell interface. Shell is implemented from scratch, and supports UNIX-like commands such as `cat`, `ls`, `tree`, `copy` and `hexdump`. For more information, check the well-documented code. 

### Assignment 9 - Vectors and Complex Numbers, Newton Fractal producer, 3D Ray Caster implementation (Multithreading)
- Vectors and Complex Numbers
  - a simple mathematical framework is implemented that supports the usage of 3-dimensional vectors, complex numbers and complex polynomials
- Newton Fractal producer 
  - A fractal producer is implemented using Newton-Rhapson iteration of finding the closest complex root, and coloring different regions of a 2D plane according to the index of the closest root. A course-provided JAR is used to draw the fractal, and all the preprocessing and multithreading operations are implemented from scratch.
- 3D Ray Caster
  - A multithreaded ray caster is implemented using the various Java-provided multithreaded frameworks and math libraries implemented in the earlier part of the assignment.    

### Assignment 10 - GUI Calculator (Swing), Bar Chart Drawer (AWT)
- GUI calculator with custom layout manager
  - Using the Java Swing framework, a custom Layout Manager is implemented from scratch, and used to develop a fully-working calculator application. Notable is the extenside use of the command design pattern.
- Bar Chart drawer
  - A configurable bar chart drawer is implemented from scratch in the Java AWT framework.

### Assignment 11 - GUI Notepad++ (Swing)
An advanted notepad is implemented in Java Swing. Besides basic text editing, it supports the following functionalities:
- opening and saving files
- editing multiple files at once using tabs
- status bar indicating current line number, character count, word count, space count
- internationalization and localization, which includes support for Croatian, English and German across the whole application interface
- text manipulation such as lowercasing, uppercasing, sorting and removing duplicate words

### Assignment 12 - Web Server with custom templating engine (Network programming using Java.net)
In this assignment, a simple web-server is implemented from scratch. It supports the following functionalities:
- multithreaded client connections
- session storage
- cookies
- request parameters and attributes
- serving different MIME types (text, images, GIFs)
- custom templating language
The template language, called SmartScript, is an extension of the third assignment, in which the lexer and parser were implemented. In this assignment, the language is upgraded with dynamic type system and an engine that executes the commands.
### Assignment 13 - a Voting Web Application (Servlets, JSP)

### Assignment 14 - a Voting Web Application (JDBC)

### Assignment 15 - a Blog Web Application (JPA)
