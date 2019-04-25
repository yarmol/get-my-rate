package me.jarad.rates.controller;

import lombok.extern.slf4j.Slf4j;
import me.jarad.rates.handler.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Common controller to decline unknown requests
 */
@RestController
@Slf4j
public abstract class CommonRequestParentController {

    @RequestMapping(method = {GET,POST,PUT,PATCH},path = "/**")
    public void performErrorRequest(HttpServletRequest request, HttpServletResponse response) throws ApiException {
        throw new ApiException("Wrong request");
    }

}
