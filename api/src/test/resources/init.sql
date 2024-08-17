CREATE DATABASE burgermap_test;
USE burgermap_test;

CREATE TABLE test.member (
    member_id bigint NOT NULL AUTO_INCREMENT,
    login_id varchar(15) DEFAULT NULL,
    email VARCHAR(255) DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (member_id)
)