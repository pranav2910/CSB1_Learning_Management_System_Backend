package com.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityResponse {
    private Long newUsersLast30Days;
    private Long activeUsersLast7Days;
    private Map<String, Long> activityByCountry;
    private Map<String, Long> userGrowthByMonth;
}