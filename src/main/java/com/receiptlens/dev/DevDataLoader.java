package com.receiptlens.dev;

import com.receiptlens.receipt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component          // 交给 Spring 管理，启动时自动创建
@Profile("dev")     // 只在 dev 环境运行，生产环境不会执行
public class DevDataLoader implements CommandLineRunner {   // 程序启动完成后自动执行 run()

    private static final Logger log = LoggerFactory.getLogger(DevDataLoader.class);
    private static final UUID DEMO_USER = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final ReceiptRepository receiptRepository;

    // 构造器注入：Spring 自动把 ReceiptRepository 传进来
    public DevDataLoader(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (receiptRepository.count() > 0) {
            log.info("Demo data already exists, skipping");
            return;
        }
        receiptRepository.save(paknsave());
        receiptRepository.save(woolworths());
        receiptRepository.save(asianGrocery());
        log.info("Inserted {} demo receipts", receiptRepository.count());
    }

    private Receipt paknsave() {
        Receipt r = new Receipt("demo-hash-paknsave", DEMO_USER);
        r.setStatus(ReceiptStatus.COMPLETED);
        r.setStoreChain("PAK_N_SAVE");
        r.setStoreName("PAK'nSAVE Albany");
        r.setReceiptNumber("DEMO-0001");
        r.setPurchasedAt(OffsetDateTime.parse("2026-11-08T10:15:00+13:00"));
        r.setSourceLanguage("en");
        r.setPaymentMethod("EFTPOS");
        r.addItem(item(1, LineType.ITEM, "PAMS MILK 2L", "1", "ea", "4.39", "4.39"));
        r.addItem(item(2, LineType.ITEM, "BANANAS", "0.856", "kg", "3.99", "3.42"));
        r.addItem(item(3, LineType.ITEM, "PAMS WHITE TOAST 600G", "1", "ea", "1.80", "1.80"));
        r.setSubtotal(new BigDecimal("9.61"));
        r.setTotalAmount(new BigDecimal("9.61"));
        r.setGstAmount(new BigDecimal("1.25"));
        return r;
    }

    private Receipt woolworths() {
        Receipt r = new Receipt("demo-hash-woolworths", DEMO_USER);
        r.setStatus(ReceiptStatus.COMPLETED);
        r.setStoreChain("WOOLWORTHS");
        r.setStoreName("Woolworths Albany");
        r.setReceiptNumber("DEMO-0002");
        r.setPurchasedAt(OffsetDateTime.parse("2026-11-10T18:40:00+13:00"));
        r.setSourceLanguage("en");
        r.setPaymentMethod("CREDIT_CARD");
        r.addItem(item(1, LineType.ITEM, "WW FREE RANGE EGGS 12PK", "1", "ea", "8.50", "8.50"));
        r.addItem(item(2, LineType.ITEM, "ANCHOR BUTTER 500G", "1", "ea", "7.50", "7.50"));
        r.addItem(item(3, LineType.DISCOUNT, "EVERYDAY REWARDS SAVING", "1", "ea", null, "-1.50"));
        r.setSubtotal(new BigDecimal("16.00"));
        r.setDiscountTotal(new BigDecimal("1.50"));
        r.setTotalAmount(new BigDecimal("14.50"));
        r.setGstAmount(new BigDecimal("1.89"));
        return r;
    }

    private Receipt asianGrocery() {
        Receipt r = new Receipt("demo-hash-asian", DEMO_USER);
        r.setStatus(ReceiptStatus.NEEDS_REVIEW);
        r.setStoreChain("ASIAN_GROCERY");
        r.setStoreName("Demo Asian Supermarket");
        r.setReceiptNumber("DEMO-0003");
        r.setPurchasedAt(OffsetDateTime.parse("2026-11-12T15:05:00+13:00"));
        r.setSourceLanguage("zh");
        r.setPaymentMethod("EFTPOS");
        r.addItem(item(1, LineType.ITEM, "老干妈辣椒酱 280G", "1", "ea", "4.99", "4.99"));
        r.addItem(item(2, LineType.ITEM, "白菜", "1.200", "kg", "2.99", "3.59"));
        r.addItem(item(3, LineType.ITEM, "李锦记生抽 500ML", "1", "ea", "5.49", "5.49"));
        r.setSubtotal(new BigDecimal("14.07"));
        r.setTotalAmount(new BigDecimal("14.07"));
        r.setGstAmount(new BigDecimal("1.84"));
        return r;
    }

    // 小工具：用字符串建 BigDecimal，避免 double 的精度问题
    private static ReceiptItem item(int line, LineType type, String name, String qty,
                                    String unit, String unitPrice, String lineTotal) {
        return new ReceiptItem(line, type, name, new BigDecimal(qty), unit,
                unitPrice == null ? null : new BigDecimal(unitPrice),
                new BigDecimal(lineTotal));
    }
}