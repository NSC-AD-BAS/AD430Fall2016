USE ad430_db;

INSERT INTO user(
	
    microsft_api_info, 
    full_name, 
    
    
    
    is_interpreter,
    ok_to_chat, 
    ok_to_show_location,
    last_known_location_lat, 
    last_knonw_location_long
)VALUES
 ("microAccts01", "Seattle PersonA", 0, 1, 1, 47.6080, -122.3351),
 ("microAccts02", "Seattle PersonB", 0, 1, 1, 47.6080, -122.3351),
 ("microAccts03", "Seattle PersonC", 1, 0, 1, 47.6080, -122.3351),
 ("microAccts04", "Seattle PersonD", 1, 0, 1, 47.6080, -122.3351),
 
 ("microAccts05", "Tacoma PersonA", 0, 1, 0, 47.2528, -122.4442),
 ("microAccts06", "Tacoma PersonB", 0, 1, 0, 47.2528, -122.4442),
 ("microAccts07", "Tacoma PersonC", 1, 0, 0, 47.2528, -122.4442),
 ("microAccts08", "Tacoma PersonD", 1, 0, 0, 47.2528, -122.4442),
 
 ("microAccts09", "Bellevue PersonA", 0, 1, 1, 47.6080, -122.3351),
 ("microAccts10", "Bellevue PersonB", 0, 1, 1, 47.6080, -122.3351),
 ("microAccts11", "Bellevue PersonC", 1, 1, 1, 47.6080, -122.3351),
 ("microAccts12", "Bellevue PersonD", 1, 1, 1, 47.6080, -122.3351)
;

INSERT INTO user_report(
	
    creating_user_id, #not null
    blocking_user_id,#not null
    
    creation_timestamp,
    
    was_reported,#not null
    reporting_user_comment
    
) VALUES
( 1, 2, NOW(), 0, "comments"),
( 1, 3, NOW(), 1, "comments"),
( 3, 2, NOW(), 0, "comments"),
( 3, 1, NOW(), 1, "comments"),
( 3, 3, NOW(), 0, "comments")

;

INSERT INTO convo(
	
   
    hoh_user_id,
    start_time,
    interpreter_user_id,
    last_updated_hoh,
    last_updated_interpreter
    
    
) VALUES
( 1, NOW(), 2, NOW(), NOW()  ),
( 1, NOW(), 3, NOW(), NOW() ),
( 4, NOW(), 6, NOW(), NOW()  ),
( 5, NOW(), 7, NOW(), NOW()  ),
( 8, NOW(), 10, NOW(), NOW()  ),
( 9, NOW(), 11, NOW(), NOW()  )

;INSERT INTO convo_rating(
	convo_id, 
    asl_skill, 
    translate_speed,
    friendliness 
    
) VALUES
(1, 5, 5, 5)

;
