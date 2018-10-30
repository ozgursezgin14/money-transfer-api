INSERT INTO BANK(BANK_ID, BANK_NAME, CODE, DESCRIPTION) VALUES
  (1, 'ING Bank', 'ING', 'ING Bank Decription'),
  (2, 'Garanti Bank', 'GRT', 'Garanti Bank Decription'),
  (3, 'AkBank', 'AKB', 'AkBank Decription'),
  (4, 'ISBank', 'ISB', 'ISBank Decription');
  

INSERT INTO CUSTOMER(CUSTOMER_ID, CUSTOMER_NAME, EMAIL, DESCRIPTION, BANKS_CUSTOMER_BANK_ID) VALUES
  (5, 'Joe Doe', 'joe@gmail.com', 'Joe Doe Decription', 1),
  (6, 'John Doe', 'john@gmail.com', 'John Doe Decription', 1),
  (7, 'Jane Doe', 'jane@gmail.com', 'Jane Doe Decription', 3),
  (8, 'Jany Doe', 'jany@gmail.com', 'Jany Doe Decription', 4);
  

INSERT INTO ACCOUNT(ACCOUNT_ID, ACCOUNT_NUMBER, BALANCE, CURRENCY_CODE, DESCRIPTION, CUSTOMERS_ACCOUNT_CUSTOMER_ID) VALUES
  (9, 'ACN1001', 1000.0, 'USD', 'ACN1001 Account Decription', 5),
  (10, ' ACN1003', 3000.0, 'USD', 'ACN1001 Account  Decription', 8),
  (11, 'ACN1004', 500.0,  'EUR', 'ACN1001 Account  Decription', 7),
  (12, 'ACN1007', 10000.0, 'TRY', 'ACN1001 Account  Decription', 5);
