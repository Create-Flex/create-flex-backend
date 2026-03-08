-- ==========================================
-- MCN 회사 더미 데이터 통합본 (Updated)
-- 작성일: 2026-01-28
-- 수정사항: employee_detail의 task, department_id를 members로 이동
-- 내용: 초기화(TRUNCATE) 포함, 중복 제거 완료
-- ==========================================

-- 1. 초기화 (재실행 시 충돌 방지)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE attendance;
TRUNCATE TABLE vacation_workation;
TRUNCATE TABLE vacation_sick;
TRUNCATE TABLE vacation_family;
TRUNCATE TABLE vacation;
TRUNCATE TABLE schedule_visitor;
TRUNCATE TABLE schedule;
TRUNCATE TABLE team_relay;
TRUNCATE TABLE team;
TRUNCATE TABLE creator_todo;
TRUNCATE TABLE creator_todo_column;
TRUNCATE TABLE creator_promotion;
TRUNCATE TABLE creator_legal_tax;
TRUNCATE TABLE creator_contract;
TRUNCATE TABLE member_profile;
TRUNCATE TABLE creator_detail;
TRUNCATE TABLE employee_detail;
TRUNCATE TABLE creator_metal_health;
TRUNCATE TABLE health;
TRUNCATE TABLE members;
TRUNCATE TABLE department;
TRUNCATE TABLE notifications;
SET FOREIGN_KEY_CHECKS = 1;

-- ==========================================
-- 2. Department (부서)
-- ==========================================
INSERT INTO department (department_id, department_name, department_call, department_detail, department_color) VALUES
(1001, '경영지원본부', '02-1234-5001', '회사의 전반적인 경영 및 행정 업무를 담당합니다', '#FF6B6B'),
(1002, '사업본부', '02-1234-5002', '신규 사업 기획 및 수익 창출을 담당합니다', '#4ECDC4'),
(1003, '매니지먼트팀', '02-1234-5003', '크리에이터 매니지먼트 및 콘텐츠 기획을 담당합니다', '#95E1D3'),
(1004, '마케팅팀', '02-1234-5004', '브랜드 마케팅 및 홍보를 담당합니다', '#F38181'),
(1005, '기술본부', '02-1234-5005', '시스템 개발 및 기술 지원을 담당합니다', '#AA96DA'),
(1006, '인사문화팀', '02-1234-5006', '인사 관리 및 조직 문화 조성을 담당합니다', '#FCBAD3'),
(1007, '글로벌사업팀', '02-1234-5007', '해외 시장 진출 및 글로벌 파트너십을 담당합니다', '#A8D8EA');

