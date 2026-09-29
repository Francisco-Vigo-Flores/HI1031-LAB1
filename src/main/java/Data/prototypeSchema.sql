CREATE TYPE UserType as ENUM (
    'Admin', 'InventoryManager', 'Customer'
);

CREATE TABLE "User" (
    ID UUID primary key,
    NAME VARCHAR(50) NOT NULL,
    USERNAME VARCHAR(50) UNIQUE NOT NULL,
    PASSWORD VARCHAR(4) NOT NULL, --probs not permanent
    Type UserType NOT NULL
);

CREATE  TABLE Product (
    ID UUID primary key,
    NAME VARCHAR(50) NOT NULL,
    COST DOUBLE PRECISION NOT NULL,
    CATEGORY VARCHAR(50) NOT NULL
);

CREATE TABLE UserCart (
    ID UUID PRIMARY KEY,
    UserID UUID NOT NULL UNIQUE,
    FOREIGN KEY (UserID) REFERENCES "User"(ID)
);

CREATE TABLE CartProducts (
    CartID UUID NOT NULL,
    ProductID UUID NOT NULL,
    Quantity INT NOT NULL DEFAULT 1,
    PRIMARY KEY (CartID, ProductID),
    FOREIGN KEY (CartID) REFERENCES UserCart(ID),
    FOREIGN KEY (ProductID) REFERENCES Product(ID)
);