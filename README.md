<div align="center">

# 🔐 User Management System

**ユーザー・グループ・権限・プロフィールを管理する RESTful API**

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat-square&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-green?style=flat-square&logo=springsecurity)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?style=flat-square&logo=mysql)
![Cloudinary](https://img.shields.io/badge/Cloudinary-Storage-3448C5?style=flat-square&logo=cloudinary)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

</div>

---

## 🌐 言語 / Language / Ngôn ngữ

- 🇯🇵 [日本語](#-日本語)
- 🇺🇸 [English](#-english)
- 🇻🇳 [Tiếng Việt](#-tiếng-việt)

---

---

# 🇯🇵 日本語

## 📖 目次

- [概要](#概要)
- [主な機能](#主な機能)
- [使用技術](#使用技術)
- [ディレクトリ構成](#ディレクトリ構成)
- [セットアップ](#セットアップ)
- [APIエンドポイント](#apiエンドポイント)
- [環境変数](#環境変数)

## 概要

**User Management System** は、Spring Boot を使用して構築された REST API システムです。

ユーザー、グループ、権限、およびプロフィール情報を管理し、**RBAC（Role-Based Access Control）** によるアクセス制御を提供します。管理者はグループに対してView/Edit権限を細かく設定し、ユーザーのアクセスを柔軟にコントロールできます。

## 主な機能

### 🔑 認証
- **JWT** によるログイン認証
- **BCrypt** によるパスワード暗号化

### 👤 ユーザー管理
- ページネーション付きユーザー一覧取得
- ユーザー詳細取得
- ユーザー作成
- ユーザー情報更新
- ソフト削除（論理削除）

### 👥 グループ管理
- グループの作成 / 更新 / 削除
- グループへのユーザー追加
- グループからのユーザー削除

### 🛡️ 権限管理
- **RBAC** による権限制御
- 各機能への **View / Edit** 権限設定
- **Spring AOP（Aspect）** による権限チェック
- カスタム `@Permission` アノテーションによる認可

### 🪪 プロフィール管理
- プロフィール情報取得
- プロフィール更新
- パスワード変更
- **Cloudinary** によるアバター画像アップロード

### ⚙️ その他
- **Soft Delete** — 物理削除せず論理フラグで管理
- **Pagination** — 全一覧APIにpage/sizeパラメータ対応
- **Projection** — 必要なフィールドのみ取得するJPA最適化クエリ

## 使用技術

| 技術 | 用途 |
|---|---|
| Java 21 | コア言語 |
| Spring Boot | アプリケーションフレームワーク |
| Spring Security | 認証・認可 |
| Spring Data JPA | データベースORM |
| MySQL | リレーショナルデータベース |
| JWT | ステートレス認証 |
| BCrypt | パスワード暗号化 |
| Lombok | ボイラープレート削減 |
| Cloudinary | アバター画像ストレージ |

## ディレクトリ構成

```
src/main/java/com/example/
├── modules/
│   ├── auth/               # ログイン、JWT生成
│   ├── user/               # ユーザーCRUD
│   ├── group/              # グループ管理
│   ├── permission/         # 権限設定
│   ├── profile/            # プロフィール・アバター
│   └── feature/            # 機能定義
└── common/
    ├── entity/             # 基底エンティティ
    ├── dto/                # 共通DTO
    ├── enums/              # 列挙型
    └── service/            # 共通サービス
```

## セットアップ

### 前提条件

- Java 21+
- Maven 3.8+
- MySQL 8.x
- Cloudinaryアカウント

### インストール手順

**1. リポジトリをクローン**
```bash
git clone https://github.com/your-username/user-management-system.git
cd user-management-system
```

**2. データベースを作成**
```bash
mysql -u root -p -e "CREATE DATABASE user_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

**3. `application.yml` を設定**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_management
    username: your_db_username
    password: your_db_password

jwt:
  secret: your_jwt_secret_key
  expiration: 86400000  # 24時間 (ms)

cloudinary:
  cloud-name: your_cloud_name
  api-key: your_api_key
  api-secret: your_api_secret
```

**4. ビルドして起動**
```bash
mvn clean install
mvn spring-boot:run
```

APIは `http://localhost:8080` で利用可能になります。

## APIエンドポイント

### 認証
| メソッド | エンドポイント | 説明 |
|--------|----------|-------------|
| POST | `/api/auth/login` | ログイン・JWT取得 |

### ユーザー
| メソッド | エンドポイント | 説明 |
|--------|----------|-------------|
| GET | `/api/users` | ページネーション付きユーザー一覧 |
| GET | `/api/users/{id}` | ユーザー詳細取得 |
| POST | `/api/users` | ユーザー作成 |
| PUT | `/api/users/{id}` | ユーザー更新 |
| DELETE | `/api/users/{id}` | ユーザーのソフト削除 |

### グループ
| メソッド | エンドポイント | 説明 |
|--------|----------|-------------|
| GET | `/api/groups` | グループ一覧取得 |
| POST | `/api/groups` | グループ作成 |
| PUT | `/api/groups/{id}` | グループ更新 |
| DELETE | `/api/groups/{id}` | グループ削除 |
| POST | `/api/groups/{id}/users` | グループにユーザー追加 |
| DELETE | `/api/groups/{id}/users/{userId}` | グループからユーザー削除 |

### 権限
| メソッド | エンドポイント | 説明 |
|--------|----------|-------------|
| GET | `/api/permissions` | 権限一覧取得 |
| POST | `/api/permissions` | 権限をグループに付与 |
| DELETE | `/api/permissions/{id}` | 権限を削除 |

### プロフィール
| メソッド | エンドポイント | 説明 |
|--------|----------|-------------|
| GET | `/api/profile` | 自分のプロフィール取得 |
| PUT | `/api/profile` | プロフィール更新 |
| PUT | `/api/profile/password` | パスワード変更 |
| POST | `/api/profile/avatar` | アバターアップロード |

> ⚠️ `/api/auth/login` 以外の全エンドポイントは、`Authorization: Bearer <token>` ヘッダーが必要です。

## 環境変数

| 変数名 | 説明 |
|--------|------|
| `DB_URL` | MySQL接続URL |
| `DB_USERNAME` | データベースユーザー名 |
| `DB_PASSWORD` | データベースパスワード |
| `JWT_SECRET` | JWT署名用シークレットキー |
| `JWT_EXPIRATION` | JWTの有効期限（ミリ秒） |
| `CLOUDINARY_CLOUD_NAME` | Cloudinaryクラウド名 |
| `CLOUDINARY_API_KEY` | Cloudinary APIキー |
| `CLOUDINARY_API_SECRET` | Cloudinary APIシークレット |

---

---

# 🇺🇸 English

## 📖 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Environment Variables](#environment-variables)

## Overview

**User Management System** is a RESTful API built with **Spring Boot**, designed to handle user accounts, group management, permission control, and personal profiles.

The system implements **RBAC (Role-Based Access Control)**, allowing administrators to define granular View/Edit permissions per feature and assign them to groups.

## Features

### 🔑 Authentication
- Login with **JWT** (JSON Web Token)
- Password hashing with **BCrypt**

### 👤 User Management
- Get paginated user list
- Get user details by ID
- Create new user
- Update user information
- Soft delete user

### 👥 Group Management
- Create / Update / Delete groups
- Assign users to groups
- Remove users from groups

### 🛡️ Permission Management
- **RBAC** — Role-Based Access Control
- Configure **View / Edit** permissions per feature
- Permission checking via **Spring AOP (Aspect)**
- Custom `@Permission` annotation for controller-level authorization

### 🪪 Profile Management
- View personal profile
- Update profile information
- Change password
- Upload avatar via **Cloudinary**

### ⚙️ Additional
- **Soft Delete** — records flagged, not physically removed
- **Pagination** — all list APIs support page/size parameters
- **Projection** — optimized JPA queries returning only required fields

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Core language |
| Spring Boot | Application framework |
| Spring Security | Authentication & authorization |
| Spring Data JPA | Database ORM |
| MySQL | Relational database |
| JWT | Stateless authentication |
| BCrypt | Password encryption |
| Lombok | Boilerplate reduction |
| Cloudinary | Avatar image storage |

## Project Structure

```
src/main/java/com/example/
├── modules/
│   ├── auth/               # Login, JWT generation
│   ├── user/               # User CRUD
│   ├── group/              # Group management
│   ├── permission/         # Permission configuration
│   ├── profile/            # Profile & avatar
│   └── feature/            # Feature definitions
└── common/
    ├── entity/             # Base entities
    ├── dto/                # Shared DTOs
    ├── enums/              # Enumerations
    └── service/            # Shared services
```

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- MySQL 8.x
- Cloudinary account

### Installation

**1. Clone the repository**
```bash
git clone https://github.com/your-username/user-management-system.git
cd user-management-system
```

**2. Create the database**
```bash
mysql -u root -p -e "CREATE DATABASE user_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

**3. Configure `application.yml`**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_management
    username: your_db_username
    password: your_db_password

jwt:
  secret: your_jwt_secret_key
  expiration: 86400000  # 24 hours in ms

cloudinary:
  cloud-name: your_cloud_name
  api-key: your_api_key
  api-secret: your_api_secret
```

**4. Build and run**
```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login and receive JWT |

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get paginated user list |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Soft delete user |

### Groups
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/groups` | Get all groups |
| POST | `/api/groups` | Create group |
| PUT | `/api/groups/{id}` | Update group |
| DELETE | `/api/groups/{id}` | Delete group |
| POST | `/api/groups/{id}/users` | Add user to group |
| DELETE | `/api/groups/{id}/users/{userId}` | Remove user from group |

### Permissions
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/permissions` | Get permission list |
| POST | `/api/permissions` | Assign permission to group |
| DELETE | `/api/permissions/{id}` | Revoke permission |

### Profile
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/profile` | Get current user profile |
| PUT | `/api/profile` | Update profile |
| PUT | `/api/profile/password` | Change password |
| POST | `/api/profile/avatar` | Upload avatar |

> ⚠️ All endpoints (except `/api/auth/login`) require `Authorization: Bearer <token>` header.

## Environment Variables

| Variable | Description |
|----------|-------------|
| `DB_URL` | MySQL connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key for JWT signing |
| `JWT_EXPIRATION` | JWT expiration time (ms) |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name |
| `CLOUDINARY_API_KEY` | Cloudinary API key |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret |

---

---

# 🇻🇳 Tiếng Việt

## 📖 Mục lục

- [Giới thiệu](#giới-thiệu)
- [Chức năng](#chức-năng)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc thư mục](#cấu-trúc-thư-mục)
- [Cài đặt](#cài-đặt)
- [API Endpoints](#api-endpoints-1)
- [Biến môi trường](#biến-môi-trường)

## Giới thiệu

**User Management System** là REST API được xây dựng bằng **Spring Boot** để quản lý người dùng, nhóm người dùng, phân quyền và hồ sơ cá nhân.

Hệ thống áp dụng mô hình **RBAC (Role-Based Access Control)** cho phép quản trị viên cấu hình quyền View/Edit theo từng chức năng và gán vào các nhóm người dùng.

## Chức năng

### 🔑 Xác thực
- Đăng nhập bằng **JWT**
- Mã hóa mật khẩu bằng **BCrypt**

### 👤 Quản lý người dùng
- Xem danh sách người dùng có phân trang
- Xem chi tiết người dùng
- Thêm người dùng
- Cập nhật người dùng
- Xóa mềm (Soft Delete)

### 👥 Quản lý nhóm
- Tạo / Cập nhật / Xóa nhóm
- Thêm người dùng vào nhóm
- Xóa người dùng khỏi nhóm

### 🛡️ Quản lý quyền
- **RBAC** — Phân quyền theo vai trò
- Cấu hình quyền **View / Edit** cho từng chức năng
- Kiểm tra quyền bằng **Spring AOP (Aspect)**
- Custom annotation `@Permission` cho phép kiểm tra quyền tại controller

### 🪪 Hồ sơ cá nhân
- Xem thông tin cá nhân
- Cập nhật hồ sơ
- Đổi mật khẩu
- Upload ảnh đại diện bằng **Cloudinary**

### ⚙️ Tính năng khác
- **Soft Delete** — Xóa mềm, không xóa vật lý khỏi database
- **Pagination** — Tất cả API danh sách hỗ trợ phân trang
- **Projection** — Tối ưu truy vấn JPA, chỉ lấy các field cần thiết

## Công nghệ sử dụng

| Công nghệ | Mục đích |
|---|---|
| Java 21 | Ngôn ngữ chính |
| Spring Boot | Framework ứng dụng |
| Spring Security | Xác thực & phân quyền |
| Spring Data JPA | ORM kết nối database |
| MySQL | Cơ sở dữ liệu quan hệ |
| JWT | Xác thực stateless |
| BCrypt | Mã hóa mật khẩu |
| Lombok | Giảm boilerplate code |
| Cloudinary | Lưu trữ ảnh đại diện |

## Cấu trúc thư mục

```
src/main/java/com/example/
├── modules/
│   ├── auth/               # Đăng nhập, tạo JWT
│   ├── user/               # CRUD người dùng
│   ├── group/              # Quản lý nhóm
│   ├── permission/         # Cấu hình phân quyền
│   ├── profile/            # Hồ sơ & ảnh đại diện
│   └── feature/            # Định nghĩa tính năng
└── common/
    ├── entity/             # Base entity
    ├── dto/                # DTO dùng chung
    ├── enums/              # Các enum
    └── service/            # Service dùng chung
```

## Cài đặt

### Yêu cầu

- Java 21+
- Maven 3.8+
- MySQL 8.x
- Tài khoản Cloudinary

### Các bước cài đặt

**1. Clone repository**
```bash
git clone https://github.com/your-username/user-management-system.git
cd user-management-system
```

**2. Tạo database**
```bash
mysql -u root -p -e "CREATE DATABASE user_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

**3. Cấu hình `application.yml`**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_management
    username: your_db_username
    password: your_db_password

jwt:
  secret: your_jwt_secret_key
  expiration: 86400000  # 24 giờ (ms)

cloudinary:
  cloud-name: your_cloud_name
  api-key: your_api_key
  api-secret: your_api_secret
```

**4. Build và chạy**
```bash
mvn clean install
mvn spring-boot:run
```

API sẽ chạy tại `http://localhost:8080`.

## API Endpoints

### Xác thực
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/auth/login` | Đăng nhập, nhận JWT |

### Người dùng
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| GET | `/api/users` | Danh sách người dùng (phân trang) |
| GET | `/api/users/{id}` | Chi tiết người dùng |
| POST | `/api/users` | Tạo người dùng |
| PUT | `/api/users/{id}` | Cập nhật người dùng |
| DELETE | `/api/users/{id}` | Xóa mềm người dùng |

### Nhóm
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| GET | `/api/groups` | Danh sách nhóm |
| POST | `/api/groups` | Tạo nhóm |
| PUT | `/api/groups/{id}` | Cập nhật nhóm |
| DELETE | `/api/groups/{id}` | Xóa nhóm |
| POST | `/api/groups/{id}/users` | Thêm người dùng vào nhóm |
| DELETE | `/api/groups/{id}/users/{userId}` | Xóa người dùng khỏi nhóm |

### Quyền
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| GET | `/api/permissions` | Danh sách quyền |
| POST | `/api/permissions` | Gán quyền cho nhóm |
| DELETE | `/api/permissions/{id}` | Thu hồi quyền |

### Hồ sơ
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| GET | `/api/profile` | Xem hồ sơ cá nhân |
| PUT | `/api/profile` | Cập nhật hồ sơ |
| PUT | `/api/profile/password` | Đổi mật khẩu |
| POST | `/api/profile/avatar` | Upload ảnh đại diện |

> ⚠️ Tất cả endpoint (ngoại trừ `/api/auth/login`) yêu cầu header `Authorization: Bearer <token>`.

## Biến môi trường

| Biến | Mô tả |
|------|-------|
| `DB_URL` | URL kết nối MySQL |
| `DB_USERNAME` | Tên đăng nhập database |
| `DB_PASSWORD` | Mật khẩu database |
| `JWT_SECRET` | Khóa bí mật ký JWT |
| `JWT_EXPIRATION` | Thời hạn JWT (ms) |
| `CLOUDINARY_CLOUD_NAME` | Tên cloud Cloudinary |
| `CLOUDINARY_API_KEY` | API key Cloudinary |
| `CLOUDINARY_API_SECRET` | API secret Cloudinary |

---

## 👨‍💻 Author

<div align="center">

**Trương Quang Quốc**

*Intern Backend Developer*

**IVS Co., Ltd.**

</div>
