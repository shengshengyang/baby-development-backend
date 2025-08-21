-- Add roles table and user_roles junction table
-- Author: System
-- Date: 2025-08-21

-- Create roles table
CREATE TABLE roles (
    id BINARY(16) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uc_roles_name UNIQUE (name)
);

-- Create user_roles junction table
CREATE TABLE user_roles (
    user_id BINARY(16) NOT NULL,
    role_id BINARY(16) NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Insert default roles
SET @role_admin_id = UNHEX(REPLACE(UUID(),'-',''));
SET @role_user_id = UNHEX(REPLACE(UUID(),'-',''));

INSERT INTO roles (id, name, description) VALUES
(@role_admin_id, 'ROLE_ADMIN', '系統管理員角色，擁有所有權限'),
(@role_user_id, 'ROLE_USER', '一般用戶角色，擁有基本功能權限');

-- Assign default role to existing users (if any)
-- This will give all existing users the USER role
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, @role_user_id
FROM users u
WHERE NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id
);
