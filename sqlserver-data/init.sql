-- Insert data for 'accounts' table
INSERT INTO accounts (email, password, role, is_active, deleted_at) VALUES
('john.doe@example.com', 'hashed_password_1', 'ROLE_USER', 1, NULL),
('jane.smith@example.com', 'hashed_password_2', 'ROLE_ORGANIZATION', 1, NULL),
('admin@example.com', 'hashed_password_3', 'ROLE_ADMIN', 1, NULL),
('volunteer.user@example.com', 'hashed_password_4', 'ROLE_USER', 1, NULL),
('org.charity@example.com', 'hashed_password_5', 'ROLE_ORGANIZATION', 1, NULL);

-- Insert data for 'volunteer' table
INSERT INTO volunteer (username, email, pic, contact, account_id, is_active, deleted_at) VALUES
('johndoe_volunteer', 'john.doe@example.com', 'pic_john.jpg', '123-456-7890', 1, 1, NULL),
('volunteer_user_1', 'volunteer.user@example.com', 'pic_volunteer1.jpg', '987-654-3210', 4, 1, NULL);

-- Insert data for 'organization' table
INSERT INTO organization (organization_name, description, volunteer_id) VALUES
('Green Earth Alliance', 'Dedicated to environmental protection and sustainability.', 2),
('Helping Hands Foundation', 'Provides aid to underprivileged communities.', 1);

-- Insert data for 'charity_events' table
INSERT INTO charity_events (organization_id, charity_name, description, destination, date_start, date_end, num_volunteer_require, num_volunteer_actual, note, pic, event_status) VALUES
(1, 'Beach Cleanup Day', 'Join us to clean up local beaches.', 'Coastal Area', '2025-08-10 09:00:00', '2025-08-10 17:00:00', 50, 35, 'Bring your own gloves and water bottle.', 'pic_beach_cleanup.jpg', 'UPCOMING'),
(2, 'Community Food Drive', 'Collecting non-perishable food items for those in need.', 'City Center', '2025-09-01 10:00:00', '2025-09-15 18:00:00', 20, 15, 'Drop-off points available at various locations.', 'pic_food_drive.jpg', 'ONGOING'),
(1, 'Tree Planting Initiative', 'Help us plant trees to reforest urban areas.', 'Forest Park', '2025-10-20 08:00:00', '2025-10-20 16:00:00', 30, 0, 'Tools will be provided.', 'pic_tree_planting.jpg', 'UPCOMING');

-- Insert data for 'donation_events' table
INSERT INTO donation_events (organization_id, title, description, money_need, event_status, has_donate, note, qr_pic, bank_account, pic) VALUES
(1, 'Support Wildlife Sanctuary', 'Funds to maintain and expand our wildlife sanctuary.', 10000.00, 'OPEN', 0, 'Every donation helps protect endangered species.', 'qr_wildlife.png', '1234567890', 'pic_wildlife_donation.jpg'),
(2, 'Education for All', 'Collecting donations to provide school supplies for children.', 5000.00, 'OPEN', 0, 'Empowering the next generation through education.', 'qr_education.png', '0987654321', 'pic_education_donation.jpg');

-- Insert data for 'volunteer_charity_event' table
INSERT INTO volunteer_charity_event (volunteer_id, charity_event_id, join_date, join_status) VALUES
(1, 1, '2025-07-15 10:00:00', 'REGISTERED'),
(2, 1, '2025-07-16 11:30:00', 'REGISTERED'),
(1, 2, '2025-07-10 14:00:00', 'REGISTERED');

-- Insert data for 'volunteer_donation' table
INSERT INTO volunteer_donation (volunteer_id, donation_event_id, donate_amount, donation_date, note) VALUES
(1, 1, 50.00, '2025-07-14 10:30:00', 'For the animals.'),
(2, 2, 25.00, '2025-07-15 16:00:00', 'Hope this helps.');

-- Insert data for 'requests' table
INSERT INTO requests (request_type, volunteer_id, organization_id, status, deny_reason, pic, request_date) VALUES
('ORGANIZATION_REGISTRATION', 1, NULL, 'PENDING', NULL, 'pic_org_reg_request.jpg', '2025-07-10 09:00:00'),
('ADD_NEW_CHARITY_EVENT', 2, 1, 'APPROVED', NULL, 'pic_new_charity_event_request.jpg', '2025-07-12 11:00:00'),
('UPDATE_DONATION_EVENT', 1, 2, 'PENDING', NULL, 'pic_update_donation_event_request.jpg', '2025-07-13 14:00:00');

-- Insert data for 'notifications' table
INSERT INTO notifications (content, event_id, volunteer_id, organization_id, created_at, [read]) VALUES
('Your registration for Beach Cleanup Day has been confirmed.', 1, 1, NULL, '2025-07-15 10:05:00', 0),
('New donation event: Support Wildlife Sanctuary.', 1, NULL, 1, '2025-07-16 09:00:00', 0),
('Your request for organization registration has been approved.', NULL, 1, NULL, '2025-07-17 08:30:00', 0);