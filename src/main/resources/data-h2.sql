INSERT INTO user_profile (username, email, password, is_email_authenticated, is_ubi_authenticated)
values
('admin', 'test@email.com', 'test_password', TRUE, FALSE),
('user1', 'test@email.com', 'test_password', TRUE, FALSE),
('user2', 'test@email.com', 'test_password', TRUE, FALSE),
('user3', 'test@email.com', 'test_password', TRUE, FALSE),
('user4', 'test@email.com', 'test_password', TRUE, FALSE),
('user5', 'test@email.com', 'test_password', TRUE, FALSE);


INSERT INTO POST (user_profile_id, notice, type, title, content, view_cnt, recommend_cnt, created_time)
values
(1, true, 'notice', '공지입니다', '공지입니다', 2, 0, '2020-01-01'),
(2, false, 'free', 'title1', 'conetne1', 0, 0, '2020-01-01'),
(2, false, 'free', 'title2', 'conetne2', 0, 0, '2020-01-01'),
(2, false, 'free', 'title3', 'conetne3', 0, 0, '2020-01-01'),
(2, false, 'free', 'title4', 'conetne4', 0, 0, '2020-01-01');

INSERT INTO COMMENT (post_id, content, user_profile_id, parent_comment_id, created_time)
values
(2, '1', 2, null, '2020-01-02'),
(2, '2', 3, 1, '2020-01-03'),
(2, '2-2', 4, 1, '2020-01-04'),
(2, '3', 2, 2, '2020-01-05'),
(2, '4', 3, 4, '2020-01-07'),
(2, '3-2-1', 5, 3, '2020-01-07');
