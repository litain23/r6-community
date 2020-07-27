INSERT INTO user_profile (username, email, password, is_email_authenticated, is_ubi_authenticated)
values
('admin', 'test@email.com', 'test_password', TRUE, FALSE),
('user0', 'test@email.com', 'test_password', TRUE, FALSE),
('user1', 'test@email.com', 'test_password', TRUE, FALSE);


INSERT INTO POST (user_profile_id, notice, type, title, content, view_cnt, recommend_cnt, created_time)
values
(1, true, 'notice', '공지입니다', '공지입니다', 2, 0, '2020-01-01'),
(2, false, 'free', 'title1', 'conetne1', 0, 0, '2020-01-01'),
(2, false, 'free', 'title2', 'conetne2', 0, 0, '2020-01-01'),
(2, false, 'free', 'title3', 'conetne3', 0, 0, '2020-01-01'),
(2, false, 'free', 'title4', 'conetne4', 0, 0, '2020-01-01');

INSERT INTO COMMENT (user_profile_id, post_id, content, parent_comment_id, created_time)
values
(2, 2, '댓글입니다', null, '2020-01-02'),
(1, 2, '대댓글입니다', 1, '2020-01-03'),
(2, 2, '대대ㅡㅅ글입니다', 1, '2020-01-04'),
(1, 2, '새로운 댓글입니다', null, '2020-01-01');
