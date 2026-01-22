-- 기존 테스트 데이터 삭제 (외래키 참조 순서 고려)
DELETE
FROM travel_template_places
WHERE travel_template_id IN (SELECT id
                             FROM travel_templates
                             WHERE travel_id = 'TRAVEL_001');
DELETE
FROM travel_templates
WHERE travel_id = 'TRAVEL_001';
DELETE
FROM places
WHERE google_place_id IN (
                          'ChIJSc8jdZORQTURu6BMwxrKbGg',
                          'ChIJN1t_tDeuEmsRUsoyG83frY4',
                          'ChIJ_xkgOmOuEmsR8FhZz3qJN1I'
    );

-- 테스트용 Travel Template 데이터 삽입
INSERT INTO travel_templates (travel_id,
                              youtuber,
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
                              profile_image,
                              created_at,
                              updated_at)
VALUES ('TRAVEL_001',
        'PANI_BOTTLE',
        '빠니보틀',
        '태국',
        '방콕',
        '여름철 고온다습, 가벼운 옷차림 권장. 우산 필수.',
        '식당에서 팁 불필요, 조용히 식사하는 것이 예의. 신발 벗는 곳이 많음.',
        '라멘과 초밥이 유명하며, 현지 식당에서 현금 결제가 일반적. 편의점 음식도 훌륭함.',
        'https://i.ytimg.com/vi/F2utz6L76D0/mqdefault.jpg',
        'https://www.youtube.com/watch?v=F2utz6L76D0',
        1200000,
        '빠니보틀은 주말을 이용해 직장인들도 충분히 다녀올 수 있는 ''금요일 퇴근 후 방콕 여행''의 가능성을 보여주며, 곽튜브와의 티격태격 케미를 통해 방콕의 매력을 소개합니다',
        '방콕 풀코스, 동남아 안 가본 곽튜브와 함께 【방콕】',
        3,
        4,
        'https://i.ytimg.com/vi/F2utz6L76D0/mqdefault.jpg',
        NOW(),
        NOW());

-- 테스트용 Place 데이터 삽입 (3개)
-- 도쿄 타워
INSERT INTO places (`created_at`, `updated_at`, `formatted_address`, `google_maps_uri`,
                    `google_place_id`, `international_phone_number`, `latitude`, `longitude`, `name`,
                    `national_phone_number`, `photos_json`, `rating`, `regular_opening_hours`, `thumbnail`,
                    `user_rating_count`, `website_uri`)
