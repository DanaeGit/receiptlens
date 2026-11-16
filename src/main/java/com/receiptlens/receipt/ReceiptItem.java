package com.receiptlens.receipt;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "receipt_items")
public class ReceiptItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;

    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "line_type", nullable = false, length = 20)
    private LineType lineType = LineType.ITEM;

    @Column(name = "name_original", nullable = false, length = 200)
    private String nameOriginal;

    @Column(name = "name_translated", length = 200)
    private String nameTranslated;

    @Column(name = "quantity", nullable = false, precision = 10, scale = 3)
    private BigDecimal quantity = BigDecimal.ONE;

    @Column(name = "unit", nullable = false, length = 10)
    private String unit = "ea";

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal;

    @Column(name = "confidence", precision = 4, scale = 3)
    private BigDecimal confidence;

    public UUID getId() {
        return id;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public LineType getLineType() {
        return lineType;
    }

    public String getNameOriginal() {
        return nameOriginal;
    }

    public String getNameTranslated() {
        return nameTranslated;
    }

    public void setNameTranslated(String nameTranslated) {
        this.nameTranslated = nameTranslated;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public BigDecimal getConfidence() {
        return confidence;
    }

    public void setConfidence(BigDecimal confidence) {
        this.confidence = confidence;
    }

    protected ReceiptItem() {
    }

    public ReceiptItem(Integer lineNumber, LineType lineType, String nameOriginal, BigDecimal quantity, String unit, BigDecimal unitPrice, BigDecimal lineTotal) {
        this.lineNumber = lineNumber;
        this.lineType = lineType;
        this.nameOriginal = nameOriginal;
        this.quantity = quantity;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }


}