package me.jarad.rates.controller;

import me.jarad.rates.model.RateResponse;
import me.jarad.rates.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Main rates info controller
 * Created by Vitalii Yarmolenko (yarmol@gmail.com) on 23.04.19.
 */
@RestController
@RequestMapping
public class RateController extends CommonRequestParentController {


    private RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }


    /**
     * Getting current rate
     * @return
     */
    @GetMapping("/api/current")
    public ResponseEntity<RateResponse> getCurrent() {
        Optional<RateResponse> currentRate = rateService.getCurrentRate();
        if (currentRate.isPresent()) {
            return ResponseEntity.ok(currentRate.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Getting rates history
     */
    @GetMapping("/api/history")
    public ResponseEntity<List<RateResponse>> getHistory(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                             @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<RateResponse> rateHistory = rateService.getRateHistory(from, to);
        return ResponseEntity.ok(rateHistory);
    }

}
