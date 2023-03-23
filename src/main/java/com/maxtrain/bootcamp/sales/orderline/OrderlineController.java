package com.maxtrain.bootcamp.sales.orderline;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/orderlines")
public class OrderlineController { // start of class

	@Autowired
	private OrderlineRepository olineRepo;
	
} // end of class
