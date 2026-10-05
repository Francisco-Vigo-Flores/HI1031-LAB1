INSERT INTO "User" (NAME, USERNAME, PASSWORD, TYPE)
VALUES
    ('Guest', 'guest', '123', 'Customer'),
    ('John Stock', 'StockGoat', '123', 'InventoryManager'),
    ('John Adminis', 'AdminGoat', '123', 'Admin');

INSERT INTO Product (NAME, COST, CATEGORY, DESCRIPTION, QUANTITY)
VALUES
    ('Hot dog', 7, 'Canned food', 'Canned hot dog with bun', 2),
    ('Hamburger', 6, 'Canned food', 'Canned hamburger with no pickles', 5),
    ('High fashion jacket', 1500, 'Clothing', 'Jacket by new up and coming brand Vivet', 10),
    ('Pen set (5 pieces)', 5, 'Office', '5 basic pens, no ink included in the pens, no refunds', 100),
    ('Pet Pig', 200, 'Pets', 'A tiny pet pig with a deformity that causes it to be dwarf sized forever', 0);
