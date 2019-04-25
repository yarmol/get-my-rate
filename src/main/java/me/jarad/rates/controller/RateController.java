package me.jarad.rates.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import me.jarad.rates.handler.WrongRequestException;
import me.jarad.rates.model.RateResponse;
import me.jarad.rates.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
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
@Api(value = "Rate controller")
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
    @ApiOperation(value = "Get curent rate", nickname = "getCurrent", notes = "", response = RateResponse.class, tags={ "rate-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = RateResponse.class),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @GetMapping(path = "/api/current", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @ApiOperation(value = "Get curent rate history", nickname = "getHistory", notes = "", response = List.class, tags={ "rate-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = RateResponse.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 422, message = "Wrong parameters")
    })
    @GetMapping(path ="/api/history", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RateResponse>> getHistory(
                             @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                             @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws WrongRequestException {

        if (to.isBefore(from)) {
            throw new WrongRequestException("Wrong dates period");
        }


        List<RateResponse> rateHistory = rateService.getRateHistory(from, to);
        return ResponseEntity.ok(rateHistory);
    }

}
