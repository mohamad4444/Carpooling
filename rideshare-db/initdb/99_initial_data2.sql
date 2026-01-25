-- Offers (ride providers)
INSERT INTO `offer` (`user_id`, `start_time`, `distance`)
VALUES 
    -- Zweibrücken users (close to campus)
    (1, TIMESTAMPADD(HOUR, 2, NOW()), 5),  -- test1 offering now+2h (5km radius)
    (5, TIMESTAMPADD(HOUR, 3, NOW()), 10), -- test5 offering now+3h (10km radius)
    (6, TIMESTAMPADD(MINUTE, 90, NOW()), 3), -- test6 offering now+1.5h
    
    -- Homburg users (~15km from campus)
    (2, TIMESTAMPADD(HOUR, 4, NOW()), 15),  -- test2 offering now+4h (large radius)
    (3, TIMESTAMPADD(HOUR, 2, NOW()), 8),   -- test3 offering now+2h
    (4, TIMESTAMPADD(MINUTE, 150, NOW()), 5), -- test4 offering now+2.5h
    
    -- Kaiserslautern users (~30km from campus)
    (10, TIMESTAMPADD(HOUR, 5, NOW()), 20), -- test10 offering now+5h (very large radius)
    (11, TIMESTAMPADD(HOUR, 6, NOW()), 15);

-- Requests (ride seekers)
INSERT INTO `request` (`user_id`, `start_time`)
VALUES
    -- Users in Zweibrücken (should match local offers)
    (7, TIMESTAMPADD(MINUTE, 110, NOW())),  -- test7 needs ride ~now+1.8h (matches test6)
    (8, TIMESTAMPADD(MINUTE, 130, NOW())),  -- test8 needs ride ~now+2.2h (matches test1, test3)
    
    -- Users in Homburg (should match Homburg offers)
    (9, TIMESTAMPADD(HOUR, 3, NOW())),     -- test9 needs ride now+3h (matches test2, test4)
    
    -- Users in Kaiserslautern (only matches large radius offers)
    (12, TIMESTAMPADD(HOUR, 4, NOW())),    -- test12 needs ride now+4h (matches test10)
    (13, TIMESTAMPADD(HOUR, 5, NOW()));