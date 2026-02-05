-- Auto-generated from application/src/main/resources/example-request.json

-- 1) Travel Template
INSERT INTO travel_templates (
    travel_id,
    youtuber,
    traveler,
    country,
    city,
    continent,
    budget_per_person,
    summary,
    title,
    nights,
    days,
    profile_image,
    created_at,
    updated_at
) VALUES (
             'Pani_Hanoi_2212',
             'PANI_BOTTLE',
             '빠니보틀 Pani Bottle',
             '베트남',
             '하노이',
             'SOUTHEAST_ASIA',
             98000, -- "98,000원" -> 98000 가정 (per person 여부 확인 필요)
             '노홍철, 곽튜브와 함께 베트남 하노이에서 연말을 보내며 현지 로컬 맛집과 문화를 즐기는 여행입니다. 하노이의 활기찬 거리에서 쌀국수를 맛보고 새해 카운트다운을 함께하며 청춘 여행의 감성을 담았습니다.',
             '탈모와 뚱보와 털보의 여행기 【베트남1】',
             2,
             3,
             'https://yt3.ggpht.com/Sr5y4IxegXCEZ0SYNvFB749crrAZmNpurZqfq2KvPEpiCYeakoMjBWMnW_56rMuYW_HipJOBRtU=s88-c-k-c0x00ffffff-no-rj',
             NOW(),
             NOW()
         );

-- 2) Places (필수 필드: google_place_id, latitude, longitude, name)
-- 실제 Google Place ID/좌표가 있으면 아래 값을 교체하세요.
INSERT INTO places (
    google_place_id,
    latitude,
    longitude,
    name,
    created_at,
    updated_at
) VALUES
      ('ChIJU8SvORUCNTERtCYCUqP64OY', 21.2178, 105.8025, 'Noi Bai International Airport', NOW(), NOW()),
      ('ChIJO7qN1b2rNTERi1n8m8ji4QU', 21.0336, 105.8465147, '퍼 자쭈옌', NOW(), NOW()),
      ('ChIJlclXM5WrNTERDqL5tGu_ugE', 21.0286, 105.8521, '호안끼엠 호', NOW(), NOW());

-- 3) Travel Template Places
INSERT INTO travel_template_places (
    travel_template_id,
    place_id,
    sequence,
    day,
    distance_km,
    transportation_json,
    youtube_tips_json,
    plan_b_json,
    estimated_duration,
    created_at,
    updated_at
) VALUES
      (
          (SELECT id FROM travel_templates WHERE travel_id = 'Pani_Hanoi_2212'),
          (SELECT id FROM places WHERE google_place_id = 'ChIJU8SvORUCNTERtCYCUqP64OY'),
          1,
          1,
          0.0,
          '[]',
          '[
            "하노이는 미세먼지가 상당히 심한 편이라 기관지가 예민하다면 마스크를 꼭 챙기는 게 좋아요.",
            "공항에서 시내로 들어갈 때는 미리 택시 앱을 이용하거나 정찰제 차량을 확인하고 타는 것이 안전해요."
          ]',
          '[{"name":"Noi Bai Airport Lounge","feature":"시내로 나가기 전 간단히 허기를 채우거나 휴식을 취하기 좋은 라운지에요."}]',
          60,
          NOW(),
          NOW()
      ),
      (
          (SELECT id FROM travel_templates WHERE travel_id = 'Pani_Hanoi_2212'),
          (SELECT id FROM places WHERE google_place_id = 'ChIJO7qN1b2rNTERi1n8m8ji4QU'),
          3,
          1,
          0.6,
          '[{"mode":"WALKING","time_min":8}]',
          '[
            "여기는 항상 줄이 긴 유명한 맛집이지만 회전율이 빨라서 생각보다 금방 자리가 나요.",
            "쌀국수를 먹을 때 옆에서 파는 튀김 도너츠인 ''꿔이''를 국물에 푹 찍어 먹으면 정말 맛있어요.",
            "길을 건널 때는 오토바이가 많아 무서워도 멈추지 말고 일정한 속도로 천천히 걸어가면 오토바이들이 알아서 피해가요."
          ]',
          '[{"name":"Phở 10 Lý Quốc Sư","feature":"하노이 3대 쌀국수 중 하나로 꼽히는 또 다른 쌀국수 맛집이에요."}]',
          40,
          NOW(),
          NOW()
      ),
      (
          (SELECT id FROM travel_templates WHERE travel_id = 'Pani_Hanoi_2212'),
          (SELECT id FROM places WHERE google_place_id = 'ChIJlclXM5WrNTERDqL5tGu_ugE'),
          4,
          1,
          0.7,
          '[{"mode":"WALKING","time_min":10}]',
          '[
            "새해 카운트다운 기간에는 호수 주변에 사람이 정말 어마어마하게 많으니 소지품 관리에 유의하세요.",
            "명당 자리를 찾기보다는 호수가 보이는 카페나 식당에 미리 자리를 잡는 게 훨씬 편해요.",
            "거리에서 파는 해바라기 씨를 까먹으면서 사람 구경하는 것도 베트남 현지 분위기를 느끼기에 좋아요."
          ]',
          '[{"name":"Tạ Hiện Street","feature":"북적이는 분위기에서 맥주 한잔하며 하노이의 밤을 즐기기 좋은 맥주 거리예요."}]',
          120,
          NOW(),
          NOW()
      );
