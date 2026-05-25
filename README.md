# University Admission Portal

A comprehensive Java console-based application for managing university admissions following Object-Oriented Programming (OOP) and SOLID principles.

## Project Overview

The University Admission Portal is a modular, scalable, and beginner-friendly application designed to manage student admissions across multiple engineering colleges and branches. The system demonstrates professional software architecture with proper separation of concerns, interfaces, and clean code practices.

## Features

### 1. Main Menu
- **New Admission**: Register new students
- **View Student Details**: Search and display student information
- **View All Students**: Display all admitted students in tabular format
- **View Branch Capacity**: Check available seats in each branch
- **Exit**: Close the application

### 2. Colleges
The system manages 4 Engineering Colleges:
- **AEC** - Assam Engineering College
- **JEC** - Jorhat Engineering College

### 3. Branches
Each college offers 4 branches with predefined capacities:
- **CSE** (Computer Science Engineering) - 60 seats
- **BVEC** - Bineswar Brahma Engineering College
- **GEC** - Golaghat Engineering College
- **ECE** (Electronics and Communication Engineering) - 60 seats
- **ME** (Mechanical Engineering) - 45 seats
- **CE** (Civil Engineering) - 45 seats

### 4. Semesters
Each branch offers 8 semesters:
- Semester 1-8 with corresponding year and term information

### 5. Student Admission
- Validates student ID uniqueness
- Checks branch seat availability
- Auto-generates admission numbers
- Updates remaining seats after successful admission
- Prevents admission if branch is full

### 6. Additional Features
- Input validation for age, ID, and menu choices
- Formatted console output
- Exception handling
- Duplicate student ID prevention
- Comprehensive student search functionality

## Project Structure

```
UniversityAdmissionPortal/
│
├── src/
│   └── com/university/
│       ├── Main.java                          (Entry point)
│       │
│       ├── models/
│       │   ├── Semester.java                  (Semester model)
│       │   ├── Student.java                   (Student model)
│       │   └── Branch.java                    (Branch model)
│       │
│       ├── colleges/
│       │   ├── College.java                   (Abstract base class)
│       │   ├── AECCollege.java               (AEC implementation)
│       │   ├── JECCollege.java               (JEC implementation)
│       │   ├── BVECCollege.java              (BVEC implementation)
│       │   └── GECCollege.java               (GEC implementation)
│       │
│       ├── interfaces/
│       │   ├── Searchable.java               (Search interface)
│       │   ├── Displayable.java              (Display interface)
│       │   ├── Admissible.java               (Admission interface)
│       │   └── CapacityManageable.java       (Capacity interface)
│       │
│       ├── repositories/
│       │   ├── StudentRepository.java        (Repository interface)
│       │   └── StudentRepositoryImpl.java     (Repository implementation)
│       │
│       └── services/
│           ├── AdmissionService.java         (Admission logic)
│           └── MenuService.java              (Menu and UI logic)
│
├── bin/                                       (Compiled classes)
└── README.md                                  (This file)
```

## OOP Principles Implemented

### 1. Encapsulation
- Private fields with public getter/setter methods
- Data hiding and controlled access

### 2. Inheritance
- Abstract `College` class with concrete implementations
- Subclasses: `AECCollege`, `JECCollege`, `BVECCollege`, `GECCollege`

### 3. Polymorphism
- Method overriding: `display()`, `toString()`
- Interface implementations in multiple classes

### 4. Abstraction
- Abstract `College` class with abstract methods
- Interface-based contracts for components

### 5. Composition
- `College` contains `Branch` objects
- `Branch` contains `Semester` objects
- Services depend on repositories

## SOLID Principles Implementation

### Single Responsibility Principle (SRP)
- **Student**: Represents student data
- **College**: Manages college information
- **Branch**: Handles branch capacity and operations
- **Semester**: Represents semester information
- **AdmissionService**: Handles admission logic only
- **StudentRepository**: Handles data persistence only
- **MenuService**: Handles user interface only

### Open/Closed Principle (OCP)
- Abstract `College` class allows adding new colleges without modifying existing code
- New college implementations can be added by extending the abstract class

### Liskov Substitution Principle (LSP)
- All college implementations can be substituted for the abstract `College` class
- Maintains the contract of the parent class

### Interface Segregation Principle (ISP)
- Separate interfaces for different concerns:
  - `Searchable`: For searchable objects
  - `Displayable`: For displayable objects
  - `Admissible`: For admissible objects
  - `CapacityManageable`: For capacity management

### Dependency Inversion Principle (DIP)
- High-level services depend on `StudentRepository` interface
- `AdmissionService` doesn't depend on concrete `StudentRepositoryImpl`
- `MenuService` depends on `AdmissionService` interface

## Technical Requirements Met

✅ Proper package structure
✅ ArrayList collections for data storage
✅ Comprehensive comments and documentation
✅ Exception handling for invalid inputs
✅ Input validation for menu choices and data
✅ Scanner for user input
✅ Formatted console output
✅ Core Java only (no external libraries)
✅ No database required
✅ Auto-generated admission numbers
✅ Duplicate student ID prevention
✅ Getter/setter methods
✅ Overridden toString() methods
✅ Modular folder structure
✅ Beginner-friendly code

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Terminal or Command Prompt

### Compilation

Navigate to the project directory and compile all Java files:

```bash
cd "C:\Users\MEHEBUB\Desktop\Lab\JAVA\UniversityAdmissionPortal"

# Using PowerShell
$files = Get-ChildItem -Path src -Recurse -Filter *.java -Name
javac -d bin @($files.ForEach({"src\$_"}))
```