VALUES (NOW(),
        NOW(),
        '4-chōme-2-8 Shibakōen, Minato City, Tokyo 105-0011 일본',
        'https://maps.google.com/?cid=5195627782660688349&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA',
        'ChIJCewJkL2LGGAR3Qmk0vCTGkg',
        '+81 3-3433-5111',
        '35.6585805',
        '139.7454329',
        '도쿄 타워',
        '03-3433-5111',
        '[{\"name\": \"places/ChIJCewJkL2LGGAR3Qmk0vCTGkg/photos/AcnlKN1RNbCQGOrfMPtwZikKvbr8vi8fELB_FJCY-jMFfEupKQedHRHMWn9S1WpJU4K5uPkHweFrtVVjZ1ZjQTBHslYu37dLyiL04b4ed1fKPKCxOUvYxm6Od7DsjmtQDyL6jREaflAg_SMnFgv1eLjCcYAr1tKrs5q9MzrzyZsmlC_G73XnhqZCoMBjwl4DdfO3ZOlnpAlN1UqCq9fcX1Pha5iDVVsuVizxNOTr9_u5TRpRRDhZ2rPQihPMuM1qHg7z_MuFcCo0sAmSzz1QUxFnqlo_GtT44TQnizZjq9AE39Wh7tqodD3qY0xs4hnZwoEYXVa1Z62-syYFk8bOrAd8Aue9InsexJrh8rLS7aPIkDjhfWjW82R8BCPXeZTnsA1D0_lOchNQQmyuCzQSO29t1tdd2GAhcwKmSiUzl58bLyzIEA\", \"widthPx\": 4800, \"heightPx\": 3600}, {\"name\": \"places/ChIJCewJkL2LGGAR3Qmk0vCTGkg/photos/AcnlKN1I217qzYQ2YFuPXGX2xuEmMxrb2i2haqlurWbT0ItysI7S65kkmn80-0zHkxMvA8Ru6uuaURJYnJWXMEn2qzvDzKNx24NLOCzr8PbaRvq8hM1UL3l6r_pRGDlVLA2i4KPt4y6ffnbUeOflRTTNKdo75RUL8ANfy4_aISjBtJzr5TgcELVQHpVtiED8yUeM-Bn014YXx8-BMPtRvc5Bql0cyK0IfTZz0ymsyBvPn5EPIYZuo_-I0uaDfwcc2CpeL0j3uN0btUJbPkLkbU21Iys-Tcd-vRuerCG03x0-fNWpdXDDijkGLwdc6qs6wdKncUQHBQpGN0FCT9nHPI1MwOQaNEYO_4mklMSR3_pXeogO9zrAdH8ZV6GCG1tlCBUkwsSEgDe6eqgLANivTtIRZqKOUEjNICQ_SieR0wckd7I\", \"widthPx\": 591, \"heightPx\": 886}, {\"name\": \"places/ChIJCewJkL2LGGAR3Qmk0vCTGkg/photos/AcnlKN0UcnxFvx-o0f_3rEVK5-YRr7pDyLYHxqO-7HJTyAe6E5dQofdnkKQveLNiQOisTp5ijiL8OlGs0TZJ-yvGKES_e8owKlHB4qAj--_THIYnlCcRbfO-4mZ1Rj1ReS54blyIejPGMLQfn6j53-0zu-7Nxghk4XMD507mMclggZtRw-aEl1rbuPomLclEFsTNrEM7jkIxUCLb9pi5EQFOqbzlU7s6ObAShtjEyr4iqkEohnC-WUqLL-kOXXYJCQA0dT6tj937IkJez-0VP8O_0lj09qXNRURv9XkSXpsd9csAFaX9G_VBnnR29YKm6bvP6Z1u543TEsUBTPiyCN0N1APSDDeONk4Ijk7TSO6odRmG4Eajfc3zVro_77ApCn96TYm0stjLxNiQVRkFrDGN_z6wuLemmSXZG-y8w6qVEgARGJXnjvmdmMxoMVWjPg\", \"widthPx\": 4800, \"heightPx\": 3193}, {\"name\": \"places/ChIJCewJkL2LGGAR3Qmk0vCTGkg/photos/AcnlKN1ffaokMlVYRw8Z6cn9RrIrlbVuli0cuWOjMfxDbsvPGnQdDjjbgtYXQ1QnYR7KxTWd1e10aUltWvUsv9CcI8yWcG7XxdD0pkuD3Uu3PydhJ7ydxqvSIW8cLQcz0IxMXPobbP2Tfp11n04UXB7qVcuxkiYD4a4WnLx7rGwWkrALYlLOTh-eeAe7dyzN7QJETjEF3B_Q-ITnDjnyuv9b9bsC3-ruhdgz99hHp6OQtaMiVhRvOyjVIWToGxzAzzwntXfr8SlarJcWvh1UaX5-o3weVOampa4LIxJ2tq4mLovEBypmRWeotiCT0riEDTFDF3XEw_W0rpJFRFUlP73QlYuvWtbORF5G-qyB1RKVHLxGUyFbKw98TeS4yB2mu9TM6qIKNKp_7jw0oCyg4iQ4WScc38rjerI5qNTtlYfwQxu7ag\", \"widthPx\": 4080, \"heightPx\": 3072}]',
        '4.5',
        '[\"월요일: AM 9:00 ~ PM 11:00\", \"화요일: AM 9:00 ~ PM 11:00\", \"수요일: AM 9:00 ~ PM 11:00\", \"목요일: AM 9:00 ~ PM 11:00\", \"금요일: AM 9:00 ~ PM 11:00\", \"토요일: AM 9:00 ~ PM 11:00\", \"일요일: AM 9:00 ~ PM 11:00\"]',
        'https://lh3.googleusercontent.com/place-photos/AEkURDym40I4XyqXUosRz8bTu9aPvDUklxkfM79KCa03C0SQTnDaTu_RXXiWQjCRZ3-yK4dTbzoySqMrucj1ubPQNUZ5yKseTRfmaME5C--5jLYB0rU-MLXqUabNEk3myTWywzIuEHcKz_I-H4Xtdg=s4800-w4800-h3600',
        '92813',
        'https://www.tokyotower.co.jp/');

