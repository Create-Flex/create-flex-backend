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
TRUNCATE TABLE creator_work;
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
(1014, 1004, 'MK003', '$2a$10$X/gsyQUYbf4R3N6Nmj2WseJTkR21hBjxJ0N8CtTs2C7uqsQ4BX/EK', '구직원', 'EMPLOYEE', 'SUSPENDED', '마케터');

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
(1001, 1001, '인사왕', 'Kim Insa', 'kim.insa@gmail.com', 'kim.insa@mcn.com', '010-1111-1111', '2020-03-02', NULL, '서울시 강남구 테헤란로 123', 'EXPERIENCED', NULL, 15.0),
(1002, 1002, '채용마스터', 'Lee Chaeyong', 'lee.chaeyong@naver.com', 'lee.chaeyong@mcn.com', '010-2222-2222', '2021-06-15', NULL, '서울시 서초구 서초대로 456', 'NEWBIE', NULL, 15.0),
(1003, 1003, '매니저박', 'Park Manager', 'park.mg@gmail.com', 'park.manager@mcn.com', '010-3333-3333', '2019-01-10', NULL, '서울시 마포구 월드컵로 789', 'EXPERIENCED', NULL, 15.0),
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
(1014, 1014, '구직원님', 'Gu Jikwon', 'gu.old@naver.com', 'gu.jikwon@mcn.com', '010-1515-1515', '2020-02-01', '2024-12-31', '서울시 동대문구 왕산로 1111', 'NEWBIE', '개인 사정으로 인한 퇴사', 15.0);

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
(1002, 1002, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/VNgy5fkH/photo_1486406146926_c627a92ad1ab.avif'),
(1003, 1003, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/VNgy5fkH/photo_1486406146926_c627a92ad1ab.avif'),
(1004, 1004, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/mrjxhLg1/photo_1497366216548_37526070297c.avif'),
(1005, 1005, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/63pkvk8D/photo_1506784983877_45594efa4cbe.avif'),
(1006, 1006, 'https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png', 'https://i.postimg.cc/d3zpgD4x/photo_1519389950473_47ba0277781c.avif'),
(1007, 1007, 'https://cdn.mcn.com/profiles/yoon_dev.jpg', 'https://cdn.mcn.com/banners/yoon_dev_banner.jpg'),
(1008, 1008, 'https://cdn.mcn.com/profiles/han_coding.jpg', 'https://cdn.mcn.com/banners/han_coding_banner.jpg'),
(1009, 1009, 'https://cdn.mcn.com/profiles/song_hongbo.jpg', 'https://cdn.mcn.com/banners/song_hongbo_banner.jpg'),
(1010, 1010, 'https://cdn.mcn.com/profiles/jo_business.jpg', 'https://cdn.mcn.com/banners/jo_business_banner.jpg'),
(1011, 1011, 'https://cdn.mcn.com/profiles/shin_gyeong.jpg', 'https://cdn.mcn.com/banners/shin_gyeong_banner.jpg'),
(1012, 1012, 'https://cdn.mcn.com/profiles/emma_wilson.jpg', 'https://cdn.mcn.com/banners/emma_wilson_banner.jpg'),
(1013, 1013, 'https://cdn.mcn.com/profiles/moon_thumbnail.jpg', 'https://cdn.mcn.com/banners/moon_thumbnail_banner.jpg'),
(1014, 1014, 'https://cdn.mcn.com/profiles/gu_jikwon.jpg', 'https://cdn.mcn.com/banners/gu_jikwon_banner.jpg'),
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
(2001, 2001, '삼성전자', '갤럭시 S24 울트라 기능 리뷰 및 시연', '8000000', '신제품 출시 기념 메인 기능 집중 리뷰 영상 제작 (15분 이상), AI 카메라 기능 실사용 시연', '2025-01-10', '2025-02-15', 'ACCEPTED'),
(2002, 2002, 'CJ제일제당', '비비고 왕교자 먹방 콘텐츠', '5000000', '비비고 왕교자 신제품 먹방 콘텐츠 제작, 맛 리뷰 및 조리 과정 포함', '2025-01-15', '2025-02-28', 'WAITING'),
(2003, 2003, '아모레퍼시픽', '설화수 신제품 체험 영상', '6500000', '설화수 신제품 라인 체험 및 사용 후기 영상, 메이크업 튜토리얼 포함', '2025-01-20', '2025-03-10', 'ACCEPTED'),
(2004, 2004, '배달의민족', '배민 신규 서비스 홍보', '4000000', '배민 신규 서비스 소개 및 실사용 라이브 방송 2시간 진행', '2025-01-12', '2025-02-20', 'WAITING'),
(2005, 2005, '넥슨', '던전앤파이터 모바일 신작 찍먹 플레이', '10000000', '캐주얼하게 게임을 즐기는 모습을 담은 라이브 방송 3시간 진행, 신규 직업 체험 콘텐츠', '2025-01-08', '2025-02-05', 'ACCEPTED'),
(2006, 2007, 'LG전자', 'LG 그램 2025 신제품 리뷰', '7000000', 'LG 그램 신모델 상세 리뷰 영상 제작, 성능 테스트 및 비교 분석 포함', '2025-01-18', '2025-03-01', 'WAITING'),
(2007, 2001, '나이키', '나이키 에어맥스 신제품 홍보', '6000000', '나이키 에어맥스 신제품 착화 리뷰 및 운동 콘텐츠', '2025-01-05', '2025-02-10', 'REJECTED'),
(2008, 2003, '에스티로더', '에스티로더 어드밴스드 나이트 리페어', '5500000', '에스티로더 대표 제품 사용 후기 및 스킨케어 루틴 영상', '2024-12-20', '2025-01-31', 'ACCEPTED');

-- ==========================================
-- 10. Creator Work (업무 현황)
-- ==========================================
INSERT INTO creator_work (creator_work_id, member_creator_id, work_name, work_status, member_worker_id) VALUES
(2001, 2001, '다음주 콘텐츠 기획안 피드백', 'WORKING', 1003),
(2002, 2001, '유튜브 채널 아트 리뉴얼 시안 확인', 'WORKING', 1013),
(2003, 2002, '2월 먹방 콘텐츠 스케줄 조율', 'WORKING', 1003),
(2004, 2003, '뷰티 트렌드 분석 보고서 검토', 'DONE', 1004),
(2005, 2003, '신규 협찬 제품 수령 및 확인', 'WORKING', 1004),
(2006, 2004, '라이브 방송 장비 점검', 'DONE', 1005),
(2007, 2005, '게임 대회 출전 일정 협의', 'WORKING', 1005),
(2008, 2005, '신규 게임 협찬 미팅 준비', 'WORKING', 1005),
(2009, 2006, '브이로그 촬영 장소 섭외', 'WORKING', 1005),
(2010, 2007, '테크 신제품 발표회 참석 일정 확인', 'DONE', 1003),
(2011, 2001, '삼성 광고 콘텐츠 1차 편집본 확인', 'WORKING', 1003),
(2012, 2003, '아모레 협찬 콘텐츠 기획안 작성', 'DONE', 1004);

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
(1002, 1001, 1013), -- 문썸네일 (1001번 팀 추가 소속)
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
(1011, 1004, 1012), -- Emma Wilson (1004번 팀 추가 소속)
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
-- 19. Attendance (근태) - 새 구조
-- check_in_status: 출근 상태 (NORMAL, LATE, ABSENT)
-- check_out_status: 퇴근 상태 (EARLY_LEAVE, NORMAL, OVERTIME) - null은 미퇴근(근무중)
-- ==========================================
INSERT INTO attendance (attendance_id, member_id, attendance_date, attendance_start, attendance_end, check_in_status, check_out_status) VALUES
-- 2026-01-27 (월)
(1001, 1001, '2026-01-27', TIMESTAMP '2026-01-27 09:00:00', TIMESTAMP '2026-01-27 18:00:00', 'NORMAL', 'NORMAL'),
(1002, 1003, '2026-01-27', TIMESTAMP '2026-01-27 09:15:00', TIMESTAMP '2026-01-27 18:30:00', 'LATE', 'OVERTIME'),
(1003, 1007, '2026-01-27', TIMESTAMP '2026-01-27 10:00:00', TIMESTAMP '2026-01-27 19:00:00', 'LATE', 'OVERTIME'),
(1004, 1009, '2026-01-27', TIMESTAMP '2026-01-27 09:00:00', TIMESTAMP '2026-01-27 18:00:00', 'NORMAL', 'NORMAL'),
-- 2026-01-28 (화)
(1005, 1001, '2026-01-28', TIMESTAMP '2026-01-28 09:05:00', TIMESTAMP '2026-01-28 18:00:00', 'LATE', 'NORMAL'),
(1006, 1003, '2026-01-28', TIMESTAMP '2026-01-28 09:00:00', TIMESTAMP '2026-01-28 20:00:00', 'NORMAL', 'OVERTIME'),
(1007, 1007, '2026-01-28', TIMESTAMP '2026-01-28 10:00:00', TIMESTAMP '2026-01-28 17:30:00', 'LATE', 'EARLY_LEAVE'),
(1008, 1009, '2026-01-28', TIMESTAMP '2026-01-28 09:10:00', TIMESTAMP '2026-01-28 18:00:00', 'LATE', 'NORMAL'),
-- 2026-01-29 (수)
(1009, 1001, '2026-01-29', TIMESTAMP '2026-01-29 09:00:00', TIMESTAMP '2026-01-29 18:00:00', 'NORMAL', 'NORMAL'),
(1010, 1003, '2026-01-29', TIMESTAMP '2026-01-29 09:00:00', TIMESTAMP '2026-01-29 18:00:00', 'NORMAL', 'NORMAL'),
(1011, 1007, '2026-01-29', TIMESTAMP '2026-01-29 10:00:00', TIMESTAMP '2026-01-29 19:00:00', 'LATE', 'OVERTIME'),
(1012, 1009, '2026-01-29', TIMESTAMP '2026-01-29 09:00:00', TIMESTAMP '2026-01-29 18:00:00', 'NORMAL', 'NORMAL'),
-- 2026-01-30 (목)
(1013, 1001, '2026-01-30', TIMESTAMP '2026-01-30 09:00:00', TIMESTAMP '2026-01-30 18:00:00', 'NORMAL', 'NORMAL'),
(1014, 1003, '2026-01-30', TIMESTAMP '2026-01-30 09:30:00', TIMESTAMP '2026-01-30 18:30:00', 'LATE', 'OVERTIME'),
(1015, 1007, '2026-01-30', TIMESTAMP '2026-01-30 10:00:00', TIMESTAMP '2026-01-30 17:00:00', 'LATE', 'EARLY_LEAVE'),
(1016, 1009, '2026-01-30', TIMESTAMP '2026-01-30 09:00:00', TIMESTAMP '2026-01-30 18:00:00', 'NORMAL', 'NORMAL'),
-- 2026-01-31 (금)
(1017, 1001, '2026-01-31', TIMESTAMP '2026-01-31 09:00:00', TIMESTAMP '2026-01-31 18:00:00', 'NORMAL', 'NORMAL'),
(1018, 1003, '2026-01-31', TIMESTAMP '2026-01-31 09:00:00', TIMESTAMP '2026-01-31 18:00:00', 'NORMAL', 'NORMAL'),
(1019, 1007, '2026-01-31', TIMESTAMP '2026-01-31 10:00:00', TIMESTAMP '2026-01-31 19:00:00', 'LATE', 'OVERTIME'),
(1020, 1009, '2026-01-31', TIMESTAMP '2026-01-31 09:00:00', TIMESTAMP '2026-01-31 17:30:00', 'NORMAL', 'EARLY_LEAVE'),
-- 2026-02-03 (월) - 2월 시작
(1021, 1001, '2026-02-03', TIMESTAMP '2026-02-03 09:00:00', TIMESTAMP '2026-02-03 18:00:00', 'NORMAL', 'NORMAL'),
(1022, 1003, '2026-02-03', TIMESTAMP '2026-02-03 09:00:00', TIMESTAMP '2026-02-03 19:30:00', 'NORMAL', 'OVERTIME'),
(1023, 1007, '2026-02-03', TIMESTAMP '2026-02-03 10:00:00', TIMESTAMP '2026-02-03 19:00:00', 'LATE', 'OVERTIME'),
(1024, 1009, '2026-02-03', TIMESTAMP '2026-02-03 09:00:00', TIMESTAMP '2026-02-03 18:00:00', 'NORMAL', 'NORMAL'),
-- 2026-02-04 (화) - 오늘 (근무중인 사람들)
(1025, 1001, '2026-02-04', TIMESTAMP '2026-02-04 09:00:00', NULL, 'NORMAL', NULL),
(1026, 1003, '2026-02-04', TIMESTAMP '2026-02-04 09:00:00', NULL, 'NORMAL', NULL),
(1027, 1007, '2026-02-04', TIMESTAMP '2026-02-04 10:00:00', NULL, 'LATE', NULL),
(1028, 1009, '2026-02-04', TIMESTAMP '2026-02-04 09:00:00', NULL, 'NORMAL', NULL);

-- 2026-02-06 (금) - 오늘
INSERT INTO attendance (attendance_id, member_id, attendance_date, attendance_start, attendance_end, check_in_status, check_out_status) VALUES
(1029, 1001, '2026-02-06', TIMESTAMP '2026-02-06 08:55:00', NULL, 'NORMAL', NULL),
(1030, 1002, '2026-02-06', TIMESTAMP '2026-02-06 09:05:00', NULL, 'LATE', NULL),
(1031, 1003, '2026-02-06', TIMESTAMP '2026-02-06 08:50:00', NULL, 'NORMAL', NULL),
(1032, 1004, '2026-02-06', TIMESTAMP '2026-02-06 09:10:00', NULL, 'LATE', NULL),
(1033, 1005, '2026-02-06', TIMESTAMP '2026-02-06 08:58:00', NULL, 'NORMAL', NULL),
(1034, 1006, '2026-02-06', TIMESTAMP '2026-02-06 09:00:00', NULL, 'NORMAL', NULL),
(1035, 1007, '2026-02-06', TIMESTAMP '2026-02-06 10:15:00', NULL, 'LATE', NULL),
(1036, 1008, '2026-02-06', TIMESTAMP '2026-02-06 08:45:00', NULL, 'NORMAL', NULL);


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
