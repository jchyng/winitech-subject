CREATE TABLE board2.`member` (
	id BIGINT auto_increment NOT NULL,
	username varchar(30) NOT NULL,
	password varchar(60) NOT NULL,
	name varchar(18) NOT NULL,
	role varchar(20) NOT NULL,
	use_yn BOOL DEFAULT true NOT NULL,
	CONSTRAINT member_pk PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;




CREATE TABLE board2.post (
	id BIGINT auto_increment NOT NULL,
	member_id BIGINT NOT NULL,
	title varchar(300) NOT NULL,
	content varchar(3000) NOT NULL,
	thumbnail varchar(100),
	view_count BIGINT DEFAULT 0 NOT NULL,
	reg_date DATETIME NOT NULL,
	use_yn BOOL DEFAULT true NOT NULL,
	CONSTRAINT post_pk PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;




CREATE TABLE board2.file_info (
	id BIGINT auto_increment NOT NULL,
	post_id BIGINT NOT NULL,
	original_name varchar(255) NOT NULL,
	extension varchar(20) NOT NULL,
	saved_name varchar(36) NOT NULL,
	`size` BIGINT NOT NULL,
	`path` varchar(100) NOT NULL,
	reg_date DATETIME NOT NULL,
	use_yn BOOL DEFAULT true NOT NULL,
	CONSTRAINT file_info_pk PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;