-- 메이지 신궁 --
INSERT INTO places (`created_at`, `updated_at`, `formatted_address`, `google_maps_uri`,
                    `google_place_id`, `international_phone_number`, `latitude`, `longitude`, `name`,
                    `national_phone_number`, `photos_json`, `rating`, `regular_opening_hours`, `thumbnail`,
                    `user_rating_count`, `website_uri`)
VALUES (NOW(),
        NOW(),
        '1-1 Yoyogikamizonochō, Shibuya, Tokyo 151-8557 일본',
        'https://maps.google.com/?cid=10361244767556222835&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA',
        'ChIJ5SZMmreMGGARcz8QSTiJyo8',
        '+81 3-3379-5511',
        '35.6763976',
        '139.6993259',
        '메이지 신궁',
        '03-3379-5511',
        '[{\"name\": \"places/ChIJ5SZMmreMGGARcz8QSTiJyo8/photos/AcnlKN1qg5FC8QMHAAyANQT-hrwmUITq83YW49z_e1KQB5ewh0DM3S_vXrM1bBsH6a_4sGfZIQhb7T96TzytqQDcftQOQTXjr9oHHJX7l2pvI0PBb0Rd1OdUDiRsmG-D8-jnXuJvXVdY7d1w5yd0mWRaep6L2CRKJAO1CPrMGsXO2b4M0EkBgA5B6P15K6BxcgHzNeZW51h47lgfwqLfAQMqJqjK9Ji_ppjaaBXUFqLcHh44Jzk22ordR-RDFYX_8aVWNgsDKIfN8KUAr9ditJi_OZ0g8wZaJEKOw9PrqGxOERCfgx_tXUlBXLhoTy4y_VmoVQOvQ_ECTmFWJsDGJX4mulQEiTu2uQpcR-qWbdLwzI8lhrf60fZ-oUDROi25agAS2k3qJUEwYShjDQXDp5lhLKdSKXpsYK3Sdr-MA_PVu1JW3xiu\", \"widthPx\": 4800, \"heightPx\": 3600}, {\"name\": \"places/ChIJ5SZMmreMGGARcz8QSTiJyo8/photos/AcnlKN2kArTUc5bSCeW-PkL7gpm1Du0Aue2yLXH03Y7NPATBUZ5kuaVfQ3jjCxkUGQQIk7kXo_DNZJQ4Z7T1DBWfphF97O8IiV3a_WMalbb1xbQDad_eoWiu_3uBdnj1em8ZgJoSZosxL3lsAx9NMifMhPBhlTxTRb42v7MZUsTQAK_12aQWqRRqu0ywohydOWmVu2cfObvSgaAOcnOSy9-tIgiyxvA4avuJ0VbLTtueMIgoBB6NOtMJravdTeLofITml01SXV1Ul4qHurHYv23NKpVKxkxZsR8qqEDmJ_AUTX7uiCTANHr6jTDJlLrCI65elxCm18PHbuZzurE4uoPeELlWHllWMlt4KacKj-4bAoBbi3j04dheMb34Dd47KS6oOHb-3PDvWSV-vC_7DxbtgrS-XvzVcrqs2oeZCz6yVe-vGaRK\", \"widthPx\": 1170, \"heightPx\": 1048}, {\"name\": \"places/ChIJ5SZMmreMGGARcz8QSTiJyo8/photos/AcnlKN17vdNYx3LXMbFkkGvKYEAGuM4XCHQhYow2eIqXjR1IcqYlYFNJDqfE_NG4DgmSggn2RwtFOvq8jl_0OzWrqHP99Rk0aE7mxoiipQRGZFqCLmU2ENtw2i-lsm9TFAlid-48kh8ldzwJo3dX1UfsqI7Vvos_ekj98jEQIM--Bpt1ruoszGUD7xb67Vk2wzmwWkjZ9ob-qUSu-wqy7hFL1kGw_zI26wSig6gysHwcpk4Gd5C0c8Lfp2Fgyc4Z64m5ZMNk-xxvKnb0DE8QxXu3OvDuDODyGvqZkhnBWMTlcNYU_OHuhX7nD-lrozg5R5P_hiE4Hqi3ThYQP2If34Sgq8Wea-W-zwa9yxK99ghkE7pErBdNhm3FJn3uTZ3C2sWdyWqitgsDdne7RRKVG7hxsIKiDSM_619mmZUdShxBhcmGDw\", \"widthPx\": 4032, \"heightPx\": 3024}]',
        '4.6',
        null,
        'https://lh3.googleusercontent.com/place-photos/AEkURDzxiZRdmqJLnwzpndMSh2aKK3KqLQFHhOVp66cs7RMrSV3QMFQz367pNWgMUgWcuFDdLWbdOvXXpY4GpNWnFMG_BRG_XX9XAnvgLBLZQcCQrSgAHESsO4bWBGAABQQtnDi3fZEqUoZNZdayFyY=s4800-w4800-h3600',
        '47809',
        'https://www.meijijingu.or.jp/');

