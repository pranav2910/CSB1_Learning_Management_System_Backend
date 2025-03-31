package com.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueAnalyticsResponse {
    private BigDecimal totalRevenue;
    private BigDecimal monthlyRevenue;
    private BigDecimal yearlyRevenue;
    private Map<LocalDate, BigDecimal> dailyRevenue;
    private Map<String, BigDecimal> revenueByCourseCategory;
}