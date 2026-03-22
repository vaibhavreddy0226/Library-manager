# 📚 Library Management System 

A console-based **Library Management System** developed using **Java and Object-Oriented Programming (OOP)** principles.  
This project simulates real-world library operations such as managing books, users, and issuing/returning books.

---

## 🚀 Project Overview

This application is designed to demonstrate the implementation of core **OOP concepts** like classes, objects, encapsulation, inheritance, and modular design in Java.

It provides a simple and efficient way to manage library records through a structured and menu-driven interface.

---

## 🧠 Features

- 📖 Add new books to the library  
- ❌ Remove books from the system  
- 🔍 Search books by title or ID  
- 👤 Manage user records  
- 📚 Issue books to users  
- 🔄 Return books  
- 📊 Track available and issued books  

---

## 🛠️ Tech Stack

- **Language:** Java  
- **Concepts Used:**  
  - Object-Oriented Programming (OOP)  
  - Classes & Objects  
  - Encapsulation  
  - Inheritance  
  - Abstraction  

---

## 📂 Project Structure
```
Library-manager/
│
├── README.md
|
└──  src/
      ├── MainFrame.java 
      ├── AdminFrame.java
      ├── StudentFrame.java
      ├── Book.java 
      ├── Borrow.java
      ├── Borrowable.java
      ├── CSVHandler.java
      ├── LibraryItem.java
      ├── LibraryManager.java
      ├── Student.java
      ├── books.csv 
      ├── borrows.csv
      └── students.csv       
      
```

---

## ⚙️ How It Works

1. The application starts with a **menu-driven interface**  
2. User selects an operation (add, search, issue, return, etc.)  
3. The system processes the request using OOP-based classes  
4. Data is updated and displayed accordingly  

---

## ▶️ How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/vaibhavreddy0226/Library-manager.git

2. Navigate to the project folder:
   ```bash
   cd Library-manager
3. Compile the Java files:
   ```bash
   javac MainFrame.java
4. Run the program:
   ```bash
   java MainFrame.java
