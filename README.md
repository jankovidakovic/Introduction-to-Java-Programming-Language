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
Below is a list of all course assignments and its basic descriptions. For a more detailed description of the implementation, feel free to check out the thorough documentation within the source code itself.
Assignments are implemented using the following technologies:
- Java 14
- Maven
- JUnit 5
- Apache Tomcat web container
- Apache Derby database
- Hibernate

### Assignment 1 - First programs
A series of very simple programs are implemented, as a part of introduction to the course. Programs include:
- factorial calculator
- rectangle circumference and area calculator
- simple set implementation using an ordered binary tree
 
### Assignment 2 - Classes
A very simple collection framework is implemented that showcases the extensive use of classes in object-oriented programming. Data structures that are implemented include:
- an abstract Collection
- ArrayIndexedCollection
- LinkedListCollection
- ObjectStack

### Assignment 3 - More classes and interfaces
- Collection framework upgrade
  - Framework from the previous assignment is upgraded. Some classes are turned into interfaces, and iterators are implemented for some collections. 
- Interpreter lexer and parser
  - A fully-functionsl lexer and parser is implemented for the custom-made SmartScript programming language. Lexer supports tokenization using finite automatons and different lexer states, while parser organizes the tokens into a syntax tree.
   
### Assignment 4 - Generics
- Collection framework parametrization
  - from the previous assignment is finally upgraded completely with Java Generics. All collections are parametrized, with careful and approproate use of `extends` and `super` keywords.
- Simple map implementation
  - A simple map is implemented as a part of the collection framework, and the implementation is done using the adapter design patter, with the `ArrayIndexedCollection` as inner collection.
- More advanced map implementation
  - A more general hash map is implemented, with an array of map entries as an inner collection and a simple hashing algorithm for indexing the collection. Dynamic size management is also implemented, as well as fully functioning map iterator, with proper usage of `ConcurrentModificationException`.
  -   
### Assignment 5 - Java Collection Framework
- Lindermayer Fractal Producer
  - Implementation of Lindermayer fractal producer. Configuration parser is implemented for loading the information about the Lindermayer system. Then, a simple turtle graphics system is implemented and the fractal is produced using the turtle commands and course-provided JAR libraries.
- Database Emulator
  - Implementation of a simple database emulator. It's a database containing the data about faculty students, and the data is stored in a text file.  A query parser is implemented, and most notable functionalities include relational and logical operators, regex-based string matching, and indexed searching.

### Assignment 6 - Working with files
- File cryptography
  - A file digest engine is implemented, that produces file digests by reading file bytes and applying the SHA-256 algorithm.
  - File encryption and decryption is implemented, that uses the AES to encrypt/decrypt files.
- Command-line shell interface
  - A simple shell that can execute some basic filesystem commands is implemented from scratch. It supports UNIX-like commands such as `cat`, `ls`, `tree`, `copy` and `hexdump`. For more information, check the well-documented code. 

### Assignment 9 - Multithreading
- Vectors and Complex Numbers
  - a simple mathematical framework is implemented that supports the usage of 3-dimensional vectors, complex numbers and complex polynomials
- Newton Fractal producer 
  - A fractal producer is implemented using Newton-Rhapson iteration of finding the closest complex root, and coloring different regions of a 2D plane according to the index of the closest root. A course-provided JAR is used to draw the fractal, and all the preprocessing and multithreading operations are implemented from scratch.
- 3D Ray Caster
  - A multithreaded ray caster is implemented using the various Java-provided multithreaded frameworks and math libraries implemented in the earlier part of the assignment.    

### Assignment 10 - Swing, AWT
- GUI calculator with custom layout manager
  - Using the Java Swing framework, a custom Layout Manager is implemented from scratch, and used to develop a fully-working calculator application. Notable is the extenside use of the command design pattern.
- Bar Chart drawer
  - A configurable bar chart drawer is implemented from scratch in the Java AWT framework.

### Assignment 11 - Advanced Swing
An advanted notepad is implemented in Java Swing. Besides basic text editing, it supports the following functionalities:
- opening and saving files
- editing multiple files at once using tabs
- status bar indicating current line number, character count, word count, space count
- internationalization and localization, which includes support for Croatian, English and German across the whole application interface
- text manipulation such as lowercasing, uppercasing, sorting and removing duplicate words

### Assignment 12 - Network applications
In this assignment, a simple web-server is implemented from scratch. It supports the following functionalities:
- multithreaded client connections
- session storage
- cookies
- request parameters and attributes
- serving different MIME types (text, images, GIFs)
- file-based configuration
- custom templating language
The template language, called SmartScript, is an extension of the third assignment, in which the lexer and parser were implemented. In this assignment, the language is upgraded with dynamic type system and an engine that executes the commands.

### Assignment 13 - Web applications (Servlets, JSP)
Servlets and JSP are used to develop a simple web application with various features, implemented as separate Java Server Pages. Application is deployed inside Apache Tomcat container. Application features include:
- background color chooser
- table of sine and cosine values for the range specified in URL parameters
- a funny story with a randomly chosen text color
- pie chart generator
- integer powers of a range of numbers, generated in the form of XLS file
- application running time information
- a simple voting application, with results dynamically displayed as a bar chart and available as an XLS file

### Assignment 14 - Web applications (JDBC)
Voting application from the previous assignment is extended so that the data is stored in the database. Apache Derby is used, and is managed from within the application using the JDBC.

### Assignment 15 - Web applications (JPA)
A simple multiple-user blog application is implemented from scratch. Application supports multiple user sessions and role-based content access. It is deployed within Apache Tomcat container, and uses the JPA and Hibernate to persist the data in the Apache Derby database.