### Running the Application

```bash
java -cp bin com.university.Main
```

## Usage Example

### New Admission
1. Select option `1` from the main menu
2. Enter Student ID (e.g., 101)
3. Enter Name (e.g., Rahul Kumar)
4. Enter Age (e.g., 20)
5. Enter Gender (e.g., Male)
6. Select College (1-4)
7. Select Branch (1-4)
8. Select Semester (1-8)
9. If successful, admission confirmation is displayed
10. If branch is full, admission is rejected

### View All Students
1. Select option `3` from the main menu
2. All admitted students are displayed in tabular format

### View Branch Capacity
1. Select option `4` from the main menu
2. Select a college
3. Capacity information for all branches is displayed

### View Student Details
1. Select option `2` from the main menu
2. Enter the Student ID to search
3. Student details are displayed if found

## Sample Output

```
========================================
  Welcome to University Admission Portal
========================================
1. New Admission
2. View Student Details
3. View All Students
4. View Branch Capacity
5. Exit
========================================

Enter Choice: 1

========== New Admission ==========
Enter Student ID: 101
Enter Name: Rahul Kumar
Enter Age: 20
Enter Gender: Male

Select College:
1. AEC - Assam Engineering College
2. JEC - Jorhat Engineering College
3. BVEC - Bineswar Brahma Engineering College
4. GEC - Golaghat Engineering College
Enter Choice: 3

Select Branch:
1. Computer Science Engineering (CSE) (Available Seats: 60)
2. Electronics and Communication Engineering (ECE) (Available Seats: 60)
3. Mechanical Engineering (ME) (Available Seats: 45)
4. Civil Engineering (CE) (Available Seats: 45)
Enter Choice: 1

Select Semester:
1. Semester 1
2. Semester 2
...
8. Semester 8
Enter Choice: 1

========== Admission Successful! ==========
======== Student Details ========
Admission Number: 1001
Student ID: 101
Name: Rahul Kumar
Age: 20
Gender: Male
College: BVEC - Bineswar Brahma Engineering College
Branch: Computer Science Engineering (CSE)
Semester: Semester 1
==================================
Remaining Seats in Computer Science Engineering (CSE): 59
==========================================
```

## Class Descriptions

### Model Classes

#### `Student.java`
- Represents a student with personal and academic information
- Implements `Displayable` and `Searchable` interfaces
- Auto-generates admission numbers
- Provides getter/setter methods for all attributes

#### `Semester.java`
- Represents an academic semester
- Contains semester number and name
- Implements `Displayable` interface

#### `Branch.java`
- Represents a branch within a college
- Manages seat capacity and availability
- Initializes 8 semesters automatically
- Implements `CapacityManageable` and `Displayable` interfaces

### College Classes

#### `College.java` (Abstract)
- Base class for all colleges
- Implements `Displayable` interface
- Defines abstract `initializeBranches()` method
- Provides methods for branch management

#### Concrete College Classes
- `AECCollege.java`: Assam Engineering College
- `JECCollege.java`: Jorhat Engineering College
- `BVECCollege.java`: Bineswar Brahma Engineering College
- `GECCollege.java`: Golaghat Engineering College

Each initializes 4 branches with appropriate capacities.

### Interface Classes

#### `Searchable.java`
Defines contract for searchable objects with `searchById()` method.

#### `Displayable.java`
Defines contract for displayable objects with `display()` method.

#### `Admissible.java`
Defines contract for admissible objects with `canAdmit()` and `admit()` methods.

#### `CapacityManageable.java`
Defines contract for capacity management with seat checking and counting methods.

### Service Classes

#### `AdmissionService.java`
- Handles all admission logic
- Validates seat availability
- Prevents duplicate student IDs
- Manages student repository
- Displays branch capacity information

#### `MenuService.java`
- Handles all user interface operations
- Displays menus and prompts
- Processes user input
- Calls appropriate admission service methods
- Manages college and branch selection
- Handles all user interactions

### Repository Classes

#### `StudentRepository.java` (Interface)
- Defines contract for data storage operations
- Methods for adding, searching, and retrieving students

#### `StudentRepositoryImpl.java`
- Implements `StudentRepository` interface
- Uses ArrayList for data storage
- Provides CRUD operations for students
- Prevents duplicate student IDs

### Main Class

#### `Main.java`
- Entry point for the application
- Initializes all colleges
- Creates repository and services
- Runs the main application loop
- Handles menu navigation

## Extensibility

The application is designed to be easily extensible:

### Add a New College
1. Create a new class extending `College`
2. Implement `initializeBranches()` method
3. Add to colleges list in `Main.java`

### Modify Branch Capacities
Edit the branch initialization in respective college classes.

### Add New Features
Create new services following the same pattern as `AdmissionService`.

## Future Enhancements

- Database integration (SQL)
- Generate admission certificate
- Semester-wise course assignment
- Grade management system
- Attendance tracking
- Fee management
- Email notifications
- Graphical User Interface (GUI)
- Data export functionality (CSV, PDF)

## Author Notes

This project demonstrates professional Java development practices with emphasis on:
- Clean, readable, and maintainable code
- Proper use of OOP and SOLID principles
- Modular architecture
- User-friendly interface
- Comprehensive documentation
- Beginner-friendly implementation

The codebase is intentionally verbose with comments to help beginners understand each component and design decision.

## License

This project is open source and available for educational purposes.

---

**Version**: 1.0
**Created**: May 11, 2026
**Last Updated**: May 11, 2026
