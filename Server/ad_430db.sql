DROP DATABASE IF EXISTS ad430_db;
CREATE DATABASE ad430_db;
use ad430_db;

CREATE TABLE user
(
	user_id 					INT 			UNSIGNED		PRIMARY KEY 	AUTO_INCREMENT,
    microsft_api_info 			VARCHAR(100)	UNIQUE 			NOT NULL,
	full_name					VARCHAR(250) 	NOT NULL,
	last_active_time			DATETIME		NULL,
    is_interpreter				BIT				NOT NULL,
    ok_to_chat					BIT				NOT NULL,
    ok_to_show_location			BIT				NOT NULL,
    last_known_location_lat		DECIMAL(7,4)	NULL,
    last_knonw_location_long	DECIMAL(7,4)	NULL,
    last_location_update		DATETIME		NULL
);
CREATE INDEX ind_user_microsft_api_info ON user(microsft_api_info);

CREATE TABLE user_report
(
	report_id					INT 			UNSIGNED		PRIMARY KEY 	AUTO_INCREMENT,
    creating_user_id			INT				UNSIGNED 		NOT NULL,
    blocking_user_id			INT				UNSIGNED		NOT NULL,
    creation_timestamp			DATETIME		NOT NULL,
    was_reported				BIT				NOT NULL,
    reporting_user_comment		VARCHAR(500)	NULL,
	CONSTRAINT cstr_user_report_users_uq 	UNIQUE(creating_user_id,blocking_user_id),
	CONSTRAINT cstr_user_report_fk_creating_user
		FOREIGN KEY (creating_user_id)
		REFERENCES user (user_id),
	CONSTRAINT cstr_user_report_fk_blocking_user
		FOREIGN KEY (blocking_user_id)
		REFERENCES user (user_id)
);
CREATE INDEX ind_user_report_creating_user_id ON user_report(creating_user_id);
CREATE INDEX ind_user_report_blocking_user_id ON user_report(blocking_user_id);


CREATE TABLE convo
(
	convo_id					INT				UNSIGNED		PRIMARY KEY		AUTO_INCREMENT,
    start_time					DATETIME		NOT NULL,
    end_time					DATETIME		NULL,
    hoh_user_id					INT				UNSIGNED		NOT NULL,
    interpreter_user_id			INT				UNSIGNED		NOT NULL,
    last_updated_hoh			DATETIME		NOT NULL,
    last_updated_interpreter	DATETIME		NOT NULL,
	CONSTRAINT cstr_convo_fk_interpreter_user_id
		FOREIGN KEY (interpreter_user_id)
		REFERENCES user (user_id),
	CONSTRAINT cstr_convo_fk_hoh_user_id
		FOREIGN KEY (hoh_user_id)
		REFERENCES user (user_id)
);
CREATE INDEX ind_convo_interpreter_user_id ON convo(interpreter_user_id);
CREATE INDEX ind_convo_hoh_user_id ON convo(hoh_user_id);

CREATE TABLE convo_rating
(
	convo_id					INT				UNSIGNED		PRIMARY KEY,
    asl_skill					INT				NOT NULL,
    translate_speed				INT				NOT NULL,
    friendliness				INT				NOT NULL,
	CONSTRAINT cstr_convo_rating_fk_convo_id
		FOREIGN KEY (convo_id)
		REFERENCES convo (convo_id)
);

CREATE VIEW intrepreter_ratings AS  
SELECT u.user_id, 
	AVG(cr.friendliness) AS avg_friendliness, 
    AVG(cr.asl_skill) AS avg_asl_skill, 
    AVG(cr.translate_speed) AS avg_translate_speed,
    SUM(ISNULL(ur.blocking_user_id)) as total_complaint_count,
    SUM(was_reported) as total_report_count
FROM user u
LEFT JOIN convo co
ON u.user_id = co.interpreter_user_id
LEFT JOIN convo_rating cr
ON cr.convo_id = co.convo_id
LEFT JOIN user_report ur
ON ur.blocking_user_id = u.user_id
WHERE u.is_interpreter
GROUP BY u.user_id;