-- 시부야 스크램블 --
INSERT INTO places (`created_at`, `updated_at`, `formatted_address`, `google_maps_uri`,
                    `google_place_id`, `international_phone_number`, `latitude`, `longitude`, `name`,
                    `national_phone_number`, `photos_json`, `rating`, `regular_opening_hours`, `thumbnail`,
                    `user_rating_count`, `website_uri`)
VALUES ( NOW(),
        NOW(),
        '2-chōme-24-12 Shibuya, Tokyo 150-0002 일본',
        'https://maps.google.com/?cid=8716402840027143535&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA',
        'ChIJscDhJ4SLGGARbx0GlzPi9ng',
        null,
        '35.6584638',
        '139.70226209999998',
        '시부야 스크램블 스퀘어',
        null,
        '[{\"name\": \"places/ChIJscDhJ4SLGGARbx0GlzPi9ng/photos/AcnlKN3EcBMTFPVzYod1YfSATNVLsdVybsm9V-OwiljjvKwRy8wRJwCME62oVAK6dHu-7m1RYkqsc6IdcfzTJxgmjjRDrG_tkEwgdFvGsZYrBQF1wK9JQS5eKjVIFmAs8Uw3ZzeTS1VE6osBQACbq_cXfWqfIpOZ8mSL2cCo1Sd6DiYIGMGQnwthDz9joEZYDPOSYixjbEjZUYH6Z9iGE42nOQ4VyZ-EnqQZDOBkpbB_gfmLtK6t94pIjuE_hp0C3Ol-XXBc9vUItn1YUD-yx7EKzcJdSP1iS8N-ci7VC9OtkqZIzbIS-9nDlo0gcOgl0XewhBpT9xXozNPIr1N0F1yOytIKmx8Xi6_K-tEjdrdJVChnyb8OOrpylhyxJ_cILRhNPRBLySZVqFpwyu7ixx8P1xXeqiZZe3TyfPRskIcWgktptw\", \"widthPx\": 4000, \"heightPx\": 3000}, {\"name\": \"places/ChIJscDhJ4SLGGARbx0GlzPi9ng/photos/AcnlKN0NAyBPB2NTy0wbxXUXnnIbip1dMQQWUt4kvA4jM1Yf5jJI7ZurRc4TEow8vqiKDR1HpS_W6EXS-c2n8VIvXqTNEgmkNUQiIi8Yyc775BhhOIq2bD4D_I3tUQiQ1rpIt0NTiO-A36d_bc1KSAyuiji6uw7YMP4vUxO3ItSy1nlropXcGnpxaY-ZoQDekEnuzl9eNaNiT2XWbYNU-Ose7yR3lSumZ4m4GtWwtvFPq7qOJhfIYXxrOuWs9Pp0Iw7bZWG0Llrcpnp0t7eonY41ZudjqLFD9gFdl_bY-btVrSsUrG_CCTsjZAQLFf6wH3kD4w1gz9agkbcQHenMWUORZk9VIOqBW4T9-azo1RSVzdBGYuVfFM1BUhaWYNkWy_4KC_93rjeaXoWLfiChAFvT56LJQ-xH5H4m2R_c69xO-3kJpA\", \"widthPx\": 4000, \"heightPx\": 3000}, {\"name\": \"places/ChIJscDhJ4SLGGARbx0GlzPi9ng/photos/AcnlKN2j9mU3D1jRiIQ7Fz8a0jrkXpe-dcZedbDhV0-yTioi9CbKLWq7bFwHYm-t1Gy3S1hTfUz4SoVtcHxYv0sxMkaUdwRcQ03DXBhrdHYDgH6CmoOf9up_Fga-QgZT9MyiIpN2ucHfFt6tN8aR_P3KHdnMCEqfOYIgoNbqqZUyYkcntuMMmc2ZMqfHcpu4f7Ssjjnesbwz5yDVlznOcDkndMClF8zoql-1Z-b8-3EO0rst_v9eRPnFKC77Pbcedeo4kGpqwaYr656TsceF49VBHrqB7M2zB48LnqWgJCepYcmdY-aK5v2LIXnBx3HFtc34e1zRGXS3vgQSGg0eiQi6ukNnik19dJrBAPaRrJTQhv6JWwMp4UlDJSZf-yZu2OdM4V0PBxDvz1AooHkVef4w3oVbOeZQJosyB3jDiaoM8os\", \"widthPx\": 3024, \"heightPx\": 3024}, {\"name\": \"places/ChIJscDhJ4SLGGARbx0GlzPi9ng/photos/AcnlKN2PLgGoFnMTH435KgUhBE0IC77JQ5Rg1qbl5ygeXcEnMRAS4qig-k1RcIGSTtGb8uUMX3W3ZPq_59OyQknR9ZkaC3CQMGfrDaqq6PKby1wHwYoWGQ3PSt6XEo_LpdpnbPMU-WkBW96K5RdQMee95yfvyvSiC8R_1LNxPJdvq0BL1OCTKOpsAC_kB2fbrYtwjeRT40RONTr-GHZZpI-Cv7LXLIzk-OexWwrkcI1DcexNcjYnISlLdQL4dtMjjXDi34B2vbHzp5F6QJWovxNpIQxEj5w5_ADPQFfYJyRaUdKLTIseemiqGiYIzsI48ka-NKhXlM1U3X5qGSS68IvGiXKE9TP0eeOsAVgN7KVXs0J9ihtg4pdS1rZecnoXdDCTg71ykCOXpCKf3Iq3UlxFdevG-3tR51wkx1NeBipw4bOxvotB\", \"widthPx\": 4032, \"heightPx\": 3024}, {\"name\": \"places/ChIJscDhJ4SLGGARbx0GlzPi9ng/photos/AcnlKN3ZajIlCWp0TqQfCyobU4Zvmr2xz-CqSen4eKWD1muHHfJcLYnf5V59xg43Co04lGxhPh8OQnPULGsl-ysflF9ZeOc5oxbeUyqCbCi5_w9szZ6S0ZJYZCVAhabOmRoouHO8Q5di8VwV6NN1U1CtRRAZMEzZ7bysxAyWt38dyz_DNPLqj2kVFWrOyv1N7MEwgugB0lclVxDirdb2bgS00pBzcDgjwqBN3WRWV1oi2ehI__tm4iUGjDJeNySfl55Smnh5RwYhjiWGQY6_6YIW0uTn1gq0CVYYXUSz0pixofQBr4POL7tkFGR58cNL20tBanP4Y33FYKdI5-ruuqu0fe7-p-kzP9hjlBOD1wr_rUejbyRdtAlro5T-zW8f1KWlISFPmXiw2iFKFv-cn3dk1sJ9CwLmujuvVE1G7GE_jiZg7Q\", \"widthPx\": 4000, \"heightPx\": 3000}, {\"name\": \"places/ChIJscDhJ4SLGGARbx0GlzPi9ng/photos/AcnlKN3iuuoaPVvwJbASoDyvjD8wdgRYcUXHANMKh5ZMSzfjFLhHGGDEF8vqgsnkO7WEXfqj1NgGKRlC6ub6BtpAV3W2LjVJeyhfYsV0KO4qOwWhYt-eGrub0PdU9ZHymX6qpHQnoTV7KpK131Su83nVcAN9Sm-QTSyjBvUv3aqQh-2H9RWkCPHyIUH_U-ri93Rg2AoN_ocue9N6XAdajf0nFklEKXzTX-uTOpRGedJZ9JOiW3biw_j5N5d8kUeL_Almf-vC_R5NY0vHV8Odi72M9fxHM_gRn2I0fcMtJ_xccI-eZqb5xeHZSELUf7mhafnmqRv-Gw3DoIA2gfm9aRetNqF-LZmL59uD2hEjaVvr25hl5iQraB51lrU02I7ZAH-n0w_nNkyTLJpNGuhijDIUGlx5e3fyaRH-T74Ncey-dqWnFQ\", \"widthPx\": 4032, \"heightPx\": 3024}]',
        '4.2',
        '[\"월요일: AM 10:00 ~ PM 9:00\", \"화요일: AM 10:00 ~ PM 9:00\", \"수요일: AM 10:00 ~ PM 9:00\", \"목요일: AM 10:00 ~ PM 9:00\", \"금요일: AM 10:00 ~ PM 9:00\", \"토요일: AM 10:00 ~ PM 9:00\", \"일요일: AM 10:00 ~ PM 9:00\"]',
        'https://lh3.googleusercontent.com/place-photos/AEkURDwg9kmB7Beq2XD-e4vBu9CDYQh1pyRCymMycD3RyxChlVoy9gchdsZ5t2tiKr7AHp14p6JNUui8x5FrmtRRYS4bdlFK9WdP0J1JDvAskQ5tHfr7ydbB-1Z_3ZyIj1qAoT6dGz-PoGZos3lXHQ=s4800-w4000-h3000',
        '8941',
        'https://www.shibuya-scramble-square.com/');


