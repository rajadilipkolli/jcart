/**
 *
 */
package com.sivalabs.jcart.entities;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Siva
 *
 */
@Entity
@Table(name = "orders")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	@Column(unique = true, nullable = false)
	private String orderNumber;

	@OneToMany(cascade = ALL, mappedBy = "order")
	private Set<OrderItem> items;

	@ManyToOne(cascade = MERGE)
	@JoinColumn(name = "cust_id")
	private Customer customer;

	@OneToOne(cascade = PERSIST)
	@JoinColumn(name = "delivery_addr_id")
	private Address deliveryAddress;

	@OneToOne(cascade = PERSIST)
	@JoinColumn(name = "billing_addr_id")
	private Address billingAddress;

	@OneToOne(cascade = PERSIST)
	@JoinColumn(name = "payment_id")
	private Payment payment;

	@Enumerated(STRING)
	private OrderStatus status;

	@Temporal(TIMESTAMP)
	@Column(name = "created_on")
	private Date createdOn;

	public Order() {
		this.items = new HashSet<>();
		this.status = OrderStatus.NEW;
		this.createdOn = new Date();
	}

	public BigDecimal getTotalAmount() {
		BigDecimal amount = new BigDecimal("0.0");
		for (OrderItem item : items) {
			amount = amount.add(item.getSubTotal());
		}
		return amount;
	}

}
