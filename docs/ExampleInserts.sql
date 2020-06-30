USE BookStore
GO

INSERT INTO Employee
	VALUES
		('Gustavo Alves', '11912344321', 'ADMIN', 'guto@admin', 'admin123'),
		('Leandro Colevati', '11998766789', 'ADMIN', 'colevati@admin', 'admin123'),
		('Daniel Caldeira', '1123324554', 'CLERK', 'dani@clerk', 'clerk123')

INSERT INTO Category 
	VALUES
		('Arts & Music'),
		('Computers & Tech'),
		('Economics'),
		('Fiction'),
		('Horror'),
		('Mystery'),
		('Romance')

INSERT INTO Publisher 
	VALUES
		('Editora Alta Books','(21) 3278-8069', 'R. Vi�va Cl�udio', '291', NULL),
		('Addison-Wesley Professional', NULL, NULL, NULL, NULL),
		('Novatec Editora', '(11) 2959-6529', 'R. Lu�s Ant�nio dos Santos', '110', NULL)

INSERT INTO Author
	VALUES
		('Eric', 'Evans'),
		('Martin', 'Fowler'),
		('Erich', 'Gamma'),
		('Richard', 'Helm'),
		('Ralph', 'Johson'),
		('John', 'Vlissides'),
		('Grady', 'Booch')

GO

INSERT INTO Book
	VALUES
		('978-8550800653', 'Domain-Driven Design: Atacando as Complexidades no Cora��o do Software', 
		'Este n�o � um livro sobre tecnologias espec�ficas. Ele oferece aos leitores uma abordagem sistem�tica com rela��o ao domain-driven design...',
		3, 2016, 94.50, 20, 'Editora Alta Books', 2),
		('B000SEIBB8', 'Design Patterns: Elements of Reusable Object-Oriented Software',
		'Capturing a wealth of experience about the design of object-oriented software, four top-notch designers present a catalog of simple and succinct...',
		1, 1994, 141.36, 35, 'Addison-Wesley Professional', 2),
		('978-8575227244', 'Refatora��o: Aperfei�oando o Design de C�digos Existentes',
		'Por mais de vinte anos, programadores experientes no mundo inteiro contaram com o livro Refatora��o de Martin Fowler para aperfei�oar o design...',
		2, 2020, 96.90, 10, 'Novatec Editora', 2)

GO

INSERT INTO Author_Book
	VALUES
		(1, '978-8550800653'),
		(2, '978-8550800653'),
		(3, 'B000SEIBB8'),
		(4, 'B000SEIBB8'),
		(5, 'B000SEIBB8'),
		(6, 'B000SEIBB8'),
		(7, 'B000SEIBB8'),
		(2, '978-8575227244')

INSERT INTO Customer 
	VALUES
		('40440212312', 'Caique Freitas', 'caique@customer', 'Rua Doutor Fernandes', '123', 'Bloco b', '05423040'),
		('40240432132', 'Murilo Meira', 'murilo@customer', 'Alameda Momor�', '321', NULL, '06454040')

GO

INSERT INTO Phone
	VALUES
		('21995598842', '40440212312'),
		('1124342341','40440212312'),
		('11945233254', '40240432132')

INSERT INTO Sale
	VALUES
		('28/06/2020', 235.86, '40440212312', 3),
		('30/06/2020', 424.08, '40240432132', 3),
		('30/03/2020', 96.90, '40440212312', 3)

GO

INSERT INTO Sale_Book
	VALUES
		(1, '978-8550800653', 1),
		(1, 'B000SEIBB8', 1),
		(2, 'B000SEIBB8', 3),
		(3, '978-8575227244', 1)