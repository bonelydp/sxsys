-- Update home page carousel (contour) to new banner images
UPDATE `contour` SET `name` = 'banner00.png', `img` = '/files/banner00.png' WHERE `id` = 14;
UPDATE `contour` SET `name` = 'banner01.png', `img` = '/files/banner01.png' WHERE `id` = 15;
UPDATE `contour` SET `name` = 'banner02.png', `img` = '/files/banner02.png' WHERE `id` = 16;

-- Update team introduction banners (contour1)
UPDATE `contour1` SET `Img` = '/files/team-banner-01.jpg', `name` = 'team-banner-01.jpg' WHERE `id` = 4;
INSERT INTO `contour1` (`id`, `Img`, `name`) VALUES (5, '/files/team-banner-02.jpg', 'team-banner-02.jpg')
    ON DUPLICATE KEY UPDATE `Img` = '/files/team-banner-02.jpg', `name` = 'team-banner-02.jpg';
INSERT INTO `contour1` (`id`, `Img`, `name`) VALUES (6, '/files/team-banner-03.png', 'team-banner-03.png')
    ON DUPLICATE KEY UPDATE `Img` = '/files/team-banner-03.png', `name` = 'team-banner-03.png';
