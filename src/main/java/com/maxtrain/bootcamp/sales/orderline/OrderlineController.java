package com.maxtrain.bootcamp.sales.orderline;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.bootcamp.sales.item.Item;
import com.maxtrain.bootcamp.sales.item.ItemRepository;

import com.maxtrain.bootcamp.sales.order.Order;
import com.maxtrain.bootcamp.sales.order.OrderRepository;

import jakarta.persistence.*;

@CrossOrigin
@RestController
@RequestMapping("/api/orderlines")
public class OrderlineController { // start of class

	@Autowired
	private OrderlineRepository olineRepo;
	@Autowired
	private OrderRepository ordRepo;	
	@Autowired
	private ItemRepository itemRepo;
	
	
	private boolean recalculateOrderTotal (int orderId) {
		// read the order to be recalculated
		Optional<Order> anOrder = ordRepo.findById(orderId);
		// if not found, return false
		if(anOrder.isEmpty()) {
			return false;
		}
		// get the order
		Order order = anOrder.get();
		// get all orderlines attache to the order
		Iterable<Orderline> orderlines = olineRepo.findByOrderId(orderId);
		double total = 0;
		for(Orderline ol : orderlines) {
			// gregsss fix
			if(ol.getItem().getName() == null) {
				Item item = itemRepo.findById(ol.getItem().getId()).get();
				ol.setItem(item);
			}
			//greggss fix

			// for each orderlin, mulitply the qunaitty times the price
			// and add it to the total
			total += ol.getQuantity()*ol.getItem().getPrice();
		}
		//update the total in the order
		order.setTotal(total);
		ordRepo.save(order);
		
		return true;
		
	}
	
	
	
	@GetMapping
	public ResponseEntity<Iterable<Orderline>> getAllOrderlines(){
		Iterable<Orderline> orderlines = olineRepo.findAll();
		return new ResponseEntity<Iterable<Orderline>>(orderlines, HttpStatus.OK);		
	}
	
	
	@GetMapping("{id}")
	public ResponseEntity<Orderline> getOrderline(@PathVariable int id){
		Optional<Orderline> orderline = olineRepo.findById(id);
		if(orderline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Orderline>(orderline.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Orderline> postOrderline(@RequestBody Orderline orderline){
		Orderline newOrderline = olineRepo.save(orderline);
		Optional<Order> order = ordRepo.findById(orderline.getOrder().getId());          // added to make recalculate call
		if(!order.isEmpty()) {															 // added to make recalculate call
			boolean success = recalculateOrderTotal(order.get().getId());                 // added to make recalculate call
			if(!success) {                                                                  // added to make recalculate call
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);             // added to make recalculate call
			}                                                                             // added to make recalculate call
		}	
		return new ResponseEntity<Orderline>(newOrderline, HttpStatus.CREATED);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("{id}")
	public ResponseEntity<Orderline> putOrderline(@PathVariable int id, @RequestBody Orderline orderline){
		if(orderline.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Orderline updatedOrderline = olineRepo.save(orderline);
		///>>>
		olineRepo.save(orderline);
		Optional<Order> order = ordRepo.findById(orderline.getOrder().getId());
		if(!order.isEmpty()) {
			boolean success = recalculateOrderTotal(order.get().getId());
			if(!success) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		}
		
		//???>>>
		
		return new ResponseEntity<Orderline>(updatedOrderline, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteOrderline(@PathVariable int id) {
		Optional<Orderline> orderline = olineRepo.findById(id);
		if(orderline.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		olineRepo.delete(orderline.get());
		/// addded
		Optional<Order> order = ordRepo.findById(orderline.get().getOrder().getId());
		if(!order.isEmpty()) {
			boolean success = recalculateOrderTotal(order.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		/// added
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	
} // end of class
