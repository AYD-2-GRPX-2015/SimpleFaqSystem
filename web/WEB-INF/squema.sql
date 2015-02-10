
drop database simplefaq;

CREATE DATABASE simplefaq
  DEFAULT CHARACTER SET latin1
  DEFAULT COLLATE latin1_spanish_ci;

use simplefaq;

CREATE TABLE sf_user(
    user                VARCHAR(255) PRIMARY KEY,
    password            VARCHAR(255),
    name                VARCHAR(512),
    mail                VARCHAR(512),
    creationdate        DATETIME,
    estado              INT
)ENGINE=INNODB;


CREATE TABLE sf_faq_topic(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(1024) NOT NULL,
    parent              INT,
    FOREIGN KEY (parent) 
        REFERENCES sf_faq_topic(id)
        ON DELETE RESTRICT
)ENGINE=INNODB;

CREATE TABLE sf_faq(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    postdate            DATETIME,
    title               VARCHAR(1024),
    short_description   VARCHAR(2024),
    long_description    TEXT,
    user                VARCHAR(255),
    topic               INT NULL,
    visits              INT,
    estado              INT,
    FOREIGN KEY (user) 
        REFERENCES sf_user(user)
        ON DELETE RESTRICT,
    FOREIGN KEY (topic) 
        REFERENCES sf_faq_topic(id)
        ON DELETE RESTRICT
)ENGINE=INNODB;

CREATE TABLE sf_comment(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    faq                 INT,
    post_date           DATETIME,
    user                VARCHAR(255),
    comment             VARCHAR(1024),
    FOREIGN KEY (faq) 
        REFERENCES sf_faq(id)
        ON DELETE CASCADE,
    FOREIGN KEY (user) 
        REFERENCES sf_user(user)
        ON DELETE CASCADE
)ENGINE=INNODB;

CREATE TABLE sf_user_permission(
    id                      INT AUTO_INCREMENT PRIMARY KEY,
    service_option          VARCHAR(255),
    post_date               DATETIME,
    user                    VARCHAR(255),
    FOREIGN KEY (user) 
        REFERENCES sf_user(user)
        ON DELETE CASCADE
)ENGINE=INNODB;


CREATE TABLE sf_faq_history(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    faq                 INT,
    user                VARCHAR(255),
    date                DATETIME,
    comment             VARCHAR(2048),
    FOREIGN KEY (faq) 
        REFERENCES sf_faq(id)
        ON DELETE CASCADE,
    FOREIGN KEY (user) 
        REFERENCES sf_user(user)
        ON DELETE CASCADE
)ENGINE=INNODB;



CREATE TABLE sf_uploaded_file(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    name                varchar(512) NOT NULL,
    filesize            int,
    description         varchar(2048) NULL,
    uplad_date          datetime NOT NULL,
    path                varchar(512) NOT NULL,
    user                VARCHAR(255) NULL,
    faq                 INT NULL,
    FOREIGN KEY (faq) 
        REFERENCES sf_faq(id)
        ON DELETE CASCADE,
    FOREIGN KEY (user) 
        REFERENCES sf_user(user)
        ON DELETE CASCADE
)ENGINE=INNODB;


CREATE TABLE sf_faq_score(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    user                VARCHAR(255),
    faq                 INT,
    level               SMALLINT,
    added_date          DATETIME,
    FOREIGN KEY (faq) 
        REFERENCES sf_faq(id)
        ON DELETE CASCADE,
    FOREIGN KEY (user) 
        REFERENCES sf_user(user)
        ON DELETE CASCADE
)ENGINE=INNODB;


CREATE TABLE sf_faq_contact(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    faq                 INT,
    name                VARCHAR(255),
    phone1              VARCHAR(64) NULL,
    phone2              VARCHAR(64) NULL,
    email1              VARCHAR(128) NULL,
    email2              VARCHAR(128) NULL,
    area                VARCHAR(128) NULL,
    FOREIGN KEY (faq) 
        REFERENCES sf_faq(id)
        ON DELETE CASCADE
)ENGINE=INNODB;

insert into sf_user(user,password,name,mail,creationdate) values ('admin','21232f297a57a5a743894a0e4a801fc3','Root user administrator',null,now());
insert into sf_faq_topic(name,parent) values('NBJ',null)
