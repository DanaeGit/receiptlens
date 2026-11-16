package com.receiptlens.receipt;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Entity
@Table(name = "receipts")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "file_hash", nullable = false, length = 64)
    private String fileHash;

    @Column(name = "blob_url", length = Integer.MAX_VALUE)
    private String blobUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReceiptStatus status = ReceiptStatus.UPLOADED;

    @Column(name = "store_chain", length = 50)
    private String storeChain;

    @Column(name = "store_name", length = 100)
    private String storeName;

    @Column(name = "store_gst_number", length = 20)
    private String storeGstNumber;

    @Column(name = "receipt_number", length = 50)
    private String receiptNumber;

    @Column(name = "purchased_at")
    private OffsetDateTime purchasedAt;

    @Column(name = "source_language", length = 10)
    private String sourceLanguage;

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.CHAR)
    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "NZD";

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "discount_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountTotal = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "gst_amount", precision = 10, scale = 2)
    private BigDecimal gstAmount;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @Column(name = "error_message", length = Integer.MAX_VALUE)
    private String errorMessage;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("lineNumber")
    private List<ReceiptItem> items = new ArrayList<>();

    public UUID getId() {
        return id;
    }


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getBlobUrl() {
        return blobUrl;
    }

    public void setBlobUrl(String blobUrl) {
        this.blobUrl = blobUrl;
    }

    public ReceiptStatus getStatus() {
        return status;
    }

    public void setStatus(ReceiptStatus status) {
        this.status = status;
    }

    public String getStoreChain() {
        return storeChain;
    }

    public void setStoreChain(String storeChain) {
        this.storeChain = storeChain;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreGstNumber() {
        return storeGstNumber;
    }

    public void setStoreGstNumber(String storeGstNumber) {
        this.storeGstNumber = storeGstNumber;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public OffsetDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(OffsetDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(BigDecimal gstAmount) {
        this.gstAmount = gstAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void addItem(ReceiptItem item) {
        items.add(item);
        item.setReceipt(this);
    }
    public List<ReceiptItem> getItems() {
        return items;
    }
    protected Receipt() {
    }
    public Receipt(String fileHash, UUID userId) {
        this.fileHash = fileHash;
        this.userId = userId;
    }
}