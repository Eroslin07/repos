CREATE TABLE IF NOT EXISTS "uctp_car_info" (
                                               "vin" varchar,
                                               "brand" varchar,
                                               "year" varchar,
                                               "style" varchar,
                                               "engine_num" varchar,
                                               "vehicle_receipt_amount" varchar,
                                               "remarks" varchar,
                                               "revision" int,
                                               "creator" varchar,
                                               "create_time" datetime NOT NULL,
                                               "updater" varchar,
                                               "updated_time" datetime NOT NULL,
                                               "sales_status" int,
                                               "check_status" int,
                                               "deleted" bit,
                                               "id" varchar NOT NULL,
                                               "car_detail_id" varchar,
                                               "business_id" varchar,
                                               "tenant_id" varchar,
                                               PRIMARY KEY ("id")
    ) COMMENT '车辆主表';