-- Travel Template과 Place 매핑 데이터 삽입
-- 주의: travel_templates와 places의 실제 id 값을 확인하여 사용하세요.
-- 아래 쿼리는 방금 삽입한 데이터의 id를 참조합니다.

-- 1일차: 도쿄 타워 (sequence 1)
INSERT INTO travel_template_places (travel_template_id,
                                    place_id,
                                    sequence,
                                    day,
                                    traveler_tip,
                                    estimated_duration,
                                    created_at,
                                    updated_at)
VALUES ((SELECT id FROM travel_templates WHERE travel_id = 'TRAVEL_001'),
        (SELECT id FROM places WHERE google_place_id = 'ChIJCewJkL2LGGAR3Qmk0vCTGkg'),
        1,
        1,
        '도쿄 타워는 저녁 시간대 방문하는 것이 좋습니다. 야경이 아름답습니다.',
        60,
        NOW(),
        NOW());

-- 1일차: 메이지 신궁 (sequence 2)
INSERT INTO travel_template_places (travel_template_id,
                                    place_id,
                                    sequence,
                                    day,
                                    traveler_tip,
                                    estimated_duration,
                                    created_at,
                                    updated_at)
VALUES ((SELECT id FROM travel_templates WHERE travel_id = 'TRAVEL_001'),
        (SELECT id FROM places WHERE google_place_id = 'ChIJ5SZMmreMGGARcz8QSTiJyo8'),
        2,
        1,
        '메이지 신궁은 조용한 분위기로 유명합니다. 아침 일찍 방문하면 더욱 좋습니다.',
        90,
        NOW(),
        NOW());

-- 2일차: 시부야 스크램블 스퀘어 (sequence 1)
INSERT INTO travel_template_places (travel_template_id,
                                    place_id,
                                    sequence,
                                    day,
                                    traveler_tip,
                                    estimated_duration,
                                    created_at,
                                    updated_at)
VALUES ((SELECT id FROM travel_templates WHERE travel_id = 'TRAVEL_001'),
        (SELECT id FROM places WHERE google_place_id = 'ChIJscDhJ4SLGGARbx0GlzPi9ng'),
        1,
        2,
        '시부야 스크램블 스퀘어는 쇼핑과 식사 모두 즐길 수 있는 곳입니다. 옥상 전망대도 추천합니다.',
        120,
        NOW(),
        NOW());

