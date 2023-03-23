package com.maxtrain.bootcamp.sales.orderline;
import jakarta.persistence.*;

@Entity
@Table(name="Orderlines")
public class Orderline {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	public int quantity = 1;
	
	// FK's
	
	//@OneToMany(optional=false)
	@JoinColumn(name="orderId", columnDefinition="int")
	private Order order;
	
	@JoinColumn(name="itemId", columnDefinition="int")
	private Item item;

}
