-- FIX: Hibernate maps the entity field "subscriptionStatus" to column "subscription_status"
-- but V1 created it as "status". This migration aligns the DB column with the entity.
ALTER TABLE subscriptions RENAME COLUMN status TO subscription_status;
