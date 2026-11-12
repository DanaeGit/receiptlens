-- V1: 小票和商品明细（按 Pak'nSave、Woolworths 和华人超市的小票格式设计）

CREATE TABLE receipts (
    id               UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id          UUID           NOT NULL,
    file_hash        VARCHAR(64)    NOT NULL,
    blob_url         TEXT,
    status           VARCHAR(20)    NOT NULL DEFAULT 'UPLOADED',

    -- 门店
    store_chain      VARCHAR(50),           -- PAK_N_SAVE / WOOLWORTHS / NEW_WORLD / ASIAN_GROCERY / OTHER
    store_name       VARCHAR(100),          -- 小票上的原始店名，如 "PAK'nSAVE Albany"
    store_gst_number VARCHAR(20),

    -- 交易
    receipt_number   VARCHAR(50),           -- 小票号 / 交易号
    purchased_at     TIMESTAMPTZ,           -- 日期 + 时间
    source_language  VARCHAR(10),           -- en / zh / mixed

    -- 金额（新西兰标价含 GST）
    currency         CHAR(3)        NOT NULL DEFAULT 'NZD',
    subtotal         NUMERIC(10, 2),
    discount_total   NUMERIC(10, 2) NOT NULL DEFAULT 0,
    total_amount     NUMERIC(10, 2),
    gst_amount       NUMERIC(10, 2),
    payment_method   VARCHAR(20),           -- EFTPOS / CREDIT_CARD / CASH / OTHER（不存卡号）

    error_message    TEXT,
    created_at       TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ    NOT NULL DEFAULT now(),

    CONSTRAINT ck_receipts_status CHECK (status IN
        ('UPLOADED', 'PROCESSING', 'NEEDS_REVIEW', 'COMPLETED', 'FAILED')),
    -- 去重 1：同一张图片不重复上传
    CONSTRAINT uq_receipts_user_file UNIQUE (user_id, file_hash)
);

-- 去重 2：同一张小票拍了两次（识别出小票号之后才生效）
CREATE UNIQUE INDEX uq_receipts_business_key
    ON receipts (user_id, store_gst_number, receipt_number, purchased_at)
    WHERE receipt_number IS NOT NULL;

CREATE INDEX idx_receipts_user_purchased ON receipts (user_id, purchased_at);

CREATE TABLE receipt_items (
    id               UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    receipt_id       UUID           NOT NULL REFERENCES receipts (id) ON DELETE CASCADE,
    line_number      INT            NOT NULL,          -- 在小票上的顺序
    line_type        VARCHAR(20)    NOT NULL DEFAULT 'ITEM',
    name_original    VARCHAR(200)   NOT NULL,          -- 小票原文，如 "PAMS MILK 2L"、"老干妈辣椒酱"
    name_translated  VARCHAR(200),
    quantity         NUMERIC(10, 3) NOT NULL DEFAULT 1,
    unit             VARCHAR(10)    NOT NULL DEFAULT 'ea',   -- ea / kg
    unit_price       NUMERIC(10, 2),
    line_total       NUMERIC(10, 2) NOT NULL,          -- 折扣、退货可以是负数
    confidence       NUMERIC(4, 3),                    -- AI 识别置信度 0–1

    CONSTRAINT ck_items_line_type CHECK (line_type IN
        ('ITEM', 'DISCOUNT', 'BAG', 'DEPOSIT', 'RETURN', 'OTHER')),
    CONSTRAINT ck_items_unit CHECK (unit IN ('ea', 'kg')),
    CONSTRAINT uq_items_receipt_line UNIQUE (receipt_id, line_number)
);