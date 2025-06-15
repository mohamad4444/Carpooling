CREATE TABLE offer
(
    offer_id   INT  AUTO_INCREMENT PRIMARY KEY,
    user_id    INT  NOT NULL,
    start_time TIMESTAMP NOT NULL,
    distance      INT  NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE
) CHARACTER SET utf8mb4;