USE master;
DROP DATABASE IF EXISTS BookStore;
CREATE DATABASE BookStore;
GO
USE BookStore;

CREATE TABLE Publisher (
    Name VARCHAR(30) PRIMARY KEY,
    Phone VARCHAR(15),
	Street VARCHAR(80),
	Number VARCHAR(6),
	Complement VARCHAR(20)
);

CREATE TABLE Category (
    ID INT IDENTITY PRIMARY KEY,
    Name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE Book (
    ISBN VARCHAR(20) PRIMARY KEY,
    Title VARCHAR(100) NOT NULL,
    Description VARCHAR(255),
    EditionNumber INT CHECK(EditionNumber > 0),
    Year CHAR(4) NOT NULL CHECK(LEN(Year) = 4),
	Price DECIMAL(7, 2) NOT NULL CHECK(Price > 0),
	Copies SMALLINT NOT NULL CHECK(Copies >= 0),
    PublisherName VARCHAR(30) NOT NULL,
    CategoryID INT NOT NULL,
    FOREIGN KEY (PublisherName) 
        REFERENCES Publisher (Name),
    FOREIGN KEY (CategoryID)
        REFERENCES Category (ID)
);

CREATE TABLE Author (
	ID INT IDENTITY PRIMARY KEY,
    FirstName VARCHAR(30) NOT NULL,
    LastName VARCHAR(20) NOT NULL
);    

CREATE TABLE Author_Book (
    AuthorID INT NOT NULL,
    BookISBN VARCHAR(20) NOT NULL,
    PRIMARY KEY (AuthorID, BookISBN),
    FOREIGN KEY (AuthorID) 
        REFERENCES Author (ID),
    FOREIGN KEY (BookISBN) 
        REFERENCES Book (ISBN)
);

CREATE TABLE Customer (
    CPF CHAR(11) PRIMARY KEY CHECK(LEN(CPF) = 11),
    Name VARCHAR(80) NOT NULL,
    Email VARCHAR(50),
    Street VARCHAR(80),
    Number VARCHAR(6),
    Complement VARCHAR(20),
    ZipCode CHAR(8)
);

CREATE TABLE Phone (
	Number VARCHAR(11) NOT NULL CHECK(LEN(Number) >= 8),
    CustomerCPF CHAR(11) NOT NULL,
    PRIMARY KEY (Number, CustomerCPF),
    FOREIGN KEY (CustomerCPF) 
        REFERENCES Customer (CPF)
);

CREATE TABLE Employee (
    ID INT IDENTITY PRIMARY KEY,
    Name VARCHAR(80) NOT NULL,
    Phone VARCHAR(11) NOT NULL CHECK(LEN(Phone) >= 8),
    Role VARCHAR(30) NOT NULL,
    Email VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(20) NOT NULL CHECK(LEN(Password) >= 6)
);

CREATE TABLE Sale (
	ID INT IDENTITY PRIMARY KEY,
    Date DATE NOT NULL CHECK(Date <= GETDATE()),
	Total DECIMAL(7, 2),
	CustomerCPF CHAR(11) NOT NULL,
    EmployeeID INT NOT NULL,
    FOREIGN KEY (CustomerCPF)
        REFERENCES Customer (CPF),
    FOREIGN KEY (EmployeeID) 
        REFERENCES Employee (ID)
);

CREATE TABLE Sale_Book (
	SaleID INT NOT NULL,
	BookISBN VARCHAR(20) NOT NULL,
	Count TINYINT NOT NULL CHECK(Count > 0),
	PRIMARY KEY (SaleID, BookISBN)
);

GO

CREATE TRIGGER Trigger_Sale_Book
ON Sale_Book
AFTER INSERT
AS
BEGIN
	DECLARE
	@Count TINYINT,
	@BookISBN VARCHAR(20)
	
	SELECT @Count = Count, @BookISBN = BookISBN FROM INSERTED 
	
	UPDATE Book
	SET Copies -= @Count
	WHERE ISBN = @BookISBN
END;

GO

INSERT INTO Employee
	VALUES('Admin', '12345678', 'ADMIN', 'admin', 'admin123');


-- QUERIES
SELECT * FROM Book;
SELECT * FROM Sale;
SELECT * FROM Sale_Book;
SELECT * FROM Author;
SELECT * FROM Customer;
SELECT * FROM Employee;

-- seleciona os dados de todas as vendas (exceto os livros)
SELECT  
	* 
FROM 
	Sale INNER JOIN Customer
		ON Sale.CustomerCPF = Customer.CPF
	INNER JOIN Employee
		ON Sale.EmployeeID = Employee.ID;
    
-- todos os livros
SELECT
	 * 
FROM 
	Book INNER JOIN Category
		ON Book.CategoryID = Category.ID
	INNER JOIN Publisher
		ON Book.PublisherName = Publisher.Name;

-- Seleciona todos os telefones de um cliente
SELECT 
    Number
FROM 
    Phone
WHERE
    CustomerCPF = '558201994';
 
-- Seleciona todos os clientes que não têm compras realizadas
SELECT
	Customer.CPF,
	Customer.Name,
	Customer.Email,
	Phone.Number
FROM
	Customer INNER JOIN Sale
		ON Customer.CPF = Sale.CustomerCPF
	INNER JOIN Phone 
		ON Sale.CustomerCPF = Phone.CustomerCPF
WHERE
	Sale.CustomerCPF IS NULL;
	
-- Seleciona todos os clientes cuja a última compra foi há mais de mês
SELECT DISTINCT
	*
FROM
	Customer INNER JOIN Sale
		ON Customer.CPF = Sale.CustomerCPF
WHERE
	DATEDIFF(MONTH, Sale.Date, GETDATE()) >= 1 

-- maior venda
SELECT
	Sale.ID,
	Sale.Date,
	Sale.Total,
	Customer.CPF,
	Customer.Name,
	Customer.Email,
	Customer.Street,
	Customer.Number,
	Customer.Complement,
	Customer.ZipCode,
	Employee.ID,
	Employee.Name,
	Employee.Phone,
	Employee.Role,
	Employee.Email
FROM
	Customer INNER JOIN Sale
		ON Customer.CPF = Sale.CustomerCPF
	INNER JOIN Employee
		ON Sale.EmployeeID = Employee.ID
WHERE
	Sale.Total IN (
		SELECT 
			MAX(Sale.Total)
		FROM
			Sale
	);
	
-- menor venda
SELECT
	Sale.ID,
	Sale.Date,
	Sale.Total,
	Customer.CPF,
	Customer.Name,
	Customer.Email,
	Customer.Street,
	Customer.Number,
	Customer.Complement,
	Customer.ZipCode,
	Employee.ID,
	Employee.Name,
	Employee.Phone,
	Employee.Role,
	Employee.Email
FROM
	Customer INNER JOIN Sale
		ON Customer.CPF = Sale.CustomerCPF
	INNER JOIN Employee
		ON Sale.EmployeeID = Employee.ID
WHERE
	Sale.Total IN (
		SELECT 
			MIN(Sale.Total)
		FROM
			Sale
	);

-- quantidade de vendas realizadas no mês
SELECT
	Sale.ID,
	Sale.Date,
	Sale.Total,
	Customer.CPF,
	Customer.Name,
	Customer.Email,
	Customer.Street,
	Customer.Number,
	Customer.Complement,
	Customer.ZipCode,
	Employee.ID,
	Employee.Name,
	Employee.Phone,
	Employee.Role,
	Employee.Email
FROM
	Customer INNER JOIN Sale
		ON Customer.CPF = Sale.CustomerCPF
	INNER JOIN Employee
		ON Sale.EmployeeID = Employee.ID
WHERE
	MONTH(Sale.Date) = MONTH(GETDATE())


-- categorização dos livros por popularidade (quantidade vendida)
SELECT
	Book.ISBN,
	Book.Title,
	Book.Description,
	Book.EditionNumber,
	Book.Year,
	Book.Price,
	Book.Copies,
	Book.PublisherName,
	Book.CategoryID,
	CASE
		WHEN (COUNT(Sale_Book.Count) <= 10) THEN 'Low'
		WHEN (COUNT(Sale_Book.Count) <= 20) THEN 'Normal'
		WHEN (COUNT(Sale_Book.Count) <= 30) THEN 'High'
		ELSE 'Best-Seller'
	END AS Popularity
FROM
	Book INNER JOIN Sale_Book
		ON Book.ISBN = Sale_Book.BookISBN
GROUP BY
	Book.ISBN, Book.Title, Book.Description, Book.EditionNumber,
	Book.Year, Book.Price, Book.Copies, Book.PublisherName,
	Book.CategoryID;


-- autores que têm pelo menos 2 livros
SELECT 
	Author.ID,
	Author.FirstName,
	Author.LastName
FROM
	Author INNER JOIN Author_Book
		ON Author.ID = Author_Book.AuthorID
HAVING
	COUNT(Author_Book.AuthorID) >= 2;


-- funcionário que realizou mais vendas
SELECT
	Employee.ID,
	Employee.Name,
	Employee.Phone,
	Employee.Role,
	Employee.Email,
	COUNT(Sale.EmployeeID) AS TotalSales
FROM
	Employee INNER JOIN Sale
		ON Employee.ID = Sale.EmployeeID
GROUP BY
	Employee.ID, Employee.Name, Employee.Phone,
	Employee.Role, Employee.Email;


-- seleciona todos os livros de uma mesma venda
SELECT 
    Book.ISBN,
    Book.Title,
    Book.Description,
    Book.EditionNumber,
    Book.Year,
    Book.PublisherName,
	Category.Name
FROM
    Sale INNER JOIN Sale_Book
        ON Sale.ID = Sale_Book.SaleID
    INNER JOIN Book
        ON Book.ISBN = Sale_Book.BookISBN
	INNER JOIN Category
		ON Book.CategoryID = Category.ID
WHERE
    Sale.ID = 1;
    
    
-- selecionar todos os autores de um livro
SELECT 
    Author.ID,
	Author.FirstName,
	Author.LastName
FROM 
    Book INNER JOIN Author_Book
        ON Book.ISBN = Author_Book.BookISBN
    INNER JOIN Author 
        ON Author_Book.AuthorID = Author.ID
WHERE
    Book.ISBN = '333-333-33';


-- selecionar todos os autores dos livros que têm mais de um autor
SELECT 
    Author.ID,
	Author.FirstName,
	Author.LastName
FROM 
    Book INNER JOIN Author_Book
        ON Book.ISBN = Author_Book.BookISBN
    INNER JOIN Author 
        ON Author_Book.AuthorID = Author.ID
HAVING
	COUNT(Author.ID) > 1;