-- ==========================================
-- 3. Members (회원) - task와 department_id 포함
-- ==========================================
-- 관리자, 매니저, 일반 직원
INSERT INTO members (member_id, department_id, member_account, member_password, member_name, member_role, member_status, task) VALUES
(1001, 1006, 'HR001', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '김인사', 'ADMINISTRATOR', 'WORKING', '인사 관리'),
(1002, 1006, 'HR002', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '이채용', 'ADMINISTRATOR', 'WORKING', '채용 담당'),
(1003, 1003, 'MG001', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '박매니저', 'MANAGER', 'WORKING', '크리에이터 매니저'),
(1004, 1003, 'MG002', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '최담당', 'MANAGER', 'WORKING', '크리에이터 매니저'),
(1005, 1003, 'MG003', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '정관리', 'MANAGER', 'WORKING', '크리에이터 매니저'),
(1006, 1004, 'MK001', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '강마케팅', 'MANAGER', 'WORKING', '마케팅 기획'),
(1007, 1005, 'IT001', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '윤개발', 'EMPLOYEE', 'WORKING', '백엔드 개발자'),
(1008, 1005, 'IT002', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '한코딩', 'EMPLOYEE', 'WORKING', '프론트엔드 개발자'),
(1009, 1004, 'MK002', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '송홍보', 'EMPLOYEE', 'WORKING', 'SNS 마케터'),
(1010, 1002, 'BIZ001', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '조사업', 'EMPLOYEE', 'WORKING', '사업 기획'),
(1011, 1001, 'ADM001', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '신경영', 'EMPLOYEE', 'WORKING', '경영 지원'),
(1012, 1007, 'GL001', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', 'Emma Wilson', 'EMPLOYEE', 'WORKING', '글로벌 매니저'),
(1013, 1003, 'MG004', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '문썸네일', 'EMPLOYEE', 'WORKING', '썸네일러'),
(1014, 1004, 'MK003', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '구직원', 'EMPLOYEE', 'SUSPENDED', '마케터'),
(1015, 1005, 'IT003', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '이상백', 'EMPLOYEE', 'WORKING', '백엔드 시니어'),
(1016, 1003, 'PD001', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '김편집', 'EMPLOYEE', 'WORKING', '영상 편집자'),
(1017, 1002, 'PL001', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '최기획', 'EMPLOYEE', 'WORKING', '사업 기획자'),
(1018, 1005, 'IT004', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '권프론트', 'EMPLOYEE', 'WORKING', '프론트엔드 시니어'),
(1019, 1005, 'IT005', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '박인프라', 'EMPLOYEE', 'WORKING', '데브옵스 엔지니어'),
(1020, 1005, 'IT006', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '이보안', 'EMPLOYEE', 'WORKING', '보안 담당자'),
(1021, 1005, 'IT007', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '조데이터', 'EMPLOYEE', 'WORKING', '데이터 엔지니어'),
(1022, 1005, 'IT008', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '강QA', 'EMPLOYEE', 'WORKING', 'QA 엔지니어'),
(1023, 1004, 'MK004', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '윤콘텐츠', 'EMPLOYEE', 'WORKING', '콘텐츠 마케터'),
(1024, 1004, 'MK005', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '정퍼포먼스', 'EMPLOYEE', 'WORKING', '퍼포먼스 마케터'),
(1025, 1004, 'MK006', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '임브랜드', 'EMPLOYEE', 'WORKING', '브랜드 마케터'),
(1026, 1004, 'MK007', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '오카피', 'EMPLOYEE', 'WORKING', '카피라이터'),
(1027, 1004, 'MK008', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '서디자인', 'EMPLOYEE', 'WORKING', '온라인 디자이너'),
(1028, 1003, 'MG005', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '류신입', 'EMPLOYEE', 'WORKING', '신입 매니저'),
(1029, 1003, 'MG006', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '한섭외', 'EMPLOYEE', 'WORKING', '크리에이터 섭외'),
(1030, 1003, 'MG007', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '백현장', 'EMPLOYEE', 'WORKING', '현장 지원 매니저'),
(1031, 1003, 'MG008', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '고유튜브', 'EMPLOYEE', 'WORKING', '채널 관리자'),
(1032, 1003, 'MG009', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '홍쇼츠', 'EMPLOYEE', 'WORKING', '쇼츠 전담 편집자'),
(1033, 1002, 'BIZ002', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '노제휴', 'EMPLOYEE', 'WORKING', '제휴 담당자'),
(1034, 1002, 'BIZ003', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '안계약', 'EMPLOYEE', 'WORKING', '계약 리걸'),
(1035, 1002, 'BIZ004', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '유투자', 'EMPLOYEE', 'WORKING', '투자 유치'),
(1036, 1002, 'BIZ005', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '장영업', 'EMPLOYEE', 'WORKING', '영업 담당'),
(1037, 1002, 'BIZ006', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '곽전략', 'EMPLOYEE', 'WORKING', '전략 기획'),
(1038, 1006, 'HR003', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '현교육', 'EMPLOYEE', 'WORKING', '사내 교육 담당'),
(1039, 1006, 'HR004', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '송복지', 'EMPLOYEE', 'WORKING', '복리후생 담당'),
(1040, 1006, 'HR005', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '신노무', 'EMPLOYEE', 'WORKING', '노무 스페셜리스트'),
(1041, 1001, 'ADM002', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '김재무', 'EMPLOYEE', 'WORKING', '재무 회계'),
(1042, 1001, 'ADM003', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '이총무', 'EMPLOYEE', 'WORKING', '총무 사원'),
(1043, 1001, 'ADM004', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '박시설', 'EMPLOYEE', 'WORKING', '시설 관리'),
(1044, 1007, 'GL002', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', 'Michael Jordan', 'EMPLOYEE', 'WORKING', '미주지역 담당자'),
(1045, 1007, 'GL003', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', 'Sarah Connor', 'EMPLOYEE', 'WORKING', '유럽지역 담당자'),
(1046, 1007, 'GL004', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', 'Akira Tanaka', 'EMPLOYEE', 'WORKING', '일본지역 담당자'),
(1047, 1007, 'GL005', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', 'Li Wei', 'EMPLOYEE', 'WORKING', '중화권지역 담당자');

-- 크리에이터 (department_id는 NULL, task는 NULL)
INSERT INTO members (member_id, department_id, member_account, member_password, member_name, member_role, member_status, task) VALUES
(2001, NULL, 'gamst', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '감스트', 'CREATOR', 'WORKING', NULL),
(2002, NULL, 'ddoganjip', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '또간집', 'CREATOR', 'WORKING', NULL),
(2003, NULL, 'beautyjini', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '뷰티지니', 'CREATOR', 'WORKING', NULL),
(2004, NULL, 'mukbopd', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '먹보PD', 'CREATOR', 'WORKING', NULL),
(2005, NULL, 'gamekingminsu', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '게임왕민수', 'CREATOR', 'WORKING', NULL),
(2006, NULL, 'dailyvlogger', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '일상브이로거', 'CREATOR', 'WORKING', NULL),
(2007, NULL, 'techreviewer', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '테크리뷰어', 'CREATOR', 'WORKING', NULL),
(2008, NULL, 'restingcreator', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '휴식중크리에이터', 'CREATOR', 'WORKING', NULL),
(2009, NULL, 'retiredcreator', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '은퇴한크리', 'CREATOR', 'WORKING', NULL);

-- ==========================================
-- 4. Employee Detail (직원 상세) - task, department_id 제거됨
-- ==========================================
INSERT INTO employee_detail (employee_detail_id, employee_id, nickname, eng_name, personal_email, corpor_email, personal_call, hire_date, emp_date, address, employment_type, leaving_reason, vacation_remainder) VALUES
(1001, 1001, '인사왕', 'Kim Insa', 'kuroiwadeath@gmail.com', 'kim.insa@mcn.com', '010-1111-1111', '2020-03-02', NULL, '서울시 강남구 테헤란로 123', 'EXPERIENCED', NULL, 15.0),
(1002, 1002, '채용마스터', 'Lee Chaeyong', 'lee.chaeyong@naver.com', 'lee.chaeyong@mcn.com', '010-2222-2222', '2021-06-15', NULL, '서울시 서초구 서초대로 456', 'NEWBIE', NULL, 15.0),
(1003, 1003, '매니저박', 'Park Manager', 'rockhavesoul@gmail.com', 'park.manager@mcn.com', '010-3333-3333', '2019-01-10', NULL, '서울시 마포구 월드컵로 789', 'EXPERIENCED', NULL, 15.0),
(1004, 1004, '최담당님', 'Choi Damdang', 'choi.dd@kakao.com', 'choi.damdang@mcn.com', '010-4444-4444', '2022-09-01', NULL, '경기도 성남시 분당구 판교역로 111', 'NEWBIE', NULL, 15.0),
(1005, 1005, '정매니저', 'Jung Gwanli', 'jung.gl@gmail.com', 'jung.gwanli@mcn.com', '010-5555-5555', '2020-11-20', NULL, '인천시 연수구 송도과학로 222', 'EXPERIENCED', NULL, 15.0),
(1006, 1006, '강마케터', 'Kang Marketing', 'kang.mk@naver.com', 'kang.marketing@mcn.com', '010-6666-6666', '2021-03-15', NULL, '서울시 영등포구 여의대로 333', 'NEWBIE', NULL, 15.0),
(1007, 1007, '윤개발자', 'Yoon Dev', 'yoon.dev@gmail.com', 'yoon.dev@mcn.com', '010-7777-7777', '2022-01-03', NULL, '서울시 강남구 역삼로 444', 'NEWBIE', NULL, 10.0),
(1008, 1008, '한코더', 'Han Coding', 'han.code@naver.com', 'han.coding@mcn.com', '010-8888-8888', '2020-07-15', NULL, '경기도 고양시 일산동구 중앙로 555', 'EXPERIENCED', NULL, 15.0),
(1009, 1009, '송마케터', 'Song Hongbo', 'song.sns@gmail.com', 'song.hongbo@mcn.com', '010-9999-9999', '2023-02-01', NULL, '서울시 송파구 올림픽로 666', 'NEWBIE', NULL, 14.5),
(1010, 1010, '조사업가', 'Jo Business', 'jo.biz@kakao.com', 'jo.business@mcn.com', '010-1010-1010', '2019-08-20', NULL, '서울시 중구 세종대로 777', 'EXPERIENCED', NULL, 10.0),
(1011, 1011, '신경영인', 'Shin Gyeong', 'shin.admin@naver.com', 'shin.gyeong@mcn.com', '010-1212-1212', '2021-12-01', NULL, '서울시 종로구 종로 888', 'NEWBIE', NULL, 15.0),
(1012, 1012, 'Emma', 'Emma Wilson', 'emma.wilson@gmail.com', 'emma.wilson@mcn.com', '010-1313-1313', '2022-05-10', NULL, '서울시 용산구 이태원로 999', 'EXPERIENCED', NULL, 15.0),
(1013, 1013, '문디자이너', 'Moon Thumbnail', 'moon.thumb@gmail.com', 'moon.thumbnail@mcn.com', '010-1414-1414', '2023-04-01', NULL, '경기도 수원시 영통구 광교로 1010', 'NEWBIE', NULL, 15.0),
(1014, 1014, '구직원님', 'Gu Jikwon', 'gu.old@naver.com', 'gu.jikwon@mcn.com', '010-1515-1515', '2020-02-01', '2024-12-31', '서울시 동대문구 왕산로 1111', 'NEWBIE', '개인 사정으로 인한 퇴사', 15.0),
(1015, 1015, '이서버', 'Lee Sangbaek', 'lee.dev@gmail.com', 'lee.dev@mcn.com', '010-1616-1616', '2018-05-15', NULL, '서울시 서초구 방배동 123', 'EXPERIENCED', NULL, 20.0),
(1016, 1016, '김컷편집', 'Kim Pyunjip', 'kim.pd@gmail.com', 'kim.pd@mcn.com', '010-1717-1717', '2022-11-01', NULL, '서울시 마포구 상암동 456', 'NEWBIE', NULL, 12.0),
(1017, 1017, '최아이디어', 'Choi Gihwek', 'choi.plan@gmail.com', 'choi.plan@mcn.com', '010-1818-1818', '2021-08-20', NULL, '서울시 강남구 삼성동 789', 'EXPERIENCED', NULL, 16.0),
(1018, 1018, '권리액트', 'Kwon Front', 'kwon.fe@gmail.com', 'kwon.fe@mcn.com', '010-1919-1919', '2019-03-01', NULL, '서울시 중구 명동 111', 'EXPERIENCED', NULL, 15.0),
(1019, 1019, '박도커', 'Park Infra', 'park.ops@gmail.com', 'park.ops@mcn.com', '010-2020-2020', '2020-01-10', NULL, '서울시 성동구 성수동 222', 'EXPERIENCED', NULL, 15.0),
(1020, 1020, '이해커', 'Lee Secur', 'lee.sec@gmail.com', 'lee.sec@mcn.com', '010-2121-2121', '2023-05-15', NULL, '서울시 강동구 천호동 333', 'NEWBIE', NULL, 15.0),
(1021, 1021, '조하둡', 'Jo Data', 'jo.data@gmail.com', 'jo.data@mcn.com', '010-2222-2222', '2021-09-01', NULL, '경기도 성남시 수정구 444', 'EXPERIENCED', NULL, 15.0),
(1022, 1022, '강버그', 'Kang QA', 'kang.qa@gmail.com', 'kang.qa@mcn.com', '010-2323-2323', '2024-01-02', NULL, '서울시 금천구 가산동 555', 'NEWBIE', NULL, 15.0),
(1023, 1023, '윤작가', 'Yoon Con', 'yoon.ct@gmail.com', 'yoon.ct@mcn.com', '010-2424-2424', '2021-04-10', NULL, '서울시 양천구 목동 666', 'EXPERIENCED', NULL, 15.0),
(1024, 1024, '정타겟', 'Jung Perf', 'jung.pf@gmail.com', 'jung.pf@mcn.com', '010-2525-2525', '2022-06-20', NULL, '서울시 강서구 마곡동 777', 'NEWBIE', NULL, 15.0),
(1025, 1025, '임로고', 'Lim Brand', 'lim.br@gmail.com', 'lim.br@mcn.com', '010-2626-2626', '2020-08-15', NULL, '경기도 김포시 구래동 888', 'EXPERIENCED', NULL, 15.0),
(1026, 1026, '오문구', 'Oh Copy', 'oh.cp@gmail.com', 'oh.cp@mcn.com', '010-2727-2727', '2023-10-05', NULL, '서울시 관악구 신림동 999', 'NEWBIE', NULL, 15.0),
(1027, 1027, '서포토', 'Seo Design', 'seo.ds@gmail.com', 'seo.ds@mcn.com', '010-2828-2828', '2019-11-11', NULL, '서울시 영등포구 당산동 101', 'EXPERIENCED', NULL, 15.0),
(1028, 1028, '류매니저', 'Ryu Newbie', 'ryu.mg@gmail.com', 'ryu.mg@mcn.com', '010-2929-2929', '2024-03-01', NULL, '서울시 도봉구 창동 202', 'NEWBIE', NULL, 15.0),
(1029, 1029, '한통화', 'Han Call', 'han.sub@gmail.com', 'han.sub@mcn.com', '010-3030-3030', '2021-05-20', NULL, '서울시 노원구 상계동 303', 'EXPERIENCED', NULL, 15.0),
(1030, 1030, '백현장', 'Baek Field', 'baek.fd@gmail.com', 'baek.fd@mcn.com', '010-3131-3131', '2022-07-07', NULL, '경기도 용인시 수지구 404', 'NEWBIE', NULL, 15.0),
(1031, 1031, '고유튜브', 'Go Tube', 'go.tb@gmail.com', 'go.tb@mcn.com', '010-3232-3232', '2020-02-15', NULL, '서울시 동작구 노량진동 505', 'EXPERIENCED', NULL, 15.0),
(1032, 1032, '홍쇼츠', 'Hong Short', 'hong.sh@gmail.com', 'hong.sh@mcn.com', '010-3333-3333', '2023-08-01', NULL, '서울시 마포구 합정동 606', 'NEWBIE', NULL, 15.0),
(1033, 1033, '노제휴', 'Noh Partner', 'noh.pt@gmail.com', 'noh.pt@mcn.com', '010-3434-3434', '2019-12-01', NULL, '서울시 종로구 평창동 707', 'EXPERIENCED', NULL, 15.0),
(1034, 1034, '안계약', 'Ahn Contract', 'ahn.ct@gmail.com', 'ahn.ct@mcn.com', '010-3535-3535', '2021-10-10', NULL, '서울시 용산구 한남동 808', 'EXPERIENCED', NULL, 15.0),
(1035, 1035, '유투자', 'Yoo Invest', 'yoo.iv@gmail.com', 'yoo.iv@mcn.com', '010-3636-3636', '2020-04-05', NULL, '서울시 강남구 청담동 909', 'EXPERIENCED', NULL, 15.0),
(1036, 1036, '장영업', 'Jang Sales', 'jang.sl@gmail.com', 'jang.sl@mcn.com', '010-3737-3737', '2022-12-15', NULL, '경기도 과천시 중앙동 112', 'NEWBIE', NULL, 15.0),
(1037, 1037, '곽전략', 'Kwak Plan', 'kwak.pl@gmail.com', 'kwak.pl@mcn.com', '010-3838-3838', '2019-07-20', NULL, '서울시 서초구 양재동 113', 'EXPERIENCED', NULL, 15.0),
(1038, 1038, '현교육', 'Hyun Edu', 'hyun.ed@gmail.com', 'hyun.ed@mcn.com', '010-3939-3939', '2021-02-28', NULL, '서울시 구로구 구로동 114', 'EXPERIENCED', NULL, 15.0),
(1039, 1039, '송복지', 'Song Wel', 'song.wl@gmail.com', 'song.wl@mcn.com', '010-4040-4040', '2023-01-10', NULL, '서울시 송파구 잠실동 115', 'NEWBIE', NULL, 15.0),
(1040, 1040, '신노무', 'Shin Law', 'shin.lw@gmail.com', 'shin.lw@mcn.com', '010-4141-4141', '2020-06-15', NULL, '서울시 강북구 수유동 116', 'EXPERIENCED', NULL, 15.0),
(1041, 1041, '김재무', 'Kim Won', 'kim.wn@gmail.com', 'kim.wn@mcn.com', '010-4242-4242', '2018-09-01', NULL, '서울시 은평구 불광동 117', 'EXPERIENCED', NULL, 15.0),
(1042, 1042, '이총무', 'Lee Stuff', 'lee.st@gmail.com', 'lee.st@mcn.com', '010-4343-4343', '2024-02-20', NULL, '서울시 서대문구 신촌동 118', 'NEWBIE', NULL, 15.0),
(1043, 1043, '박시설', 'Park Fix', 'park.fx@gmail.com', 'park.fx@mcn.com', '010-4444-4444', '2022-03-10', NULL, '서울시 강남구 대치동 119', 'NEWBIE', NULL, 15.0),
(1044, 1044, '마이클', 'Michael Jordan', 'mi.jordan@gmail.com', 'mi.jordan@mcn.com', '010-4545-4545', '2019-10-10', NULL, '서울시 용산구 이태원동 120', 'EXPERIENCED', NULL, 15.0),
(1045, 1045, '사라', 'Sarah Connor', 'sa.connor@gmail.com', 'sa.connor@mcn.com', '010-4646-4646', '2021-05-15', NULL, '서울시 서초구 반포동 121', 'EXPERIENCED', NULL, 15.0),
(1046, 1046, '아키라', 'Akira Tanaka', 'ak.tanaka@gmail.com', 'ak.tanaka@mcn.com', '010-4747-4747', '2020-11-20', NULL, '서울시 영등포구 여의도동 122', 'EXPERIENCED', NULL, 15.0),
(1047, 1047, '리웨이', 'Li Wei', 'li.wei@gmail.com', 'li.wei@mcn.com', '010-4848-4848', '2023-04-05', NULL, '서울시 중구 을지로동 123', 'NEWBIE', NULL, 15.0);

-- ==========================================
-- 5. Creator Detail (크리에이터 상세)
-- ==========================================
INSERT INTO creator_detail (creator_detail_id, member_creator_id, member_manager_id, creator_subscribe, creator_category, creator_platform, creator_status, creator_main_contact) VALUES
(2001, 2001, 1003, '1250000', '게임', 'YOUTUBE', 'ACTIVE', '010-1501-1501'),
(2002, 2002, 1003, '850000', '먹방', 'YOUTUBE', 'ACTIVE', '010-1601-1601'),
(2003, 2003, 1004, '620000', '뷰티', 'YOUTUBE', 'ACTIVE', 'beauty.jini@gmail.com'),
(2004, 2004, 1004, '430000', '먹방', 'TWITCH', 'ACTIVE', '010-1801-1801'),
(2005, 2005, 1005, '980000', '게임', 'CHZZK', 'ACTIVE', 'gamer.minsu@kakao.com'),
(2006, 2006, 1005, '340000', '일상', 'INSTAGRAM', 'ACTIVE', '010-2001-2001'),
(2007, 2007, 1003, '560000', '테크', 'YOUTUBE', 'ACTIVE', 'tech.reviewer@naver.com'),
(2008, 2008, 1004, '280000', '여행', 'TIKTOK', 'RESTING', '010-2201-2201'),
(2009, 2009, 1005, '150000', '게임', 'YOUTUBE', 'RETREAT', '010-2301-2301');

-- ==========================================
-- 6. Member Profile (프로필)
-- ==========================================
INSERT INTO member_profile (profile_id, member_id, profile_image, profile_banner) VALUES
-- 직원 프로필
(1001, 1001, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1002, 1002, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1003, 1003, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1004, 1004, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1005, 1005, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1006, 1006, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1007, 1007, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1008, 1008, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1009, 1009, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1010, 1010, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1011, 1011, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1012, 1012, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1013, 1013, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1014, 1014, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1015, 1015, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1016, 1016, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1017, 1017, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1018, 1018, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1019, 1019, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1020, 1020, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1021, 1021, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1022, 1022, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1023, 1023, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1024, 1024, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1025, 1025, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1026, 1026, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1027, 1027, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1028, 1028, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1029, 1029, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1030, 1030, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1031, 1031, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1032, 1032, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1033, 1033, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1034, 1034, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1035, 1035, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1036, 1036, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1037, 1037, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1038, 1038, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1039, 1039, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1040, 1040, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1041, 1041, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1042, 1042, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1043, 1043, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1044, 1044, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1045, 1045, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1046, 1046, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),
(1047, 1047, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/0NPR8MPb/photo_1454165804606_c3d57bc86b40.avif'),

-- 크리에이터 프로필
(2001, 2001, 'https://i.postimg.cc/qBczxYv8/creator.png', 'https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=1200&q=80'),
(2002, 2002, 'https://i.postimg.cc/qBczxYv8/creator.png', 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=1200&q=80'),
(2003, 2003, 'https://i.postimg.cc/qBczxYv8/creator.png', 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=1200&q=80'),
(2004, 2004, 'https://i.postimg.cc/qBczxYv8/creator.png', 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=1200&q=80'),
(2005, 2005, 'https://i.postimg.cc/qBczxYv8/creator.png', 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=1200&q=80'),
(2006, 2006, 'https://i.postimg.cc/qBczxYv8/creator.png', 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=1200&q=80'),
(2007, 2007, 'https://i.postimg.cc/qBczxYv8/creator.png', 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=1200&q=80'),
(2008, 2008, 'https://i.postimg.cc/qBczxYv8/creator.png', 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=1200&q=80'),
(2009, 2009, 'https://i.postimg.cc/qBczxYv8/creator.png', 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=1200&q=80');

-- ==========================================
-- 7. Creator Contract (계약)
-- ==========================================
INSERT INTO creator_contract (creator_contract_id, creator_name, contract_name, contract_start, contract_end) VALUES
(2001, '감스트', '감스트 전속 계약', '2023-01-01', '2026-12-31'),
(2002, '또간집', '또간집 전속 계약', '2023-06-01', '2026-05-31'),
(2003, '뷰티지니', '뷰티지니 전속 계약', '2024-01-01', '2027-12-31'),
(2004, '먹보PD', '먹보PD 전속 계약', '2022-09-01', '2025-08-31'),
(2005, '게임왕민수', '게임왕민수 전속 계약', '2023-03-01', '2026-02-28'),
(2006, '일상브이로거', '일상브이로거 전속 계약', '2024-07-01', '2027-06-30'),
(2007, '테크리뷰어', '테크리뷰어 전속 계약', '2023-11-01', '2026-10-31');

-- ==========================================
-- 8. Creator Legal/Tax (법률/세무)
-- ==========================================
INSERT INTO creator_legal_tax (legal_tax_id, member_creator_id, legal_tax_type, legal_tax_name, legal_tax_detail, letal_tax_status) VALUES
(2001, 2001, 'TAX', '2025년 1분기 부가세 신고', '1분기 유튜브 광고 수익 및 협찬 관련 부가세 신고 필요', 'IN_PROGRESS'),
(2002, 2002, 'TAX', '2024년 종합소득세 신고', '전년도 수익 관련 종합소득세 신고 완료', 'DONE'),
(2003, 2003, 'LEGAL', '상표권 등록 건', '뷰티지니 브랜드 상표권 등록 진행 중', 'CONTACTED'),
(2004, 2004, 'TAX', '2025년 1분기 원천세 납부', '1분기 직원 급여 관련 원천세 납부 대기', 'NOT_RECEIVED'),
(2005, 2005, 'LEGAL', '게임사 제휴 계약서 검토', '게임사와의 장기 제휴 계약서 법률 검토 요청', 'IN_PROGRESS'),
(2006, 2007, 'TAX', '2025년 부가세 환급 신청', '장비 구입 관련 부가세 환급 신청', 'CONTACTED');

-- ==========================================
-- 9. Creator Promotion (광고/협찬)
-- ==========================================
INSERT INTO creator_promotion (promotion_id, member_creator_id, promotion_client, promotion_name, promotion_fee, promotion_detail, created_at, promotion_targer_date, promotion_status) VALUES
(2001, 2001, '삼성전자', '갤럭시 S24 울트라 기능 리뷰 및 시연', '8000000', '신제품 출시 기념 메인 기능 집중 리뷰 영상 제작 (15분 이상), AI 카메라 기능 실사용 시연', '2026-01-10', '2026-02-15', 'ACCEPTED'),
(2002, 2002, 'CJ제일제당', '비비고 왕교자 먹방 콘텐츠', '5000000', '비비고 왕교자 신제품 먹방 콘텐츠 제작, 맛 리뷰 및 조리 과정 포함', '2026-01-15', '2026-02-28', 'WAITING'),
(2003, 2003, '아모레퍼시픽', '설화수 신제품 체험 영상', '6500000', '설화수 신제품 라인 체험 및 사용 후기 영상, 메이크업 튜토리얼 포함', '2026-01-20', '2026-03-10', 'ACCEPTED'),
(2004, 2004, '배달의민족', '배민 신규 서비스 홍보', '4000000', '배민 신규 서비스 소개 및 실사용 라이브 방송 2시간 진행', '2026-01-12', '2026-02-20', 'WAITING'),
(2005, 2005, '넥슨', '던전앤파이터 모바일 신작 찍먹 플레이', '10000000', '캐주얼하게 게임을 즐기는 모습을 담은 라이브 방송 3시간 진행, 신규 직업 체험 콘텐츠', '2026-01-08', '2026-02-05', 'ACCEPTED'),
(2006, 2007, 'LG전자', 'LG 그램 2026 신제품 리뷰', '7000000', 'LG 그램 신모델 상세 리뷰 영상 제작, 성능 테스트 및 비교 분석 포함', '2026-01-18', '2026-03-01', 'WAITING'),
(2007, 2001, '나이키', '나이키 에어맥스 신제품 홍보', '6000000', '나이키 에어맥스 신제품 착화 리뷰 및 운동 콘텐츠', '2026-01-05', '2026-02-10', 'REJECTED'),
(2008, 2003, '에스티로더', '에스티로더 어드밴스드 나이트 리페어', '5500000', '에스티로더 대표 제품 사용 후기 및 스킨케어 루틴 영상', '2026-02-02', '2026-01-31', 'ACCEPTED');


-- ==========================================
-- 11. Team (팀)
-- ==========================================
INSERT INTO team (team_id, team_name, team_detail) VALUES
(1001, '감스트 크루', '감스트를 중심으로 한 게임 콘텐츠 제작팀'),
(1002, '뷰티 컬렉티브', '뷰티 크리에이터들의 협업 팀'),
(1003, '먹방 유니버스', '먹방 크리에이터 연합'),
(1004, '테크 리뷰어즈', '테크 리뷰어들의 정보 공유 팀');

-- ==========================================
-- 12. Team Relay (팀 멤버 관계) - 수정본
-- 작성일: 2026-02-03
-- 규칙: 크리에이터(2000번대)는 팀당 1명, 직원(1000번대)은 다수 팀 소속 가능
-- ==========================================

INSERT INTO team_relay (team_relay_id, team_id, member_id) VALUES
-- [1001번 팀: 감스트 크루] (기존: 감스트, 게임왕민수 소속 -> 게임왕민수는 타 팀으로 이동 권장하나, 일단 유지 시 직원 추가)
(1001, 1001, 1003), -- 박매니저 (1001번 팀 추가 소속)
(1002, 1001, 1006),
(1003, 1001, 2001), -- 감스트 (1001번 팀 추가 소속)

-- [1002번 팀: 뷰티 컬렉티브] (기존: 뷰티지니)
-- *규칙에 따라 일상브이로거(2006)가 있으므로 직원만 추가
(1004, 1002, 1004), -- 최담당 (1002번 팀 추가 소속)
(1005, 1002, 1006), -- 강마케팅 (1002번 팀 추가 소속)
(1006, 1002, 2003), -- 뷰티지니 (1002번 팀 추가 소속)

-- [1003번 팀: 먹방 유니버스] (기존: 또간집, 먹보PD 소속)
(1007, 1003, 1003), -- 박매니저 (중복 소속: 1001, 1003)
(1008, 1003, 1005), -- 정관리 (1003번 팀 추가 소속)
(1009, 1003, 2002), -- 또간집 (1003번 팀 추가 소속)

-- [1004번 팀: 테크 리뷰어즈] (기존: 테크리뷰어)
(1010, 1004, 1007), -- 윤개발 (1004번 팀 추가 소속)
(1012, 1004, 2007); -- 테크리뷰어 (1004번 팀 추가 소속)


-- ==========================================
-- 13. Schedule (일정)
-- ==========================================
INSERT INTO schedule (schedule_id, member_id, creator_id, schedule_name, schedule_date, schedule_detail, schedule_type) VALUES
-- 크리에이터 본인 등록 일정 (creator_id는 NULL)
(1001, 2001, NULL, '삼성 갤럭시 광고 촬영', '2026-02-10', '삼성전자 본사 방문, 갤럭시 S24 울트라 리뷰 촬영', 'PROMOTION'),
(1002, 2002, NULL, '비비고 먹방 촬영', '2026-02-20', '비비고 왕교자 신제품 먹방 콘텐츠 촬영', 'CONTENT'),
(1003, 2003, NULL, '설화수 체험단 행사', '2026-03-05', '아모레퍼시픽 본사 방문, 설화수 신제품 체험', 'PROMOTION'),
(1004, 2005, NULL, '던파 모바일 라이브 방송', '2026-02-05', '넥슨 던전앤파이터 모바일 신작 라이브 방송 3시간', 'LIVE'),
(1006, 2001, NULL, '게임왕민수 합방', '2026-02-15', '게임왕민수와 함께하는 합방 콘텐츠', 'MERGE'),
(1007, 2005, NULL, '게임왕민수 합방', '2026-02-15', '감스트와 함께하는 합방 콘텐츠', 'MERGE'),
(1010, 2006, NULL, '일상 브이로그 촬영', '2026-01-30', '카페 투어 브이로그 촬영', 'CONTENT'),
(1011, 2007, NULL, 'LG 그램 제품 발표회', '2026-02-28', 'LG전자 신제품 발표회 참석 및 취재', 'MEETING'),
(1012, 2003, NULL, '뷰티 세미나 참석', '2026-02-12', '2026 K-뷰티 트렌드 세미나 참석', 'PERSONAL'),
(1013, 2004, NULL, '배민 라이브 방송', '2026-02-20', '배달의민족 신규 서비스 소개 라이브', 'LIVE'),

-- 일반 직원/사내 일정 (creator_id is NULL)
(1005, 1003, NULL, '월간 크리에이터 미팅', '2026-02-03', '담당 크리에이터들과 2월 스케줄 조율 및 피드백', 'MEETING'),
(1008, 1001, NULL, '전사 워크샵', '2026-03-15', '2026년 상반기 전사 워크샵 (제주도)', 'COMPANY'),
(1009, 1007, NULL, '시스템 점검', '2026-02-01', '서버 및 시스템 정기 점검', 'COMPANY'),
(1014, 1006, NULL, '마케팅 전략 회의', '2026-02-05', '1분기 마케팅 전략 수립 회의', 'MEETING'),
(1015, 1011, NULL, '법인카드 정산', '2026-01-31', '1월 법인카드 사용 내역 정산', 'COMPANY'),

-- 매니저(1003, 1004, 1005)가 크리에이터(2000s) 일정 등록 (creator_id에 대상 크리에이터 기입)
(1016, 1003, 2001, '감스트 기획 미팅 (매니저 등록)', '2026-02-05', '신규 콘텐츠 사업 관련 세부 기획 미팅', 'MEETING'),
(1017, 1003, 2002, '또간집 촬영 지원 (매니저 등록)', '2026-02-12', '지방 촬영 현장 케어 및 진행 보조', 'CONTENT'),
(1018, 1004, 2003, '뷰티지니 광고 조율 (매니저 등록)', '2026-02-14', '아모레 협찬 세부 사항 매니저 대행 논의', 'PROMOTION'),
(1019, 1005, 2005, '게임왕민수 합방 준비 (매니저 등록)', '2026-02-18', '타 크리에이터 협업 방송 장비 및 스튜디오 세팅', 'MERGE'),
(1020, 1003, 2007, '테크리뷰어 신작 리뷰 (매니저 등록)', '2026-02-22', '신규 스마트폰 언박싱 및 리뷰 일정 관리', 'CONTENT'),

-- 박매니저(1003) 스케줄 추가 (creator_id에 대상 크리에이터 기입)
(1021, 1003, 2001, '감스트 2월 야외 촬영 보조', '2026-02-07', '매니저 현장 동행 및 소품 준비', 'CONTENT'),
(1022, 1003, 2001, '감스트 신규 플랫폼 미팅', '2026-02-08', '플랫폼 관계자 미팅 및 계약 조건 대행', 'MEETING'),
(1023, 1003, 2002, '또간집 2월 라이브 방송 세팅', '2026-02-13', '방송 장비 점검 및 매니저 모니터링', 'LIVE'),
(1024, 1003, 2002, '또간집 광고주 미팅', '2026-02-15', '식품 광고주 관련 단가 협의', 'MEETING'),
(1025, 1003, 2007, '테크리뷰어 박람회 동행', '2026-02-20', 'IT 전시회 취재 보조 및 매니징', 'MEETING'),
(1026, 1003, 2007, '테크리뷰어 장비 구입 상담', '2026-02-25', '법인 명의 촬영 장비 리스 상담', 'ETC'),
(1027, 1003, 2001, '감스트 광고 2차 촬영', '2026-03-02', '추가 컷 촬영 및 일정 관리', 'PROMOTION'),
(1028, 1003, 2002, '또간집 맛집 섭외 미팅', '2026-03-04', '3월 촬영지 선정을 위한 매니저 사전 방문', 'CONTENT'),
(1029, 1003, 2007, '테크리뷰어 콘텐츠 피드백', '2026-03-06', '지난달 영상 성과 지표 분석 미팅', 'MEETING'),
(1030, 1003, 2001, '감스트 휴식기 스케줄 조율', '2026-03-10', '장기 휴방 대비 사전 녹화 일정 조절', 'ETC'),

-- 박매니저(1003) 개인 일정 추가
(1031, 1003, NULL, '개인 운동', '2026-02-03', '회사 헬스장에서 오전 운동', 'PERSONAL'),
(1032, 1003, NULL, '치과 정기검진', '2026-02-05', '점심시간 이용 치과 방문', 'PERSONAL'),
(1033, 1003, NULL, '부모님 생신 준비', '2026-02-10', '선물 구입 및 예약', 'PERSONAL'),
(1034, 1003, NULL, '개인 운동', '2026-02-12', '회사 헬스장에서 저녁 운동', 'PERSONAL'),
(1035, 1003, NULL, '자기계발 스터디', '2026-02-14', '매니지먼트 관련 온라인 강의 수강', 'PERSONAL'),
(1036, 1003, NULL, '동창회 모임', '2026-02-16', '대학 동기들과 저녁 식사', 'PERSONAL'),
(1037, 1003, NULL, '개인 운동', '2026-02-19', '회사 헬스장에서 오전 운동', 'PERSONAL'),
(1038, 1003, NULL, '독서 모임', '2026-02-22', '월간 북클럽 참석 - 경영 서적', 'PERSONAL'),
(1039, 1003, NULL, '개인 운동', '2026-02-26', '회사 헬스장에서 저녁 운동', 'PERSONAL'),
(1040, 1003, NULL, '부동산 상담', '2026-02-28', '아파트 투자 관련 공인중개사 미팅', 'PERSONAL'),
(1041, 1003, NULL, '가족 여행 계획', '2026-03-01', '3월 말 가족 여행지 예약 및 일정 조율', 'PERSONAL'),
(1042, 1003, NULL, '개인 운동', '2026-03-03', '회사 헬스장에서 오전 운동', 'PERSONAL'),
(1043, 1003, NULL, '자동차 정기점검', '2026-03-05', '차량 점검 및 오일 교환', 'PERSONAL'),
(1044, 1003, NULL, '요리 클래스', '2026-03-07', '주말 요리 강습 참여 - 일식 기초', 'PERSONAL'),
(1045, 1003, NULL, '개인 운동', '2026-03-09', '회사 헬스장에서 저녁 운동', 'PERSONAL');
-- ==========================================
-- 14. Schedule Visitor (일정 참가자)
-- ==========================================
INSERT INTO schedule_visitor (schedule_visitor_id, schedule_id, member_id) VALUES
(1001, 1006, 2001),
(1002, 1006, 2005),
(1003, 1007, 2001),
(1004, 1007, 2005);



-- ==========================================
-- 15. Vacation (휴가)
-- ==========================================
INSERT INTO vacation (vacation_id, member_id, vacation_type, vacation_start, vacation_end, vacation_request, vacation_detail, vacation_approve, vacation_rejected, vacation_days) VALUES
(1001, 1007, 'ANNUAL', '2026-01-20', '2026-01-24', '2026-01-10', '개인 여행', 'APPROVED', NULL, 5.0),
(1002, 1009, 'HALF', '2026-01-15', '2026-01-15', '2026-01-10', '오후 병원 방문', 'APPROVED', NULL, 0.5),
(1003, 1011, 'FAMILY', '2026-02-10', '2026-02-11', '2026-01-28', '조모 칠순 잔치', 'APPROVE_NEED', NULL, 2.0),
(1004, 1008, 'SICK', '2026-01-13', '2026-01-14', '2026-01-12', '독감 증상으로 병가 신청', 'APPROVED', NULL, 2.0),
(1005, 1012, 'WORKATION', '2026-02-10', '2026-02-17', '2026-01-20', '홍콩 거래처 미팅 및 원격 근무', 'APPROVE_NEED', NULL, 8.0),
(1006, 1010, 'ANNUAL', '2026-02-02', '2026-02-06', '2026-01-15', '봄 휴가', 'APPROVED', NULL, 5.0),
(1007, 1013, 'HALF', '2026-01-22', '2026-01-22', '2026-01-15', '오전 개인 사정', 'REJECTED', '업무 일정상 해당 날짜 반차 불가', 0.5),
(1008, 1004, 'ANNUAL', '2026-02-20', '2026-02-22', '2026-02-05', '가족 행사 참석', 'APPROVE_NEED', NULL, 3.0),
(1009, 1003, 'WORKATION', '2026-02-15', '2026-02-19', '2026-01-25', '제주 오피스 근무 및 크리에이터 여행 브이로그 보조', 'APPROVED', NULL, 5.0);

-- ==========================================
-- 16. Vacation Detail (경조사/병가/워케이션)
-- ==========================================
INSERT INTO vacation_family (family_id, vacation_id, family_relation, family_detail) VALUES
(1001, 1003, '조모', '칠순 잔치');

INSERT INTO vacation_sick (sick_id, vacation_id, sick_detail, sick_hospital) VALUES
(1001, 1004, '고열(38.5도), 기침, 인후통 증상. 독감 의심', '강남 세브란스 병원');

INSERT INTO vacation_workation (workation_id, vacation_id, workation_where, workation_contact, workation_plan, workation_handover) VALUES
(1001, 1005, '홍콩 오피스', '010-1313-1313', '홍콩 거래처 미팅 (2/11, 2/13), 글로벌 파트너십 계약서 검토, 원격 근무를 통한 일반 업무 처리', '긴급 사항 발생 시 조사업 대리(010-1010-1010)에게 연락'),
(1002, 1009, '제주 오피스', '010-3333-3333', '크리에이터 일상브이로거 여행 브이로그 촬영 보조 (2/16-2/17), 제주 지역 협력사 미팅 (2/18), 원격 업무 수행', '담당 크리에이터 관련 긴급 사항은 최담당 매니저(010-4444-4444)에게 인계');

-- ==========================================
-- 17. Health (건강 검진)
-- ==========================================
INSERT INTO health (health_id, member_id, checkup_name, checkup_date, checkup_summanary, checkup_file_url) VALUES
(1001, 1001, '2024년 정기 건강검진', '2024-11-15', 'NORMAL_AB', 'https://cdn.mcn.com/health/kim_insa_2024.pdf'),
(1003, 1003, '2024년 정기 건강검진', '2024-10-20', 'NORMAL_B', 'https://cdn.mcn.com/health/park_manager_2024.pdf'),
(1007, 1007, '2024년 정기 건강검진', '2024-12-05', 'CAUTION', 'https://cdn.mcn.com/health/yoon_dev_2024.pdf'),
(2001, 2001, '2024년 종합 건강검진', '2024-09-10', 'NORMAL_AB', 'https://cdn.mcn.com/health/gamst_2024.pdf'),
(2002, 2002, '2024년 종합 건강검진', '2024-09-25', 'DANGER', 'https://cdn.mcn.com/health/ddoganjip_2024.pdf'),
(2003, 2003, '2024년 종합 건강검진', '2024-10-05', 'NORMAL_B', 'https://cdn.mcn.com/health/beautyjini_2024.pdf'),
(1008, 1008, '2024년 정기 건강검진', '2024-11-30', 'RETEST_NEED', 'https://cdn.mcn.com/health/han_coding_2024.pdf'),
(2005, 2005, '2024년 종합 건강검진', '2024-08-15', 'NORMAL_AB', 'https://cdn.mcn.com/health/gameking_2024.pdf');

-- ==========================================
-- 18. Creator Mental Health (정신건강)
-- ==========================================
INSERT INTO creator_metal_health (creator_mental_id, member_id, creator_mental_date, creator_mental_score) VALUES
(2001, 2001, '2025-01-15', 3),
(2002, 2002, '2025-01-15', 8),
(2003, 2003, '2025-01-15', 5),
(2004, 2004, '2025-01-15', 12),
(2005, 2005, '2025-01-15', 2),
(2006, 2006, '2025-01-15', 15),
(2007, 2007, '2025-01-15', 6),
(2008, 2008, '2025-01-15', 18),
(2009, 2001, '2024-12-15', 4),
(2010, 2002, '2024-12-15', 7),
(2011, 2003, '2024-12-15', 6),
(2012, 2005, '2024-12-15', 3);

-- ==========================================
-- 19. Attendance (근태) - 2026-01-01 ~ 2026-02-11
-- 공휴일 제외: 1/1(신정), 1/28~1/30(설날)
-- 주말 제외, 2/11은 퇴근 미처리(근무중)
-- check_in_status: NORMAL, LATE, ABSENT
-- check_out_status: EARLY_LEAVE, NORMAL, OVERTIME (NULL=미퇴근)
-- ==========================================
INSERT INTO attendance (attendance_id, member_id, attendance_date, attendance_start, attendance_end, check_in_status, check_out_status) VALUES
(5001, 1001, '2026-01-02', TIMESTAMP '2026-01-02 09:20:00', TIMESTAMP '2026-01-02 17:18:00', 'LATE', 'EARLY_LEAVE'),
(5002, 1002, '2026-01-02', TIMESTAMP '2026-01-02 08:59:00', TIMESTAMP '2026-01-02 20:22:00', 'NORMAL', 'OVERTIME'),
(5003, 1003, '2026-01-02', TIMESTAMP '2026-01-02 08:57:00', TIMESTAMP '2026-01-02 18:00:00', 'NORMAL', 'NORMAL'),
(5004, 1004, '2026-01-02', TIMESTAMP '2026-01-02 08:32:00', TIMESTAMP '2026-01-02 18:00:00', 'NORMAL', 'NORMAL'),
(5005, 1005, '2026-01-02', TIMESTAMP '2026-01-02 09:23:00', TIMESTAMP '2026-01-02 15:56:00', 'LATE', 'EARLY_LEAVE'),
(5006, 1006, '2026-01-02', TIMESTAMP '2026-01-02 08:47:00', TIMESTAMP '2026-01-02 15:41:00', 'NORMAL', 'EARLY_LEAVE'),
(5007, 1007, '2026-01-02', TIMESTAMP '2026-01-02 09:48:00', TIMESTAMP '2026-01-02 21:23:00', 'LATE', 'OVERTIME'),
(5008, 1008, '2026-01-02', NULL, NULL, 'ABSENT', NULL),
(5009, 1009, '2026-01-02', TIMESTAMP '2026-01-02 09:00:00', TIMESTAMP '2026-01-02 17:12:00', 'NORMAL', 'EARLY_LEAVE'),
(5010, 1010, '2026-01-02', NULL, NULL, 'ABSENT', NULL),
(5011, 1011, '2026-01-02', TIMESTAMP '2026-01-02 09:00:00', TIMESTAMP '2026-01-02 15:42:00', 'NORMAL', 'EARLY_LEAVE'),
(5012, 1012, '2026-01-02', TIMESTAMP '2026-01-02 09:00:00', TIMESTAMP '2026-01-02 18:00:00', 'NORMAL', 'NORMAL'),
(5013, 1013, '2026-01-02', TIMESTAMP '2026-01-02 09:17:00', TIMESTAMP '2026-01-02 22:20:00', 'LATE', 'OVERTIME'),
(5014, 1001, '2026-01-05', TIMESTAMP '2026-01-05 09:00:00', TIMESTAMP '2026-01-05 20:13:00', 'NORMAL', 'OVERTIME'),
(5015, 1002, '2026-01-05', TIMESTAMP '2026-01-05 10:05:00', TIMESTAMP '2026-01-05 18:00:00', 'LATE', 'NORMAL'),
(5016, 1003, '2026-01-05', TIMESTAMP '2026-01-05 08:53:00', TIMESTAMP '2026-01-05 18:00:00', 'NORMAL', 'NORMAL'),
(5017, 1004, '2026-01-05', TIMESTAMP '2026-01-05 09:00:00', TIMESTAMP '2026-01-05 18:00:00', 'NORMAL', 'NORMAL'),
(5018, 1005, '2026-01-05', NULL, NULL, 'ABSENT', NULL),
(5019, 1006, '2026-01-05', TIMESTAMP '2026-01-05 08:55:00', TIMESTAMP '2026-01-05 18:00:00', 'NORMAL', 'NORMAL'),
(5020, 1007, '2026-01-05', TIMESTAMP '2026-01-05 08:50:00', TIMESTAMP '2026-01-05 18:00:00', 'NORMAL', 'NORMAL'),
(5021, 1008, '2026-01-05', TIMESTAMP '2026-01-05 10:15:00', TIMESTAMP '2026-01-05 18:00:00', 'LATE', 'NORMAL'),
(5022, 1009, '2026-01-05', TIMESTAMP '2026-01-05 08:55:00', TIMESTAMP '2026-01-05 16:00:00', 'NORMAL', 'EARLY_LEAVE'),
(5023, 1010, '2026-01-05', TIMESTAMP '2026-01-05 09:00:00', TIMESTAMP '2026-01-05 18:00:00', 'NORMAL', 'NORMAL'),
(5024, 1011, '2026-01-05', NULL, NULL, 'ABSENT', NULL),
(5025, 1012, '2026-01-05', TIMESTAMP '2026-01-05 08:47:00', TIMESTAMP '2026-01-05 20:02:00', 'NORMAL', 'OVERTIME'),
(5026, 1013, '2026-01-05', NULL, NULL, 'ABSENT', NULL),
(5027, 1001, '2026-01-06', TIMESTAMP '2026-01-06 09:01:00', TIMESTAMP '2026-01-06 18:00:00', 'LATE', 'NORMAL'),
(5028, 1002, '2026-01-06', TIMESTAMP '2026-01-06 09:00:00', TIMESTAMP '2026-01-06 17:03:00', 'NORMAL', 'EARLY_LEAVE'),
(5029, 1003, '2026-01-06', NULL, NULL, 'ABSENT', NULL),
(5030, 1004, '2026-01-06', TIMESTAMP '2026-01-06 08:40:00', TIMESTAMP '2026-01-06 16:05:00', 'NORMAL', 'EARLY_LEAVE'),
(5031, 1005, '2026-01-06', NULL, NULL, 'ABSENT', NULL),
(5032, 1006, '2026-01-06', NULL, NULL, 'ABSENT', NULL),
(5033, 1007, '2026-01-06', TIMESTAMP '2026-01-06 09:00:00', TIMESTAMP '2026-01-06 18:00:00', 'NORMAL', 'NORMAL'),
(5034, 1008, '2026-01-06', NULL, NULL, 'ABSENT', NULL),
(5035, 1009, '2026-01-06', TIMESTAMP '2026-01-06 09:27:00', TIMESTAMP '2026-01-06 18:00:00', 'LATE', 'NORMAL'),
(5036, 1010, '2026-01-06', TIMESTAMP '2026-01-06 09:01:00', TIMESTAMP '2026-01-06 18:00:00', 'LATE', 'NORMAL'),
(5037, 1011, '2026-01-06', TIMESTAMP '2026-01-06 10:13:00', TIMESTAMP '2026-01-06 18:00:00', 'LATE', 'NORMAL'),
(5038, 1012, '2026-01-06', TIMESTAMP '2026-01-06 09:02:00', TIMESTAMP '2026-01-06 20:29:00', 'LATE', 'OVERTIME'),
(5039, 1013, '2026-01-06', TIMESTAMP '2026-01-06 09:00:00', TIMESTAMP '2026-01-06 19:49:00', 'NORMAL', 'OVERTIME'),
(5040, 1001, '2026-01-07', NULL, NULL, 'ABSENT', NULL),
(5041, 1002, '2026-01-07', TIMESTAMP '2026-01-07 09:00:00', TIMESTAMP '2026-01-07 18:00:00', 'NORMAL', 'NORMAL'),
(5042, 1003, '2026-01-07', TIMESTAMP '2026-01-07 09:00:00', TIMESTAMP '2026-01-07 18:00:00', 'NORMAL', 'NORMAL'),
(5043, 1004, '2026-01-07', TIMESTAMP '2026-01-07 08:35:00', TIMESTAMP '2026-01-07 15:51:00', 'NORMAL', 'EARLY_LEAVE'),
(5044, 1005, '2026-01-07', TIMESTAMP '2026-01-07 09:32:00', TIMESTAMP '2026-01-07 22:30:00', 'LATE', 'OVERTIME'),
(5045, 1006, '2026-01-07', TIMESTAMP '2026-01-07 09:36:00', TIMESTAMP '2026-01-07 18:00:00', 'LATE', 'NORMAL'),
(5046, 1007, '2026-01-07', TIMESTAMP '2026-01-07 09:00:00', TIMESTAMP '2026-01-07 21:26:00', 'NORMAL', 'OVERTIME'),
(5047, 1008, '2026-01-07', TIMESTAMP '2026-01-07 09:00:00', TIMESTAMP '2026-01-07 18:00:00', 'NORMAL', 'NORMAL'),
(5048, 1009, '2026-01-07', TIMESTAMP '2026-01-07 10:27:00', TIMESTAMP '2026-01-07 21:53:00', 'LATE', 'OVERTIME'),
(5049, 1010, '2026-01-07', NULL, NULL, 'ABSENT', NULL),
(5050, 1011, '2026-01-07', TIMESTAMP '2026-01-07 09:00:00', TIMESTAMP '2026-01-07 19:02:00', 'NORMAL', 'OVERTIME');

INSERT INTO attendance (attendance_id, member_id, attendance_date, attendance_start, attendance_end, check_in_status, check_out_status) VALUES
(5051, 1012, '2026-01-07', NULL, NULL, 'ABSENT', NULL),
(5052, 1013, '2026-01-07', TIMESTAMP '2026-01-07 09:00:00', TIMESTAMP '2026-01-07 18:00:00', 'NORMAL', 'NORMAL'),
(5053, 1001, '2026-01-08', TIMESTAMP '2026-01-08 08:50:00', TIMESTAMP '2026-01-08 15:15:00', 'NORMAL', 'EARLY_LEAVE'),
(5054, 1002, '2026-01-08', TIMESTAMP '2026-01-08 09:47:00', TIMESTAMP '2026-01-08 22:15:00', 'LATE', 'OVERTIME'),
(5055, 1003, '2026-01-08', TIMESTAMP '2026-01-08 09:17:00', TIMESTAMP '2026-01-08 15:23:00', 'LATE', 'EARLY_LEAVE'),
(5056, 1004, '2026-01-08', TIMESTAMP '2026-01-08 10:20:00', TIMESTAMP '2026-01-08 18:00:00', 'LATE', 'NORMAL'),
(5057, 1005, '2026-01-08', TIMESTAMP '2026-01-08 09:15:00', TIMESTAMP '2026-01-08 19:43:00', 'LATE', 'OVERTIME'),
(5058, 1006, '2026-01-08', TIMESTAMP '2026-01-08 08:52:00', TIMESTAMP '2026-01-08 19:13:00', 'NORMAL', 'OVERTIME'),
(5059, 1007, '2026-01-08', TIMESTAMP '2026-01-08 09:45:00', TIMESTAMP '2026-01-08 18:00:00', 'LATE', 'NORMAL'),
(5060, 1008, '2026-01-08', TIMESTAMP '2026-01-08 09:00:00', TIMESTAMP '2026-01-08 18:00:00', 'NORMAL', 'NORMAL'),
(5061, 1009, '2026-01-08', TIMESTAMP '2026-01-08 09:15:00', TIMESTAMP '2026-01-08 19:53:00', 'LATE', 'OVERTIME'),
(5062, 1010, '2026-01-08', TIMESTAMP '2026-01-08 09:00:00', TIMESTAMP '2026-01-08 20:27:00', 'NORMAL', 'OVERTIME'),
(5063, 1011, '2026-01-08', TIMESTAMP '2026-01-08 09:00:00', TIMESTAMP '2026-01-08 18:00:00', 'NORMAL', 'NORMAL'),
(5064, 1012, '2026-01-08', TIMESTAMP '2026-01-08 10:02:00', TIMESTAMP '2026-01-08 18:00:00', 'LATE', 'NORMAL'),
(5065, 1013, '2026-01-08', NULL, NULL, 'ABSENT', NULL),
(5066, 1001, '2026-01-09', TIMESTAMP '2026-01-09 09:00:00', TIMESTAMP '2026-01-09 17:10:00', 'NORMAL', 'EARLY_LEAVE'),
(5067, 1002, '2026-01-09', NULL, NULL, 'ABSENT', NULL),
(5068, 1003, '2026-01-09', TIMESTAMP '2026-01-09 10:04:00', TIMESTAMP '2026-01-09 22:54:00', 'LATE', 'OVERTIME'),
(5069, 1004, '2026-01-09', NULL, NULL, 'ABSENT', NULL),
(5070, 1005, '2026-01-09', TIMESTAMP '2026-01-09 09:00:00', TIMESTAMP '2026-01-09 17:17:00', 'NORMAL', 'EARLY_LEAVE'),
(5071, 1006, '2026-01-09', TIMESTAMP '2026-01-09 09:32:00', TIMESTAMP '2026-01-09 19:37:00', 'LATE', 'OVERTIME'),
(5072, 1007, '2026-01-09', TIMESTAMP '2026-01-09 08:56:00', TIMESTAMP '2026-01-09 22:49:00', 'NORMAL', 'OVERTIME'),
(5073, 1008, '2026-01-09', TIMESTAMP '2026-01-09 10:03:00', TIMESTAMP '2026-01-09 15:01:00', 'LATE', 'EARLY_LEAVE'),
(5074, 1009, '2026-01-09', NULL, NULL, 'ABSENT', NULL),
(5075, 1010, '2026-01-09', TIMESTAMP '2026-01-09 09:03:00', TIMESTAMP '2026-01-09 17:10:00', 'LATE', 'EARLY_LEAVE'),
(5076, 1011, '2026-01-09', TIMESTAMP '2026-01-09 09:00:00', TIMESTAMP '2026-01-09 20:48:00', 'NORMAL', 'OVERTIME'),
(5077, 1012, '2026-01-09', TIMESTAMP '2026-01-09 08:36:00', TIMESTAMP '2026-01-09 18:00:00', 'NORMAL', 'NORMAL'),
(5078, 1013, '2026-01-09', TIMESTAMP '2026-01-09 09:00:00', TIMESTAMP '2026-01-09 18:00:00', 'NORMAL', 'NORMAL'),
(5079, 1001, '2026-01-12', TIMESTAMP '2026-01-12 09:48:00', TIMESTAMP '2026-01-12 19:49:00', 'LATE', 'OVERTIME'),
(5080, 1002, '2026-01-12', TIMESTAMP '2026-01-12 09:00:00', TIMESTAMP '2026-01-12 18:00:00', 'NORMAL', 'NORMAL'),
(5081, 1003, '2026-01-12', TIMESTAMP '2026-01-12 10:16:00', TIMESTAMP '2026-01-12 18:00:00', 'LATE', 'NORMAL'),
(5082, 1004, '2026-01-12', NULL, NULL, 'ABSENT', NULL),
(5083, 1005, '2026-01-12', TIMESTAMP '2026-01-12 09:00:00', TIMESTAMP '2026-01-12 18:00:00', 'NORMAL', 'NORMAL'),
(5084, 1006, '2026-01-12', TIMESTAMP '2026-01-12 10:18:00', TIMESTAMP '2026-01-12 18:00:00', 'LATE', 'NORMAL'),
(5085, 1007, '2026-01-12', TIMESTAMP '2026-01-12 09:58:00', TIMESTAMP '2026-01-12 17:17:00', 'LATE', 'EARLY_LEAVE'),
(5086, 1008, '2026-01-12', TIMESTAMP '2026-01-12 09:46:00', TIMESTAMP '2026-01-12 21:25:00', 'LATE', 'OVERTIME'),
(5087, 1009, '2026-01-12', TIMESTAMP '2026-01-12 08:58:00', TIMESTAMP '2026-01-12 15:50:00', 'NORMAL', 'EARLY_LEAVE'),
(5088, 1010, '2026-01-12', TIMESTAMP '2026-01-12 09:00:00', TIMESTAMP '2026-01-12 19:33:00', 'NORMAL', 'OVERTIME'),
(5089, 1011, '2026-01-12', TIMESTAMP '2026-01-12 09:07:00', TIMESTAMP '2026-01-12 15:28:00', 'LATE', 'EARLY_LEAVE'),
(5090, 1012, '2026-01-12', TIMESTAMP '2026-01-12 09:00:00', TIMESTAMP '2026-01-12 18:00:00', 'NORMAL', 'NORMAL'),
(5091, 1013, '2026-01-12', TIMESTAMP '2026-01-12 10:08:00', TIMESTAMP '2026-01-12 19:57:00', 'LATE', 'OVERTIME'),
(5092, 1001, '2026-01-13', TIMESTAMP '2026-01-13 09:00:00', TIMESTAMP '2026-01-13 18:00:00', 'NORMAL', 'NORMAL'),
(5093, 1002, '2026-01-13', TIMESTAMP '2026-01-13 09:00:00', TIMESTAMP '2026-01-13 18:00:00', 'NORMAL', 'NORMAL'),
(5094, 1003, '2026-01-13', TIMESTAMP '2026-01-13 08:46:00', TIMESTAMP '2026-01-13 15:14:00', 'NORMAL', 'EARLY_LEAVE'),
(5095, 1004, '2026-01-13', TIMESTAMP '2026-01-13 09:35:00', TIMESTAMP '2026-01-13 19:52:00', 'LATE', 'OVERTIME'),
(5096, 1005, '2026-01-13', NULL, NULL, 'ABSENT', NULL),
(5097, 1006, '2026-01-13', TIMESTAMP '2026-01-13 09:00:00', TIMESTAMP '2026-01-13 18:00:00', 'NORMAL', 'NORMAL'),
(5098, 1007, '2026-01-13', TIMESTAMP '2026-01-13 08:47:00', TIMESTAMP '2026-01-13 16:40:00', 'NORMAL', 'EARLY_LEAVE'),
(5099, 1008, '2026-01-13', NULL, NULL, 'ABSENT', NULL),
(5100, 1009, '2026-01-13', TIMESTAMP '2026-01-13 09:58:00', TIMESTAMP '2026-01-13 16:08:00', 'LATE', 'EARLY_LEAVE');

INSERT INTO attendance (attendance_id, member_id, attendance_date, attendance_start, attendance_end, check_in_status, check_out_status) VALUES
(5101, 1010, '2026-01-13', TIMESTAMP '2026-01-13 10:15:00', TIMESTAMP '2026-01-13 18:00:00', 'LATE', 'NORMAL'),
(5102, 1011, '2026-01-13', TIMESTAMP '2026-01-13 09:53:00', TIMESTAMP '2026-01-13 18:00:00', 'LATE', 'NORMAL'),
(5103, 1012, '2026-01-13', TIMESTAMP '2026-01-13 09:00:00', TIMESTAMP '2026-01-13 20:06:00', 'NORMAL', 'OVERTIME'),
(5104, 1013, '2026-01-13', NULL, NULL, 'ABSENT', NULL),
(5105, 1001, '2026-01-14', TIMESTAMP '2026-01-14 08:36:00', TIMESTAMP '2026-01-14 18:00:00', 'NORMAL', 'NORMAL'),
(5106, 1002, '2026-01-14', TIMESTAMP '2026-01-14 08:59:00', TIMESTAMP '2026-01-14 18:00:00', 'NORMAL', 'NORMAL'),
(5107, 1003, '2026-01-14', TIMESTAMP '2026-01-14 10:11:00', TIMESTAMP '2026-01-14 18:00:00', 'LATE', 'NORMAL'),
(5108, 1004, '2026-01-14', TIMESTAMP '2026-01-14 09:00:00', TIMESTAMP '2026-01-14 20:46:00', 'NORMAL', 'OVERTIME'),
(5109, 1005, '2026-01-14', TIMESTAMP '2026-01-14 08:43:00', TIMESTAMP '2026-01-14 18:00:00', 'NORMAL', 'NORMAL'),
(5110, 1006, '2026-01-14', NULL, NULL, 'ABSENT', NULL),
(5111, 1007, '2026-01-14', TIMESTAMP '2026-01-14 09:00:00', TIMESTAMP '2026-01-14 22:55:00', 'NORMAL', 'OVERTIME'),
(5112, 1008, '2026-01-14', TIMESTAMP '2026-01-14 09:00:00', TIMESTAMP '2026-01-14 16:33:00', 'NORMAL', 'EARLY_LEAVE'),
(5113, 1009, '2026-01-14', TIMESTAMP '2026-01-14 10:04:00', TIMESTAMP '2026-01-14 18:00:00', 'LATE', 'NORMAL'),
(5114, 1010, '2026-01-14', TIMESTAMP '2026-01-14 09:22:00', TIMESTAMP '2026-01-14 18:00:00', 'LATE', 'NORMAL'),
(5115, 1011, '2026-01-14', TIMESTAMP '2026-01-14 09:00:00', TIMESTAMP '2026-01-14 18:00:00', 'NORMAL', 'NORMAL'),
(5116, 1012, '2026-01-14', TIMESTAMP '2026-01-14 08:46:00', TIMESTAMP '2026-01-14 17:08:00', 'NORMAL', 'EARLY_LEAVE'),
(5117, 1013, '2026-01-14', NULL, NULL, 'ABSENT', NULL),
(5118, 1001, '2026-01-15', TIMESTAMP '2026-01-15 09:23:00', TIMESTAMP '2026-01-15 18:00:00', 'LATE', 'NORMAL'),
(5119, 1002, '2026-01-15', TIMESTAMP '2026-01-15 09:30:00', TIMESTAMP '2026-01-15 18:00:00', 'LATE', 'NORMAL'),
(5120, 1003, '2026-01-15', NULL, NULL, 'ABSENT', NULL),
(5121, 1004, '2026-01-15', TIMESTAMP '2026-01-15 08:42:00', TIMESTAMP '2026-01-15 21:27:00', 'NORMAL', 'OVERTIME'),
(5122, 1005, '2026-01-15', NULL, NULL, 'ABSENT', NULL),
(5123, 1006, '2026-01-15', TIMESTAMP '2026-01-15 09:26:00', TIMESTAMP '2026-01-15 17:15:00', 'LATE', 'EARLY_LEAVE'),
(5124, 1007, '2026-01-15', NULL, NULL, 'ABSENT', NULL),
(5125, 1008, '2026-01-15', TIMESTAMP '2026-01-15 09:50:00', TIMESTAMP '2026-01-15 20:34:00', 'LATE', 'OVERTIME'),
(5126, 1009, '2026-01-15', TIMESTAMP '2026-01-15 09:00:00', TIMESTAMP '2026-01-15 18:00:00', 'NORMAL', 'NORMAL'),
(5127, 1010, '2026-01-15', NULL, NULL, 'ABSENT', NULL),
(5128, 1011, '2026-01-15', TIMESTAMP '2026-01-15 09:00:00', TIMESTAMP '2026-01-15 15:59:00', 'NORMAL', 'EARLY_LEAVE'),
(5129, 1012, '2026-01-15', TIMESTAMP '2026-01-15 09:00:00', TIMESTAMP '2026-01-15 18:00:00', 'NORMAL', 'NORMAL'),
(5130, 1013, '2026-01-15', TIMESTAMP '2026-01-15 09:17:00', TIMESTAMP '2026-01-15 15:10:00', 'LATE', 'EARLY_LEAVE'),
(5131, 1001, '2026-01-16', TIMESTAMP '2026-01-16 09:00:00', TIMESTAMP '2026-01-16 18:00:00', 'NORMAL', 'NORMAL'),
(5132, 1002, '2026-01-16', TIMESTAMP '2026-01-16 09:00:00', TIMESTAMP '2026-01-16 18:00:00', 'NORMAL', 'NORMAL'),
(5133, 1003, '2026-01-16', TIMESTAMP '2026-01-16 09:45:00', TIMESTAMP '2026-01-16 18:00:00', 'LATE', 'NORMAL'),
(5134, 1004, '2026-01-16', NULL, NULL, 'ABSENT', NULL),
(5135, 1005, '2026-01-16', TIMESTAMP '2026-01-16 09:02:00', TIMESTAMP '2026-01-16 16:37:00', 'LATE', 'EARLY_LEAVE'),
(5136, 1006, '2026-01-16', TIMESTAMP '2026-01-16 08:59:00', TIMESTAMP '2026-01-16 21:15:00', 'NORMAL', 'OVERTIME'),
(5137, 1007, '2026-01-16', TIMESTAMP '2026-01-16 09:00:00', TIMESTAMP '2026-01-16 20:27:00', 'NORMAL', 'OVERTIME'),
(5138, 1008, '2026-01-16', TIMESTAMP '2026-01-16 09:24:00', TIMESTAMP '2026-01-16 18:00:00', 'LATE', 'NORMAL'),
(5139, 1009, '2026-01-16', TIMESTAMP '2026-01-16 09:08:00', TIMESTAMP '2026-01-16 18:00:00', 'LATE', 'NORMAL'),
(5140, 1010, '2026-01-16', TIMESTAMP '2026-01-16 09:24:00', TIMESTAMP '2026-01-16 21:02:00', 'LATE', 'OVERTIME'),
(5141, 1011, '2026-01-16', TIMESTAMP '2026-01-16 09:20:00', TIMESTAMP '2026-01-16 18:00:00', 'LATE', 'NORMAL'),
(5142, 1012, '2026-01-16', TIMESTAMP '2026-01-16 09:00:00', TIMESTAMP '2026-01-16 18:00:00', 'NORMAL', 'NORMAL'),
(5143, 1013, '2026-01-16', TIMESTAMP '2026-01-16 09:04:00', TIMESTAMP '2026-01-16 19:27:00', 'LATE', 'OVERTIME'),
(5144, 1001, '2026-01-19', TIMESTAMP '2026-01-19 09:00:00', TIMESTAMP '2026-01-19 18:00:00', 'NORMAL', 'NORMAL'),
(5145, 1002, '2026-01-19', TIMESTAMP '2026-01-19 09:51:00', TIMESTAMP '2026-01-19 16:18:00', 'LATE', 'EARLY_LEAVE'),
(5146, 1003, '2026-01-19', TIMESTAMP '2026-01-19 09:40:00', TIMESTAMP '2026-01-19 19:10:00', 'LATE', 'OVERTIME'),
(5147, 1004, '2026-01-19', NULL, NULL, 'ABSENT', NULL),
(5148, 1005, '2026-01-19', TIMESTAMP '2026-01-19 09:00:00', TIMESTAMP '2026-01-19 22:03:00', 'NORMAL', 'OVERTIME'),
(5149, 1006, '2026-01-19', TIMESTAMP '2026-01-19 09:00:00', TIMESTAMP '2026-01-19 18:00:00', 'NORMAL', 'NORMAL'),
(5150, 1007, '2026-01-19', TIMESTAMP '2026-01-19 09:31:00', TIMESTAMP '2026-01-19 18:00:00', 'LATE', 'NORMAL');

INSERT INTO attendance (attendance_id, member_id, attendance_date, attendance_start, attendance_end, check_in_status, check_out_status) VALUES
(5151, 1008, '2026-01-19', TIMESTAMP '2026-01-19 09:00:00', TIMESTAMP '2026-01-19 22:16:00', 'NORMAL', 'OVERTIME'),
(5152, 1009, '2026-01-19', TIMESTAMP '2026-01-19 10:00:00', TIMESTAMP '2026-01-19 17:11:00', 'LATE', 'EARLY_LEAVE'),
(5153, 1010, '2026-01-19', TIMESTAMP '2026-01-19 08:31:00', TIMESTAMP '2026-01-19 18:00:00', 'NORMAL', 'NORMAL'),
(5154, 1011, '2026-01-19', TIMESTAMP '2026-01-19 09:00:00', TIMESTAMP '2026-01-19 16:34:00', 'NORMAL', 'EARLY_LEAVE'),
(5155, 1012, '2026-01-19', TIMESTAMP '2026-01-19 09:17:00', TIMESTAMP '2026-01-19 16:24:00', 'LATE', 'EARLY_LEAVE'),
(5156, 1013, '2026-01-19', TIMESTAMP '2026-01-19 09:16:00', TIMESTAMP '2026-01-19 18:00:00', 'LATE', 'NORMAL'),
(5157, 1001, '2026-01-20', TIMESTAMP '2026-01-20 09:00:00', TIMESTAMP '2026-01-20 22:02:00', 'NORMAL', 'OVERTIME'),
(5158, 1002, '2026-01-20', TIMESTAMP '2026-01-20 09:00:00', TIMESTAMP '2026-01-20 18:00:00', 'NORMAL', 'NORMAL'),
(5159, 1003, '2026-01-20', TIMESTAMP '2026-01-20 10:07:00', TIMESTAMP '2026-01-20 18:00:00', 'LATE', 'NORMAL'),
(5160, 1004, '2026-01-20', TIMESTAMP '2026-01-20 09:46:00', TIMESTAMP '2026-01-20 20:30:00', 'LATE', 'OVERTIME'),
(5161, 1005, '2026-01-20', TIMESTAMP '2026-01-20 08:37:00', TIMESTAMP '2026-01-20 18:00:00', 'NORMAL', 'NORMAL'),
(5162, 1006, '2026-01-20', TIMESTAMP '2026-01-20 10:08:00', TIMESTAMP '2026-01-20 18:00:00', 'LATE', 'NORMAL'),
(5163, 1007, '2026-01-20', NULL, NULL, 'ABSENT', NULL),
(5164, 1008, '2026-01-20', TIMESTAMP '2026-01-20 08:52:00', TIMESTAMP '2026-01-20 18:00:00', 'NORMAL', 'NORMAL'),
(5165, 1009, '2026-01-20', TIMESTAMP '2026-01-20 09:00:00', TIMESTAMP '2026-01-20 18:00:00', 'NORMAL', 'NORMAL'),
(5166, 1010, '2026-01-20', TIMESTAMP '2026-01-20 10:02:00', TIMESTAMP '2026-01-20 18:00:00', 'LATE', 'NORMAL'),
(5167, 1011, '2026-01-20', TIMESTAMP '2026-01-20 09:00:00', TIMESTAMP '2026-01-20 18:00:00', 'NORMAL', 'NORMAL'),
(5168, 1012, '2026-01-20', TIMESTAMP '2026-01-20 08:44:00', TIMESTAMP '2026-01-20 22:35:00', 'NORMAL', 'OVERTIME'),
(5169, 1013, '2026-01-20', TIMESTAMP '2026-01-20 08:42:00', TIMESTAMP '2026-01-20 17:28:00', 'NORMAL', 'EARLY_LEAVE'),
(5170, 1001, '2026-01-21', NULL, NULL, 'ABSENT', NULL),
(5171, 1002, '2026-01-21', TIMESTAMP '2026-01-21 09:31:00', TIMESTAMP '2026-01-21 22:58:00', 'LATE', 'OVERTIME'),
(5172, 1003, '2026-01-21', NULL, NULL, 'ABSENT', NULL),
(5173, 1004, '2026-01-21', TIMESTAMP '2026-01-21 08:53:00', TIMESTAMP '2026-01-21 18:00:00', 'NORMAL', 'NORMAL'),
(5174, 1005, '2026-01-21', TIMESTAMP '2026-01-21 09:30:00', TIMESTAMP '2026-01-21 22:40:00', 'LATE', 'OVERTIME'),
(5175, 1006, '2026-01-21', TIMESTAMP '2026-01-21 09:28:00', TIMESTAMP '2026-01-21 15:24:00', 'LATE', 'EARLY_LEAVE'),
(5176, 1007, '2026-01-21', TIMESTAMP '2026-01-21 08:35:00', TIMESTAMP '2026-01-21 18:00:00', 'NORMAL', 'NORMAL'),
(5177, 1008, '2026-01-21', TIMESTAMP '2026-01-21 09:00:00', TIMESTAMP '2026-01-21 18:00:00', 'NORMAL', 'NORMAL'),
(5178, 1009, '2026-01-21', TIMESTAMP '2026-01-21 09:00:00', TIMESTAMP '2026-01-21 20:21:00', 'NORMAL', 'OVERTIME'),
(5179, 1010, '2026-01-21', TIMESTAMP '2026-01-21 09:00:00', TIMESTAMP '2026-01-21 21:11:00', 'NORMAL', 'OVERTIME'),
(5180, 1011, '2026-01-21', TIMESTAMP '2026-01-21 08:30:00', TIMESTAMP '2026-01-21 18:00:00', 'NORMAL', 'NORMAL'),
(5181, 1012, '2026-01-21', TIMESTAMP '2026-01-21 09:06:00', TIMESTAMP '2026-01-21 18:00:00', 'LATE', 'NORMAL'),
(5182, 1013, '2026-01-21', TIMESTAMP '2026-01-21 09:11:00', TIMESTAMP '2026-01-21 18:00:00', 'LATE', 'NORMAL'),
(5183, 1001, '2026-01-22', TIMESTAMP '2026-01-22 09:00:00', TIMESTAMP '2026-01-22 18:00:00', 'NORMAL', 'NORMAL'),
(5184, 1002, '2026-01-22', TIMESTAMP '2026-01-22 08:54:00', TIMESTAMP '2026-01-22 18:00:00', 'NORMAL', 'NORMAL'),
(5185, 1003, '2026-01-22', TIMESTAMP '2026-01-22 08:32:00', TIMESTAMP '2026-01-22 18:00:00', 'NORMAL', 'NORMAL'),
(5186, 1004, '2026-01-22', NULL, NULL, 'ABSENT', NULL),
(5187, 1005, '2026-01-22', TIMESTAMP '2026-01-22 10:30:00', TIMESTAMP '2026-01-22 20:23:00', 'LATE', 'OVERTIME'),
(5188, 1006, '2026-01-22', TIMESTAMP '2026-01-22 09:00:00', TIMESTAMP '2026-01-22 15:48:00', 'NORMAL', 'EARLY_LEAVE'),
(5189, 1007, '2026-01-22', TIMESTAMP '2026-01-22 10:21:00', TIMESTAMP '2026-01-22 18:00:00', 'LATE', 'NORMAL'),
(5190, 1008, '2026-01-22', TIMESTAMP '2026-01-22 09:00:00', TIMESTAMP '2026-01-22 18:00:00', 'NORMAL', 'NORMAL'),
(5191, 1009, '2026-01-22', TIMESTAMP '2026-01-22 09:00:00', TIMESTAMP '2026-01-22 21:51:00', 'NORMAL', 'OVERTIME'),
(5192, 1010, '2026-01-22', TIMESTAMP '2026-01-22 09:55:00', TIMESTAMP '2026-01-22 19:14:00', 'LATE', 'OVERTIME'),
(5193, 1011, '2026-01-22', TIMESTAMP '2026-01-22 08:32:00', TIMESTAMP '2026-01-22 15:19:00', 'NORMAL', 'EARLY_LEAVE'),
(5194, 1012, '2026-01-22', TIMESTAMP '2026-01-22 10:15:00', TIMESTAMP '2026-01-22 15:42:00', 'LATE', 'EARLY_LEAVE'),
(5195, 1013, '2026-01-22', TIMESTAMP '2026-01-22 09:00:00', TIMESTAMP '2026-01-22 18:00:00', 'NORMAL', 'NORMAL'),
(5196, 1001, '2026-01-23', TIMESTAMP '2026-01-23 10:27:00', TIMESTAMP '2026-01-23 18:00:00', 'LATE', 'NORMAL'),
(5197, 1002, '2026-01-23', TIMESTAMP '2026-01-23 09:39:00', TIMESTAMP '2026-01-23 18:00:00', 'LATE', 'NORMAL'),
(5198, 1003, '2026-01-23', TIMESTAMP '2026-01-23 09:00:00', TIMESTAMP '2026-01-23 16:31:00', 'NORMAL', 'EARLY_LEAVE'),
(5199, 1004, '2026-01-23', TIMESTAMP '2026-01-23 09:00:00', TIMESTAMP '2026-01-23 17:27:00', 'NORMAL', 'EARLY_LEAVE'),
(5200, 1005, '2026-01-23', TIMESTAMP '2026-01-23 09:00:00', TIMESTAMP '2026-01-23 18:00:00', 'NORMAL', 'NORMAL');

INSERT INTO attendance (attendance_id, member_id, attendance_date, attendance_start, attendance_end, check_in_status, check_out_status) VALUES
(5201, 1006, '2026-01-23', TIMESTAMP '2026-01-23 10:13:00', TIMESTAMP '2026-01-23 20:55:00', 'LATE', 'OVERTIME'),
(5202, 1007, '2026-01-23', TIMESTAMP '2026-01-23 09:30:00', TIMESTAMP '2026-01-23 19:22:00', 'LATE', 'OVERTIME'),
(5203, 1008, '2026-01-23', TIMESTAMP '2026-01-23 09:25:00', TIMESTAMP '2026-01-23 20:16:00', 'LATE', 'OVERTIME'),
(5204, 1009, '2026-01-23', TIMESTAMP '2026-01-23 08:40:00', TIMESTAMP '2026-01-23 15:00:00', 'NORMAL', 'EARLY_LEAVE'),
(5205, 1010, '2026-01-23', TIMESTAMP '2026-01-23 09:13:00', TIMESTAMP '2026-01-23 21:45:00', 'LATE', 'OVERTIME'),
(5206, 1011, '2026-01-23', TIMESTAMP '2026-01-23 09:00:00', TIMESTAMP '2026-01-23 22:50:00', 'NORMAL', 'OVERTIME'),
(5207, 1012, '2026-01-23', TIMESTAMP '2026-01-23 09:00:00', TIMESTAMP '2026-01-23 18:00:00', 'NORMAL', 'NORMAL'),
(5208, 1013, '2026-01-23', TIMESTAMP '2026-01-23 10:24:00', TIMESTAMP '2026-01-23 18:00:00', 'LATE', 'NORMAL'),
(5209, 1001, '2026-01-26', TIMESTAMP '2026-01-26 09:00:00', TIMESTAMP '2026-01-26 18:00:00', 'NORMAL', 'NORMAL'),
(5210, 1002, '2026-01-26', TIMESTAMP '2026-01-26 09:00:00', TIMESTAMP '2026-01-26 18:00:00', 'NORMAL', 'NORMAL'),
(5211, 1003, '2026-01-26', TIMESTAMP '2026-01-26 09:20:00', TIMESTAMP '2026-01-26 20:25:00', 'LATE', 'OVERTIME'),
(5212, 1004, '2026-01-26', TIMESTAMP '2026-01-26 08:39:00', TIMESTAMP '2026-01-26 19:02:00', 'NORMAL', 'OVERTIME'),
(5213, 1005, '2026-01-26', TIMESTAMP '2026-01-26 08:59:00', TIMESTAMP '2026-01-26 18:00:00', 'NORMAL', 'NORMAL'),
(5214, 1006, '2026-01-26', TIMESTAMP '2026-01-26 09:00:00', TIMESTAMP '2026-01-26 18:00:00', 'NORMAL', 'NORMAL'),
(5215, 1007, '2026-01-26', TIMESTAMP '2026-01-26 08:33:00', TIMESTAMP '2026-01-26 19:25:00', 'NORMAL', 'OVERTIME'),
(5216, 1008, '2026-01-26', TIMESTAMP '2026-01-26 08:59:00', TIMESTAMP '2026-01-26 18:00:00', 'NORMAL', 'NORMAL'),
(5217, 1009, '2026-01-26', TIMESTAMP '2026-01-26 09:00:00', TIMESTAMP '2026-01-26 18:00:00', 'NORMAL', 'NORMAL'),
(5218, 1010, '2026-01-26', TIMESTAMP '2026-01-26 08:59:00', TIMESTAMP '2026-01-26 18:00:00', 'NORMAL', 'NORMAL'),
(5219, 1011, '2026-01-26', TIMESTAMP '2026-01-26 09:00:00', TIMESTAMP '2026-01-26 18:00:00', 'NORMAL', 'NORMAL'),
(5220, 1012, '2026-01-26', NULL, NULL, 'ABSENT', NULL),
(5221, 1013, '2026-01-26', TIMESTAMP '2026-01-26 09:59:00', TIMESTAMP '2026-01-26 18:00:00', 'LATE', 'NORMAL'),
(5222, 1001, '2026-01-27', TIMESTAMP '2026-01-27 09:46:00', TIMESTAMP '2026-01-27 17:08:00', 'LATE', 'EARLY_LEAVE'),
(5223, 1002, '2026-01-27', TIMESTAMP '2026-01-27 09:44:00', TIMESTAMP '2026-01-27 18:00:00', 'LATE', 'NORMAL'),
(5224, 1003, '2026-01-27', TIMESTAMP '2026-01-27 08:58:00', TIMESTAMP '2026-01-27 21:27:00', 'NORMAL', 'OVERTIME'),
(5225, 1004, '2026-01-27', NULL, NULL, 'ABSENT', NULL),
(5226, 1005, '2026-01-27', NULL, NULL, 'ABSENT', NULL),
(5227, 1006, '2026-01-27', NULL, NULL, 'ABSENT', NULL),
(5228, 1007, '2026-01-27', TIMESTAMP '2026-01-27 10:19:00', TIMESTAMP '2026-01-27 18:00:00', 'LATE', 'NORMAL'),
(5229, 1008, '2026-01-27', TIMESTAMP '2026-01-27 08:54:00', TIMESTAMP '2026-01-27 18:00:00', 'NORMAL', 'NORMAL'),
(5230, 1009, '2026-01-27', TIMESTAMP '2026-01-27 09:00:00', TIMESTAMP '2026-01-27 18:00:00', 'NORMAL', 'NORMAL'),
(5231, 1010, '2026-01-27', NULL, NULL, 'ABSENT', NULL),
(5232, 1011, '2026-01-27', TIMESTAMP '2026-01-27 09:00:00', TIMESTAMP '2026-01-27 19:29:00', 'NORMAL', 'OVERTIME'),
(5233, 1012, '2026-01-27', TIMESTAMP '2026-01-27 08:50:00', TIMESTAMP '2026-01-27 18:00:00', 'NORMAL', 'NORMAL'),
(5234, 1013, '2026-01-27', TIMESTAMP '2026-01-27 09:00:00', TIMESTAMP '2026-01-27 19:56:00', 'NORMAL', 'OVERTIME'),
(5235, 1001, '2026-02-02', TIMESTAMP '2026-02-02 08:53:00', TIMESTAMP '2026-02-02 18:00:00', 'NORMAL', 'NORMAL'),
(5236, 1002, '2026-02-02', TIMESTAMP '2026-02-02 09:00:00', TIMESTAMP '2026-02-02 17:24:00', 'NORMAL', 'EARLY_LEAVE'),
(5237, 1003, '2026-02-02', TIMESTAMP '2026-02-02 09:06:00', TIMESTAMP '2026-02-02 21:55:00', 'LATE', 'OVERTIME'),
(5238, 1004, '2026-02-02', TIMESTAMP '2026-02-02 09:21:00', TIMESTAMP '2026-02-02 18:00:00', 'LATE', 'NORMAL'),
(5239, 1005, '2026-02-02', TIMESTAMP '2026-02-02 09:16:00', TIMESTAMP '2026-02-02 18:00:00', 'LATE', 'NORMAL'),
(5240, 1006, '2026-02-02', TIMESTAMP '2026-02-02 09:01:00', TIMESTAMP '2026-02-02 19:24:00', 'LATE', 'OVERTIME'),
(5241, 1007, '2026-02-02', TIMESTAMP '2026-02-02 09:00:00', TIMESTAMP '2026-02-02 18:00:00', 'NORMAL', 'NORMAL'),
(5242, 1008, '2026-02-02', TIMESTAMP '2026-02-02 08:50:00', TIMESTAMP '2026-02-02 18:00:00', 'NORMAL', 'NORMAL'),
(5243, 1009, '2026-02-02', TIMESTAMP '2026-02-02 10:06:00', TIMESTAMP '2026-02-02 16:46:00', 'LATE', 'EARLY_LEAVE'),
(5244, 1010, '2026-02-02', TIMESTAMP '2026-02-02 09:22:00', TIMESTAMP '2026-02-02 15:43:00', 'LATE', 'EARLY_LEAVE'),
(5245, 1011, '2026-02-02', TIMESTAMP '2026-02-02 09:00:00', TIMESTAMP '2026-02-02 15:03:00', 'NORMAL', 'EARLY_LEAVE'),
(5246, 1012, '2026-02-02', TIMESTAMP '2026-02-02 08:40:00', TIMESTAMP '2026-02-02 18:00:00', 'NORMAL', 'NORMAL'),
(5247, 1013, '2026-02-02', TIMESTAMP '2026-02-02 08:39:00', TIMESTAMP '2026-02-02 18:00:00', 'NORMAL', 'NORMAL'),
(5248, 1001, '2026-02-03', TIMESTAMP '2026-02-03 09:00:00', TIMESTAMP '2026-02-03 21:48:00', 'NORMAL', 'OVERTIME'),
(5249, 1002, '2026-02-03', TIMESTAMP '2026-02-03 08:41:00', TIMESTAMP '2026-02-03 18:00:00', 'NORMAL', 'NORMAL'),
(5250, 1003, '2026-02-03', TIMESTAMP '2026-02-03 10:17:00', TIMESTAMP '2026-02-03 18:00:00', 'LATE', 'NORMAL');

INSERT INTO attendance (attendance_id, member_id, attendance_date, attendance_start, attendance_end, check_in_status, check_out_status) VALUES
(5251, 1004, '2026-02-03', TIMESTAMP '2026-02-03 09:00:00', TIMESTAMP '2026-02-03 18:00:00', 'NORMAL', 'NORMAL'),
(5252, 1005, '2026-02-03', TIMESTAMP '2026-02-03 08:42:00', TIMESTAMP '2026-02-03 18:00:00', 'NORMAL', 'NORMAL'),
(5253, 1006, '2026-02-03', TIMESTAMP '2026-02-03 09:16:00', TIMESTAMP '2026-02-03 18:00:00', 'LATE', 'NORMAL'),
(5254, 1007, '2026-02-03', TIMESTAMP '2026-02-03 09:00:00', TIMESTAMP '2026-02-03 20:07:00', 'NORMAL', 'OVERTIME'),
(5255, 1008, '2026-02-03', NULL, NULL, 'ABSENT', NULL),
(5256, 1009, '2026-02-03', TIMESTAMP '2026-02-03 09:00:00', TIMESTAMP '2026-02-03 22:32:00', 'NORMAL', 'OVERTIME'),
(5257, 1010, '2026-02-03', TIMESTAMP '2026-02-03 09:08:00', TIMESTAMP '2026-02-03 17:25:00', 'LATE', 'EARLY_LEAVE'),
(5258, 1011, '2026-02-03', TIMESTAMP '2026-02-03 08:48:00', TIMESTAMP '2026-02-03 20:59:00', 'NORMAL', 'OVERTIME'),
(5259, 1012, '2026-02-03', NULL, NULL, 'ABSENT', NULL),
(5260, 1013, '2026-02-03', NULL, NULL, 'ABSENT', NULL),
(5261, 1001, '2026-02-04', TIMESTAMP '2026-02-04 09:48:00', TIMESTAMP '2026-02-04 20:43:00', 'LATE', 'OVERTIME'),
(5262, 1002, '2026-02-04', NULL, NULL, 'ABSENT', NULL),
(5263, 1003, '2026-02-04', TIMESTAMP '2026-02-04 08:43:00', TIMESTAMP '2026-02-04 17:16:00', 'NORMAL', 'EARLY_LEAVE'),
(5264, 1004, '2026-02-04', TIMESTAMP '2026-02-04 09:00:00', TIMESTAMP '2026-02-04 15:51:00', 'NORMAL', 'EARLY_LEAVE'),
(5265, 1005, '2026-02-04', TIMESTAMP '2026-02-04 09:44:00', TIMESTAMP '2026-02-04 18:00:00', 'LATE', 'NORMAL'),
(5266, 1006, '2026-02-04', TIMESTAMP '2026-02-04 10:28:00', TIMESTAMP '2026-02-04 18:00:00', 'LATE', 'NORMAL'),
(5267, 1007, '2026-02-04', NULL, NULL, 'ABSENT', NULL),
(5268, 1008, '2026-02-04', TIMESTAMP '2026-02-04 09:43:00', TIMESTAMP '2026-02-04 21:08:00', 'LATE', 'OVERTIME'),
(5269, 1009, '2026-02-04', TIMESTAMP '2026-02-04 08:51:00', TIMESTAMP '2026-02-04 18:00:00', 'NORMAL', 'NORMAL'),
(5270, 1010, '2026-02-04', TIMESTAMP '2026-02-04 10:04:00', TIMESTAMP '2026-02-04 22:17:00', 'LATE', 'OVERTIME'),
(5271, 1011, '2026-02-04', TIMESTAMP '2026-02-04 09:09:00', TIMESTAMP '2026-02-04 17:25:00', 'LATE', 'EARLY_LEAVE'),
(5272, 1012, '2026-02-04', TIMESTAMP '2026-02-04 09:49:00', TIMESTAMP '2026-02-04 18:00:00', 'LATE', 'NORMAL'),
(5273, 1013, '2026-02-04', TIMESTAMP '2026-02-04 09:37:00', TIMESTAMP '2026-02-04 18:00:00', 'LATE', 'NORMAL'),
(5274, 1001, '2026-02-05', TIMESTAMP '2026-02-05 08:53:00', TIMESTAMP '2026-02-05 18:00:00', 'NORMAL', 'NORMAL'),
(5275, 1002, '2026-02-05', TIMESTAMP '2026-02-05 09:00:00', TIMESTAMP '2026-02-05 18:00:00', 'NORMAL', 'NORMAL'),
(5276, 1003, '2026-02-05', TIMESTAMP '2026-02-05 09:59:00', TIMESTAMP '2026-02-05 18:00:00', 'LATE', 'NORMAL'),
(5277, 1004, '2026-02-05', TIMESTAMP '2026-02-05 10:23:00', TIMESTAMP '2026-02-05 18:00:00', 'LATE', 'NORMAL'),
(5278, 1005, '2026-02-05', TIMESTAMP '2026-02-05 09:30:00', TIMESTAMP '2026-02-05 18:00:00', 'LATE', 'NORMAL'),
(5279, 1006, '2026-02-05', TIMESTAMP '2026-02-05 10:29:00', TIMESTAMP '2026-02-05 18:00:00', 'LATE', 'NORMAL'),
(5280, 1007, '2026-02-05', TIMESTAMP '2026-02-05 09:00:00', TIMESTAMP '2026-02-05 18:00:00', 'NORMAL', 'NORMAL'),
(5281, 1008, '2026-02-05', TIMESTAMP '2026-02-05 09:00:00', TIMESTAMP '2026-02-05 18:00:00', 'NORMAL', 'NORMAL'),
(5282, 1009, '2026-02-05', TIMESTAMP '2026-02-05 09:09:00', TIMESTAMP '2026-02-05 22:47:00', 'LATE', 'OVERTIME'),
(5283, 1010, '2026-02-05', NULL, NULL, 'ABSENT', NULL),
(5284, 1011, '2026-02-05', TIMESTAMP '2026-02-05 09:30:00', TIMESTAMP '2026-02-05 18:00:00', 'LATE', 'NORMAL'),
(5285, 1012, '2026-02-05', TIMESTAMP '2026-02-05 10:24:00', TIMESTAMP '2026-02-05 18:00:00', 'LATE', 'NORMAL'),
(5286, 1013, '2026-02-05', TIMESTAMP '2026-02-05 09:00:00', TIMESTAMP '2026-02-05 20:10:00', 'NORMAL', 'OVERTIME'),
(5287, 1001, '2026-02-06', TIMESTAMP '2026-02-06 09:49:00', TIMESTAMP '2026-02-06 18:00:00', 'LATE', 'NORMAL'),
(5288, 1002, '2026-02-06', TIMESTAMP '2026-02-06 09:00:00', TIMESTAMP '2026-02-06 22:34:00', 'NORMAL', 'OVERTIME'),
(5289, 1003, '2026-02-06', TIMESTAMP '2026-02-06 08:43:00', TIMESTAMP '2026-02-06 20:07:00', 'NORMAL', 'OVERTIME'),
(5290, 1004, '2026-02-06', TIMESTAMP '2026-02-06 09:50:00', TIMESTAMP '2026-02-06 18:00:00', 'LATE', 'NORMAL'),
(5291, 1005, '2026-02-06', NULL, NULL, 'ABSENT', NULL),
(5292, 1006, '2026-02-06', NULL, NULL, 'ABSENT', NULL),
(5293, 1007, '2026-02-06', TIMESTAMP '2026-02-06 08:35:00', TIMESTAMP '2026-02-06 21:58:00', 'NORMAL', 'OVERTIME'),
(5294, 1008, '2026-02-06', TIMESTAMP '2026-02-06 09:13:00', TIMESTAMP '2026-02-06 17:11:00', 'LATE', 'EARLY_LEAVE'),
(5295, 1009, '2026-02-06', TIMESTAMP '2026-02-06 08:37:00', TIMESTAMP '2026-02-06 19:27:00', 'NORMAL', 'OVERTIME'),
(5296, 1010, '2026-02-06', TIMESTAMP '2026-02-06 09:00:00', TIMESTAMP '2026-02-06 18:00:00', 'NORMAL', 'NORMAL'),
(5297, 1011, '2026-02-06', TIMESTAMP '2026-02-06 09:00:00', TIMESTAMP '2026-02-06 20:32:00', 'NORMAL', 'OVERTIME'),
(5298, 1012, '2026-02-06', TIMESTAMP '2026-02-06 09:40:00', TIMESTAMP '2026-02-06 18:00:00', 'LATE', 'NORMAL'),
(5299, 1013, '2026-02-06', TIMESTAMP '2026-02-06 09:45:00', TIMESTAMP '2026-02-06 20:13:00', 'LATE', 'OVERTIME'),
(5300, 1001, '2026-02-09', TIMESTAMP '2026-02-09 09:34:00', TIMESTAMP '2026-02-09 18:00:00', 'LATE', 'NORMAL');

INSERT INTO attendance (attendance_id, member_id, attendance_date, attendance_start, attendance_end, check_in_status, check_out_status) VALUES
(5301, 1002, '2026-02-09', NULL, NULL, 'ABSENT', NULL),
(5302, 1003, '2026-02-09', TIMESTAMP '2026-02-09 09:28:00', TIMESTAMP '2026-02-09 18:00:00', 'LATE', 'NORMAL'),
(5303, 1004, '2026-02-09', NULL, NULL, 'ABSENT', NULL),
(5304, 1005, '2026-02-09', TIMESTAMP '2026-02-09 10:23:00', TIMESTAMP '2026-02-09 18:00:00', 'LATE', 'NORMAL'),
(5305, 1006, '2026-02-09', TIMESTAMP '2026-02-09 09:26:00', TIMESTAMP '2026-02-09 18:00:00', 'LATE', 'NORMAL'),
(5306, 1007, '2026-02-09', TIMESTAMP '2026-02-09 09:53:00', TIMESTAMP '2026-02-09 18:00:00', 'LATE', 'NORMAL'),
(5307, 1008, '2026-02-09', TIMESTAMP '2026-02-09 09:23:00', TIMESTAMP '2026-02-09 18:00:00', 'LATE', 'NORMAL'),
(5308, 1009, '2026-02-09', TIMESTAMP '2026-02-09 10:22:00', TIMESTAMP '2026-02-09 18:00:00', 'LATE', 'NORMAL'),
(5309, 1010, '2026-02-09', TIMESTAMP '2026-02-09 09:14:00', TIMESTAMP '2026-02-09 21:39:00', 'LATE', 'OVERTIME'),
(5310, 1011, '2026-02-09', TIMESTAMP '2026-02-09 09:34:00', TIMESTAMP '2026-02-09 19:27:00', 'LATE', 'OVERTIME'),
(5311, 1012, '2026-02-09', TIMESTAMP '2026-02-09 09:00:00', TIMESTAMP '2026-02-09 19:45:00', 'NORMAL', 'OVERTIME'),
(5312, 1013, '2026-02-09', TIMESTAMP '2026-02-09 09:00:00', TIMESTAMP '2026-02-09 18:00:00', 'NORMAL', 'NORMAL'),
(5313, 1001, '2026-02-10', TIMESTAMP '2026-02-10 10:03:00', TIMESTAMP '2026-02-10 18:00:00', 'LATE', 'NORMAL'),
(5314, 1002, '2026-02-10', TIMESTAMP '2026-02-10 10:28:00', TIMESTAMP '2026-02-10 17:30:00', 'LATE', 'EARLY_LEAVE'),
(5315, 1003, '2026-02-10', NULL, NULL, 'ABSENT', NULL),
(5316, 1004, '2026-02-10', TIMESTAMP '2026-02-10 09:00:00', TIMESTAMP '2026-02-10 18:00:00', 'NORMAL', 'NORMAL'),
(5317, 1005, '2026-02-10', TIMESTAMP '2026-02-10 09:00:00', TIMESTAMP '2026-02-10 18:00:00', 'NORMAL', 'NORMAL'),
(5318, 1006, '2026-02-10', TIMESTAMP '2026-02-10 09:00:00', TIMESTAMP '2026-02-10 18:00:00', 'NORMAL', 'NORMAL'),
(5319, 1007, '2026-02-10', TIMESTAMP '2026-02-10 10:17:00', TIMESTAMP '2026-02-10 22:05:00', 'LATE', 'OVERTIME'),
(5320, 1008, '2026-02-10', TIMESTAMP '2026-02-10 08:30:00', TIMESTAMP '2026-02-10 22:03:00', 'NORMAL', 'OVERTIME'),
(5321, 1009, '2026-02-10', TIMESTAMP '2026-02-10 08:39:00', TIMESTAMP '2026-02-10 19:52:00', 'NORMAL', 'OVERTIME'),
(5322, 1010, '2026-02-10', TIMESTAMP '2026-02-10 08:31:00', TIMESTAMP '2026-02-10 21:55:00', 'NORMAL', 'OVERTIME'),
(5323, 1011, '2026-02-10', NULL, NULL, 'ABSENT', NULL),
(5324, 1012, '2026-02-10', TIMESTAMP '2026-02-10 10:06:00', TIMESTAMP '2026-02-10 18:00:00', 'LATE', 'NORMAL'),
(5325, 1013, '2026-02-10', TIMESTAMP '2026-02-10 09:39:00', TIMESTAMP '2026-02-10 18:00:00', 'LATE', 'NORMAL'),
(5326, 1001, '2026-02-11', TIMESTAMP '2026-02-11 09:00:00', NULL, 'NORMAL', NULL),
(5327, 1002, '2026-02-11', NULL, NULL, 'ABSENT', NULL),
(5328, 1003, '2026-02-11', TIMESTAMP '2026-02-11 09:55:00', NULL, 'LATE', NULL),
(5329, 1004, '2026-02-11', TIMESTAMP '2026-02-11 08:38:00', NULL, 'NORMAL', NULL),
(5330, 1005, '2026-02-11', TIMESTAMP '2026-02-11 09:00:00', NULL, 'NORMAL', NULL),
(5331, 1006, '2026-02-11', TIMESTAMP '2026-02-11 09:00:00', NULL, 'NORMAL', NULL),
(5332, 1007, '2026-02-11', TIMESTAMP '2026-02-11 09:12:00', NULL, 'LATE', NULL),
(5333, 1008, '2026-02-11', TIMESTAMP '2026-02-11 09:10:00', NULL, 'LATE', NULL),
(5334, 1009, '2026-02-11', TIMESTAMP '2026-02-11 09:00:00', NULL, 'NORMAL', NULL),
(5335, 1010, '2026-02-11', TIMESTAMP '2026-02-11 09:13:00', NULL, 'LATE', NULL),
(5336, 1011, '2026-02-11', TIMESTAMP '2026-02-11 09:28:00', NULL, 'LATE', NULL),
(5337, 1012, '2026-02-11', TIMESTAMP '2026-02-11 10:08:00', NULL, 'LATE', NULL),
(5338, 1013, '2026-02-11', TIMESTAMP '2026-02-11 09:00:00', NULL, 'NORMAL', NULL);


-- 추가 휴가 데이터
INSERT INTO vacation (vacation_id, member_id, vacation_type, vacation_start, vacation_end, vacation_request, vacation_detail, vacation_approve, vacation_rejected, vacation_days) VALUES
(1010, 1004, 'ANNUAL', '2026-03-10', '2026-03-12', '2026-02-01', '개인 사정으로 인한 연차', 'APPROVED', NULL, 3.0),
(1011, 1006, 'SICK', '2026-02-06', '2026-02-06', '2026-02-06', '급성 장염', 'APPROVED', NULL, 1.0),
(1012, 1002, 'HALF', '2026-02-14', '2026-02-14', '2026-02-05', '은행 업무', 'APPROVE_NEED', NULL, 0.5),
(1013, 1005, 'ANNUAL', '2026-04-01', '2026-04-05', '2026-02-06', '벚꽃 여행', 'APPROVE_NEED', NULL, 3.0),
(1014, 1001, 'FAMILY', '2026-03-20', '2026-03-20', '2026-02-04', '가족 결혼식', 'APPROVED', NULL, 1.0);

-- 추가 휴가 상세 데이터 (경조사/병가)
INSERT INTO vacation_family (family_id, vacation_id, family_relation, family_detail) VALUES
(1002, 1014, '여동생', '결혼식');

INSERT INTO vacation_sick (sick_id, vacation_id, sick_detail, sick_hospital) VALUES
(1002, 1011, '급성 장염 증상으로 병원 진료 필요', '서울 아산병원');

-- 게시판 데이터
INSERT INTO qa_board (qa_id, question_member, answer_member, question_title, question_detail, answer_detail, question_time, answer_time) VALUES
(1, 1007, 1001, '연차 사용 문의',
 '이번 달에 연차 사용 가능한지 문의드립니다.',
 '가능합니다. 인사팀에 별도 신청서 제출해주세요.',
 '2026-02-01 09:15:00',
 '2026-02-01 10:00:00'),
(2, 1008, 1002, '근무 시간 조정 요청',
 '금요일 근무 시간을 조정하고 싶습니다.',
 '팀장 승인 후 인사팀에 공유 바랍니다.',
 '2026-02-02 11:20:00',
 '2026-02-02 13:00:00'),
(3, 1009, NULL, '마케팅 예산 관련 문의',
 '다음 분기 예산 확정 일정이 궁금합니다.',
 NULL,
 '2026-02-03 14:05:00',
 NULL),
(4, 1003, 1001, '크리에이터 계약 조건 문의',
 '신규 크리에이터 계약 조건 변경 가능 여부 문의드립니다.',
 '기존 조건 유지가 원칙입니다.',
 '2026-02-04 10:30:00',
 '2026-02-04 11:10:00'),
(5, 1005, NULL, '정산 일정 문의',
 '이번 달 정산 일정이 언제인가요?',
 NULL,
 '2026-02-05 16:45:00',
 NULL),
(6, 1006, 1002, '마케팅 캠페인 승인 요청',
 '3월 캠페인 기획안 승인 부탁드립니다.',
 '자료 검토 후 회신드리겠습니다.',
 '2026-02-06 09:00:00',
 '2026-02-06 15:30:00'),
(7, 1012, NULL, '글로벌 프로젝트 일정 문의',
 '해외 협업 프로젝트 일정 확정되었나요?',
 NULL,
 '2026-02-07 08:40:00',
 NULL),
(8, 1013, 1001, '썸네일 제작 리소스 요청',
 '추가 디자인 리소스 지원 가능할까요?',
 '현재 인력 충원 계획은 없습니다.',
 '2026-02-08 13:10:00',
 '2026-02-08 14:00:00'),
(9, 1010, 1002, '사업 제안서 검토 요청',
 '신규 사업 제안서 검토 부탁드립니다.',
 '수정 후 다시 제출해주세요.',
 '2026-02-09 17:25:00',
 '2026-02-09 18:00:00'),
(10, 1011, NULL, '경영지원 시스템 오류 문의',
 '내부 시스템 접속 오류가 발생합니다.',
 NULL,
 '2026-02-10 09:50:00',
 NULL);

-- ==========================================
-- 크리에이터 Todo 컬럼 (칸반 보드)
-- ==========================================
INSERT INTO creator_todo_column (id, creator_id, title, sequence) VALUES
-- 감스트 (2001)
(1, 2001, '할 일', 1),
(2, 2001, '진행 중', 2),
(3, 2001, '완료', 3),
-- 또간집 (2002)
(4, 2002, '할 일', 1),
(5, 2002, '진행 중', 2),
(6, 2002, '완료', 3),
-- 뷰티지니 (2003)
(7, 2003, '할 일', 1),
(8, 2003, '진행 중', 2),
(9, 2003, '완료', 3),
-- 먹보PD (2004)
(10, 2004, '할 일', 1),
(11, 2004, '진행 중', 2),
(12, 2004, '완료', 3),
-- 게임왕민수 (2005)
(13, 2005, '할 일', 1),
(14, 2005, '진행 중', 2),
(15, 2005, '완료', 3),
-- 일상브이로거 (2006)
(16, 2006, '할 일', 1),
(17, 2006, '진행 중', 2),
(18, 2006, '완료', 3),
-- 테크리뷰어 (2007)
(19, 2007, '할 일', 1),
(20, 2007, '진행 중', 2),
(21, 2007, '완료', 3),
-- 휴식중크리에이터 (2008)
(22, 2008, '할 일', 1),
(23, 2008, '진행 중', 2),
(24, 2008, '완료', 3),
-- 은퇴한크리 (2009)
(25, 2009, '할 일', 1),
(26, 2009, '진행 중', 2),
(27, 2009, '완료', 3);

-- ==========================================
-- 크리에이터 Todo 샘플 데이터
-- ==========================================
INSERT INTO creator_todo (id, creator_id, column_id, content, position, created_by, created_by_name, created_at, updated_at, version) VALUES
-- 감스트 Todo
(1, 2001, 1, '다음주 영상 촬영 준비', 1.0, 1003, '박매니저', '2026-02-15 10:00:00', NULL, 0),
(2, 2001, 1, '광고주 미팅 일정 확인', 2.0, 2001, '감스트', '2026-02-15 11:00:00', NULL, 0),
(3, 2001, 2, '영상 편집 중', 1.0, 1003, '박매니저', '2026-02-14 09:00:00', NULL, 0),
(4, 2001, 3, '섬네일 제작 완료', 1.0, 2001, '감스트', '2026-02-13 15:00:00', NULL, 0),
-- 또간집 Todo
(5, 2002, 1, '신메뉴 리뷰 섭외', 1.0, 1004, '이담당', '2026-02-15 14:00:00', NULL, 0),
(6, 2002, 2, '지방 촬영 일정 조율', 1.0, 2002, '또간집', '2026-02-14 16:00:00', NULL, 0);
