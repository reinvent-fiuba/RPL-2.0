INSERT INTO roles (id, name, permissions, date_created, last_updated)
VALUES (1, 'admin',
        'course_delete,course_view,course_edit,activity_view,activity_manage,activity_submit,user_view,user_manage',
        '2020-07-28 04:07:38', '2020-07-28 04:07:38');
INSERT INTO roles (id, name, permissions, date_created, last_updated)
VALUES (2, 'student', 'course_view,activity_view,activity_submit,user_view', '2020-07-28 04:07:41',
        '2020-07-28 04:07:41');