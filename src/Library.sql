DROP TABLE IF EXISTS Loan;
DROP TABLE IF EXISTS Author_Book;
DROP TABLE IF EXISTS Book;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS Author;
DROP TABLE IF EXISTS Phone;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Publisher;
DROP TABLE IF EXISTS Employee;

CREATE TABLE Publisher (
    Name VARCHAR(30) PRIMARY KEY,
    Address VARCHAR(50),
    Phone VARCHAR(15)
);

CREATE TABLE Category (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE Book (
    ISBN VARCHAR(20) PRIMARY KEY NOT NULL,
    Title VARCHAR(100) NOT NULL,
    Description VARCHAR(255),
    EditionNumber INT,
    Year VARCHAR(4) NOT NULL,
    PublisherName VARCHAR(30) NOT NULL,
    CategoryID INTEGER NOT NULL,
    FOREIGN KEY (PublisherName) 
        REFERENCES Publisher (Name),
    FOREIGN KEY (CategoryID)
        REFERENCES Category (ID)
);

CREATE TABLE Author (
    FirstName VARCHAR(20) NOT NULL,
    LastName VARCHAR(30) NOT NULL,
    PRIMARY KEY (FirstName, LastName)
);    

CREATE TABLE Author_Book (
    AuthorFirstName VARCHAR(20) NOT NULL,
    AuthorLastName VARCHAR(30) NOT NULL,
    BookISBN VARCHAR(20) NOT NULL,
    PRIMARY KEY (AuthorFirstName, AuthorLastName, BookISBN),
    FOREIGN KEY (AuthorFirstName, AuthorLastName) 
        REFERENCES Author (FirstName, LastName),
    FOREIGN KEY (BookISBN) 
        REFERENCES Book (ISBN)
);

CREATE TABLE Customer (
    RG VARCHAR(9) PRIMARY KEY NOT NULL,
    CPF VARCHAR(11),
    Name VARCHAR(80) NOT NULL,
    Email VARCHAR(255),
    Street VARCHAR(30),
    Number INT,
    Complement VARCHAR(50),
    PostalCode CHAR(8)
);

CREATE TABLE Phone (
    Number INT NOT NULL,
    CustomerRG  VARCHAR(9) NOT NULL,
    PRIMARY KEY (Number, CustomerRG),
    FOREIGN KEY (CustomerRg) 
        REFERENCES Customer (RG)
);

CREATE TABLE Employee (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Name VARCHAR(80) NOT NULL,
    Phone INT,
    Role VARCHAR(30) NOT NULL,
    Email VARCHAR(255) NOT NULL UNIQUE,
    Password VARCHAR(8) NOT NULL
);

CREATE TABLE Loan (
    LoanDate DATE NOT NULL,
    ReturnDate DATE NOT NULL,
    DateReturned DATE,
    CustomerRG VARCHAR(9) NOT NULL,
    BookISBN VARCHAR(20) NOT NULL,
    EmployeeID INTEGER NOT NULL,
    PRIMARY KEY (LoanDate, CustomerRG, BookISBN),
    FOREIGN KEY (CustomerRG)
        REFERENCES Customer (RG),
    FOREIGN KEY (BookISBN)
        REFERENCES Book (ISBN),
    FOREIGN KEY (EmployeeID) 
        REFERENCES Employee (ID)
);


-- seleciona todos os telefones de um cliente
SELECT 
    Number
FROM 
    Phone
WHERE
    CustomerRG = '558201994';
    
    
-- seleciona todos empréstimos
SELECT 
    * 
FROM 
    Loan;
    
-- seleciona todos os livros de um empréstimo
SELECT 
    Book.ISBN,
    Book.Title,
    Book.Description,
    Book.EditionNumber,
    Book.Year,
    Category.ID,
    Category.Name,
    Book.PublisherName
FROM
    Loan INNER JOIN Book
        ON Loan.BookISBN = Book.ISBN
    INNER JOIN Category
        ON Book.CategoryID = Category.ID
WHERE
    Loan.LoanDate = '25/06/2020' AND Loan.CustomerRG = '558201994';
    

SELECT * FROM Book;

    
-- selecionar todos os autores de um livro
SELECT 
    Author.FirstName, Author.LastName
FROM 
    Book INNER JOIN Author_Book
        ON Book.ISBN = Author_Book.BookISBN
    INNER JOIN Author 
        ON Author_Book.AuthorFirstName = Author.FirstName AND
         Author_Book.AuthorLastName = Author.LastName
WHERE
    Book.ISBN = '3';

