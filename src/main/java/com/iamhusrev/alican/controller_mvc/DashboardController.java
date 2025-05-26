package com.iamhusrev.alican.controller_mvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // TODO: Replace with actual service calls
        model.addAttribute("totalSales", new BigDecimal("150000.00"));
        model.addAttribute("activeCustomers", 45);
        model.addAttribute("totalProducts", 120);
        model.addAttribute("pendingOrders", 23);

        // Sample recent orders
        List<OrderDTO> recentOrders = Arrays.asList(
            new OrderDTO("ORD001", "Ahmet Yılmaz", LocalDate.now(), new BigDecimal("1250.00"), "COMPLETED"),
            new OrderDTO("ORD002", "Mehmet Demir", LocalDate.now().minusDays(1), new BigDecimal("850.00"), "PENDING"),
            new OrderDTO("ORD003", "Ayşe Kaya", LocalDate.now().minusDays(2), new BigDecimal("2100.00"), "COMPLETED")
        );
        model.addAttribute("recentOrders", recentOrders);

        // Sample low stock products
        List<ProductDTO> lowStockProducts = Arrays.asList(
            new ProductDTO("PRD001", "Laptop", 5, 10),
            new ProductDTO("PRD002", "Monitor", 3, 8),
            new ProductDTO("PRD003", "Klavye", 2, 15)
        );
        model.addAttribute("lowStockProducts", lowStockProducts);

        return "dashboard";
    }

    // DTO classes for sample data
    private static class OrderDTO {
        private String orderNumber;
        private String customerName;
        private LocalDate orderDate;
        private BigDecimal total;
        private String status;

        public OrderDTO(String orderNumber, String customerName, LocalDate orderDate, BigDecimal total, String status) {
            this.orderNumber = orderNumber;
            this.customerName = customerName;
            this.orderDate = orderDate;
            this.total = total;
            this.status = status;
        }

        // Getters
        public String getOrderNumber() {
            return orderNumber;
        }

        public String getCustomerName() {
            return customerName;
        }

        public LocalDate getOrderDate() {
            return orderDate;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public String getStatus() {
            return status;
        }
    }

    private static class ProductDTO {
        private String code;
        private String name;
        private int currentStock;
        private int minimumStock;

        public ProductDTO(String code, String name, int currentStock, int minimumStock) {
            this.code = code;
            this.name = name;
            this.currentStock = currentStock;
            this.minimumStock = minimumStock;
        }

        // Getters
        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public int getCurrentStock() {
            return currentStock;
        }

        public int getMinimumStock() {
            return minimumStock;
        }
    }
} 