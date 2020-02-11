CREATE TABLE transfers(
   ID INTEGER PRIMARY KEY,
   SOURCE_ID INTEGER NOT NULL,
   TARGET_ID INTEGER NOT NULL,
   AMOUNT decimal NOT NULL,
   TRANSFER_TIME timestamp (6) without time zone NOT NULL,
   CONSTRAINT transfers_accounts_source_constrain
      FOREIGN KEY (SOURCE_ID)
      REFERENCES accounts (ID),
   CONSTRAINT transfers_accounts_target_constrain
      FOREIGN KEY (TARGET_ID)
      REFERENCES accounts (ID)
);