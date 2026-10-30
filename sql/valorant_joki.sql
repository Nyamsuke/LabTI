-- ============================================================
--  Aplikasi VALORANT JOKI
--  Skrip pembuatan database + data awal
--  MySQL 8.x  (XAMPP / Laragon)
-- ============================================================

DROP DATABASE IF EXISTS valorant_joki;
CREATE DATABASE valorant_joki
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE valorant_joki;

-- ------------------------------------------------------------
-- 1. Tabel users
-- ------------------------------------------------------------
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nama VARCHAR(100) NOT NULL,
    role ENUM('admin', 'user') NOT NULL DEFAULT 'user'
);

-- ------------------------------------------------------------
-- 2. Tabel layanan
-- ------------------------------------------------------------
CREATE TABLE layanan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_layanan VARCHAR(100) NOT NULL,
    rank_awal VARCHAR(50) NOT NULL,
    rank_tujuan VARCHAR(50) NOT NULL,
    harga DECIMAL(12,2) NOT NULL,
    deskripsi TEXT,
    status ENUM('Aktif', 'Nonaktif') DEFAULT 'Aktif'
);

-- ------------------------------------------------------------
-- 3. Tabel pesanan
-- ------------------------------------------------------------
CREATE TABLE pesanan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    kode_pesanan VARCHAR(30) NOT NULL UNIQUE,
    id_user INT NOT NULL,
    id_layanan INT NOT NULL,
    id_akun_valorant VARCHAR(100) NOT NULL,
    password_valorant VARCHAR(100) NOT NULL,
    tanggal_pesanan DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_harga DECIMAL(12,2) NOT NULL,
    status_pesanan ENUM(
        'Menunggu Pembayaran',
        'Menunggu Konfirmasi',
        'Diproses',
        'Selesai',
        'Dibatalkan'
    ) DEFAULT 'Menunggu Pembayaran',
    FOREIGN KEY (id_user) REFERENCES users(id),
    FOREIGN KEY (id_layanan) REFERENCES layanan(id)
);

-- ------------------------------------------------------------
-- 4. Tabel pembayaran
-- ------------------------------------------------------------
CREATE TABLE pembayaran (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pesanan INT NOT NULL,
    metode VARCHAR(50) NOT NULL,
    nominal DECIMAL(12,2) NOT NULL,
    tanggal_pembayaran DATETIME DEFAULT CURRENT_TIMESTAMP,
    status_pembayaran ENUM(
        'Menunggu Konfirmasi',
        'Dikonfirmasi',
        'Ditolak'
    ) DEFAULT 'Menunggu Konfirmasi',
    FOREIGN KEY (id_pesanan) REFERENCES pesanan(id)
);

-- ------------------------------------------------------------
-- 5. Tabel riwayat_status
-- ------------------------------------------------------------
CREATE TABLE riwayat_status (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pesanan INT NOT NULL,
    status_lama VARCHAR(50),
    status_baru VARCHAR(50) NOT NULL,
    waktu_perubahan DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pesanan) REFERENCES pesanan(id)
);

-- ------------------------------------------------------------
-- Data awal : akun admin
-- ------------------------------------------------------------
INSERT INTO users (username, password, nama, role)
VALUES ('admin', 'admin123', 'Administrator', 'admin');

-- ------------------------------------------------------------
-- Data awal : layanan joki rank
-- ------------------------------------------------------------
INSERT INTO layanan
(nama_layanan, rank_awal, rank_tujuan, harga, deskripsi)
VALUES
('Bronze ke Silver', 'Bronze', 'Silver', 40000,
 'Layanan simulasi peningkatan rank'),
('Silver ke Gold', 'Silver', 'Gold', 60000,
 'Layanan simulasi peningkatan rank'),
('Gold ke Platinum', 'Gold', 'Platinum', 90000,
 'Layanan simulasi peningkatan rank'),
('Platinum ke Diamond', 'Platinum', 'Diamond', 100000,
 'Layanan simulasi peningkatan rank'),
('Diamond ke Ascendant', 'Diamond', 'Ascendant', 150000,
 'Layanan simulasi peningkatan rank');
