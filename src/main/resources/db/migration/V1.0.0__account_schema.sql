SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for BankAccount
-- ----------------------------
CREATE TABLE bank_account (
  account_number varchar(60) NOT NULL COMMENT '帳號',
  balance INT NOT NULL COMMENT '餘額',
  customer varchar(60) NOT NULL COMMENT '客戶',
  created_by varchar(60) NOT NULL,
  created_date datetime NOT NULL,
  last_modified_by varchar(60) NOT NULL,
  last_modified_date datetime NOT NULL,
  PRIMARY KEY ( account_number )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帳戶資料';

SET FOREIGN_KEY_CHECKS=1;