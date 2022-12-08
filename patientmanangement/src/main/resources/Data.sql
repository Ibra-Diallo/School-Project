REPLACE INTO `role` VALUES
(1,'Doctor'),
(2, 'Patient'),
(3, 'Admin');

INSERT INTO hospitalManagement_db.user_details (id,create_date,date_of_birth,first_name,last_name,password,username) VALUES
	 (500,NULL,NULL,'admin','admin','$2a$12$NJopfxse./FveUEB.boRkOupIdGRlY1vdQI82pbfQUzJB5jBwu9A.','admin');
INSERT INTO hospitalManagement_db.user_role (id,role_id) VALUES
	 (500,3);
