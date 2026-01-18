-- 기존 테스트 데이터 삭제 (외래키 참조 순서 고려)
DELETE FROM travel_template_places WHERE travel_template_id IN (
    SELECT id FROM travel_templates WHERE travel_id = 'TRAVEL_001'
);
DELETE FROM travel_templates WHERE travel_id = 'TRAVEL_001';
DELETE FROM places WHERE place_id IN (
    'ChIJSc8jdZORQTURu6BMwxrKbGg',
    'ChIJN1t_tDeuEmsRUsoyG83frY4',
    'ChIJ_xkgOmOuEmsR8FhZz3qJN1I'
);

-- 테스트용 Travel Template 데이터 삽입
INSERT INTO travel_templates (
    travel_id,
    traveler,
    country,
    city,
    weather_info,
    culture_info,
    food_info,
    thumbnail,
    link,
    budget_per_person,
    summary,
    title,
    nights,
    days,
    created_at,
    updated_at
) VALUES (
    'TRAVEL_001',
    '도쿄여행러버',
    '일본',
    '도쿄',
    '여름철 고온다습, 가벼운 옷차림 권장. 우산 필수.',
    '식당에서 팁 불필요, 조용히 식사하는 것이 예의. 신발 벗는 곳이 많음.',
    '라멘과 초밥이 유명하며, 현지 식당에서 현금 결제가 일반적. 편의점 음식도 훌륭함.',
    'https://example.com/thumbnail/tokyo.jpg',
    'https://www.youtube.com/watch?v=tokyo-travel',
    1200000,
    '도쿄 3박 4일 여행의 모든 것. 유튜버가 직접 다녀온 코스로 구성된 완벽한 가이드.',
    '도쿄 3박 4일 완벽 여행 가이드',
    3,
    4,
    NOW(),
    NOW()
);

-- 테스트용 Place 데이터 삽입 (3개)
INSERT INTO places (
    place_id,
    formatted_address,
    latitude,
    longitude,
    rating,
    national_phone_number,
    international_phone_number,
    website_uri,
    google_maps_uri,
    user_rating_count,
    display_name_json,
    regular_opening_hours_json,
    photos_json,
    postal_address_json,
    created_at,
    updated_at
) VALUES
(
    'ChIJSc8jdZORQTURu6BMwxrKbGg',
    '일본 〒105-0011 Tokyo, Minato City, Shiba-koen, 4 Chome−2−8',
    35.6585805,
    139.7454329,
    4.5,
    '03-3433-5111',
    '+81 3-3433-5111',
    'https://www.tokyotower.co.jp/',
    'https://maps.google.com/?cid=10281119591005088802',
    10000,
    '{"text": "Tokyo Tower", "languageCode": "ja"}',
    NULL,
    NULL,
    NULL,
    NOW(),
    NOW()
),
(
    'ChIJN1t_tDeuEmsRUsoyG83frY4',
    '일본 〒150-0001 Tokyo, Shibuya City, Jingumae, 4 Chome−2−8',
    35.6592606,
    139.7002586,
    4.6,
    '03-3409-4811',
    '+81 3-3409-4811',
    'https://www.meijijingu.or.jp/',
    'https://maps.google.com/?cid=123456789',
    15000,
    '{"text": "Meiji Jingu", "languageCode": "ja"}',
    NULL,
    NULL,
    NULL,
    NOW(),
    NOW()
),
(
    'ChIJ_xkgOmOuEmsR8FhZz3qJN1I',
    '일본 〒150-0043 Tokyo, Shibuya City, Dogenzaka, 2 Chome−1',
    35.6580339,
    139.7016358,
    4.4,
    '03-3461-5111',
    '+81 3-3461-5111',
    'https://www.shibuya-scramble-square.com/',
    'https://maps.google.com/?cid=987654321',
    8000,
    '{"text": "Shibuya Scramble Square", "languageCode": "ja"}',
    NULL,
    NULL,
    NULL,
    NOW(),
    NOW()
);

-- Travel Template과 Place 매핑 데이터 삽입
-- 주의: travel_templates와 places의 실제 id 값을 확인하여 사용하세요.
-- 아래 쿼리는 방금 삽입한 데이터의 id를 참조합니다.

-- 1일차: 도쿄 타워 (sequence 1)
INSERT INTO travel_template_places (
    travel_template_id,
    place_id,
    sequence,
    day,
    traveler_tip,
    created_at,
    updated_at
) VALUES (
    (SELECT id FROM travel_templates WHERE travel_id = 'TRAVEL_001'),
    (SELECT id FROM places WHERE place_id = 'ChIJSc8jdZORQTURu6BMwxrKbGg'),
    1,
    1,
    '도쿄 타워는 저녁 시간대 방문하는 것이 좋습니다. 야경이 아름답습니다.',
    NOW(),
    NOW()
);

-- 1일차: 메이지 신궁 (sequence 2)
INSERT INTO travel_template_places (
    travel_template_id,
    place_id,
    sequence,
    day,
    traveler_tip,
    created_at,
    updated_at
) VALUES (
    (SELECT id FROM travel_templates WHERE travel_id = 'TRAVEL_001'),
    (SELECT id FROM places WHERE place_id = 'ChIJN1t_tDeuEmsRUsoyG83frY4'),
    2,
    1,
    '메이지 신궁은 조용한 분위기로 유명합니다. 아침 일찍 방문하면 더욱 좋습니다.',
    NOW(),
    NOW()
);

-- 2일차: 시부야 스크램블 스퀘어 (sequence 1)
INSERT INTO travel_template_places (
    travel_template_id,
    place_id,
    sequence,
    day,
    traveler_tip,
    created_at,
    updated_at
) VALUES (
    (SELECT id FROM travel_templates WHERE travel_id = 'TRAVEL_001'),
    (SELECT id FROM places WHERE place_id = 'ChIJ_xkgOmOuEmsR8FhZz3qJN1I'),
    1,
    2,
    '시부야 스크램블 스퀘어는 쇼핑과 식사 모두 즐길 수 있는 곳입니다. 옥상 전망대도 추천합니다.',
    NOW(),
    NOW()
);
