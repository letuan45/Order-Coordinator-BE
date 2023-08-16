package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/analyze")
public class AnalyzeController {
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/get-card-data")
    public ResponseEntity<List<Map<String, Integer>>> getCardData() {
        return ResponseEntity.ok(analyticsService.getCardData());
    }

    @GetMapping("/get-chart-data")
    public ResponseEntity<List<Map<String, List<Integer>>>> getChartData() {
        return ResponseEntity.ok(analyticsService.getChartsData());
    }
}
