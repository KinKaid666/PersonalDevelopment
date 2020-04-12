--
-- Name:	inventory_owners
--
--
CREATE TABLE inventory_owners 
(
    inventory_owner_id	SERIAL		NOT NULL PRIMARY KEY,
    first_name		VARCHAR(15)	NOT NULL,
    last_name		VARCHAR(30)	NOT NULL,
    address1		VARCHAR(50)         NULL,
    address2		VARCHAR(50)         NULL,
    city		VARCHAR(20)         NULL,
    state		CHAR(2)             NULL,
    zip			VARCHAR(10)         NULL,
    phone		VARCHAR(13)         NULL,
    email		VARCHAR(30)         NULL,
    creation_user	VARCHAR(30)	    NULL DEFAULT CURRENT_USER,
    creation_date	TIMESTAMP	    NULL DEFAULT now()
) ;

--
-- Name:	inventory_owner_names_index
--
CREATE INDEX inventory_owners_names_index ON
    inventory_owners (first_name, last_name) ;

--
--
--
--
CREATE TABLE inventory_types
(
    inventory_type_id	SERIAL		NOT NULL PRIMARY KEY,
    name		VARCHAR(20)	NOT NULL,
    description		VARCHAR(80)	NOT NULL,
    creation_user	VARCHAR(30)	    NULL DEFAULT CURRENT_USER,
    creation_date	TIMESTAMP	    NULL DEFAULT now()
) ;

--
-- Type:	TABLE
-- Name:	inventory_items
--
CREATE TABLE inventory_items
(
    inventory_item_id	SERIAL		NOT NULL,
    inventory_owner_id	INTEGER		NOT NULL REFERENCES inventory_owners,
    title		VARCHAR(50)	NOT NULL,
    author		VARCHAR(50)         NULL,
    publisher		VARCHAR(50)         NULL,
    quantity		INTEGER		NOT NULL DEFAULT 1,
    inventory_type_id	INTEGER		NOT NULL REFERENCES inventory_types,
    purchase_date	TIMESTAMP	NOT NULL DEFAULT CURRENT_DATE,
    purchase_price	NUMERIC(8,2)	NOT NULL DEFAULT 0.00,
    creation_user	VARCHAR(30)	    NULL DEFAULT CURRENT_USER,
    creation_date	TIMESTAMP	    NULL DEFAULT now()
) ;

--
--
--
CREATE INDEX inventory_items_id_index ON inventory_items (inventory_item_id) ;

--
--
--
CREATE INDEX inventory_items_title_index ON inventory_items (title) ;

