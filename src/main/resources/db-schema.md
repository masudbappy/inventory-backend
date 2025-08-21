erDiagram

    WAREHOUSE ||--o{ PRODUCT : contains
    CATEGORY ||--o{ PRODUCT : categorizes
    TYPE ||--o{ PRODUCT : isOf
    SUPPLIER ||--o{ PRODUCT : supplies

    SUPPLIER ||--o{ PURCHASE : provides
    USER ||--o{ PURCHASE : approvedBy

    CUSTOMER ||--o{ SALE : buys
    USER ||--o{ SALE : soldBy
    SALE ||--o{ PAYMENTLOG : recordedIn

    USER ||--o{ EXPENSE : logs

    SALE ||--o{ SALESORDER : includes
    PRODUCT ||--o{ SALESORDER : containedIn

    PURCHASE ||--o{ PURCHASEDETAIL : details
    PRODUCT ||--o{ PURCHASEDETAIL : ordered

    PRODUCT {
        long productId PK
        string name
        string productCode
        string type
        double stock
        double buyingPrice
        double sellingPrice
        string unit
        double lowStockThreshold
        long wareHouseId FK
        long categoryId FK
        long supplierId FK
        long typeId FK
        timestamp createdAt
        timestamp updatedAt
    }

    CATEGORY {
        long categoryId PK
        string Name
        timestamp createdAt
        timestamp updatedAt
    }

    TYPE {
        long typeId PK
        string name
        timestamp createdAt
        timestamp updatedAt
    }

    WAREHOUSE {
        long wareHouseId PK
        string wareHouseName
        string location
        timestamp createdAt
        timestamp updatedAt
    }

    SUPPLIER {
        long SupplierId PK
        string name
        string ContactNumber
        string Address
        timestamp CreatedAt
        timestamp updatedAt
    }

    PURCHASE {
        long purchageId PK
        long supplierId FK
        timestamp Date
        string description
        double purchageAmount
        double laborCost
        double transportCost
        double paidAmount
        image reciptImage
        timestamp createdAt
        timestamp updatedAt
    }

    PURCHASEDETAIL {
        long purchageDetailId PK
        long purchageId FK
        long productId FK
        double quantity
        double unitPrice
        timestamp createdAt
        timestamp updatedAt
    }

    CUSTOMER {
        long customerId PK
        string name
        string ContactNumber
        string address
        ENUM paymentStatus
        double dueAmount
        timestamp createdAt
        timestamp updatedAt
    }

    SALE {
        long saleId PK
        long customerId FK
        long userId FK
        date date
        double totalPrice
        double paidAmount
        double laborCost
        double discountAmount
        timestamp createdAt
        timestamp updatedAt
    }

    SALESORDER {
        long salesOrderId PK
        long productId FK
        long saleId FK
        double quantity
        double rate
        timestamp createdAt
        timestamp updatedAt
    }

    PAYMENTLOG {
        long paymentLogId PK
        long saleId FK
        long userId FK
        double paymentAmount
        date date
        timestamp createdAt
        timestamp updatedAt
    }

    USER {
        long userId PK
        string name
        ENUM role
        string Username
        string passwordHash
        timestamp createdAt
        timestamp updatedAt
    }

    EXPENSE {
        long expenseId PK
        long userId FK
        string category
        double amount
        timestamp date
        timestamp createdAt
        timestamp updatedAt
    }