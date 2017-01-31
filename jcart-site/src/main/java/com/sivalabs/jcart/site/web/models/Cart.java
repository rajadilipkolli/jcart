/**
 * 
 */
package com.sivalabs.jcart.site.web.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.sivalabs.jcart.entities.Address;
import com.sivalabs.jcart.entities.Customer;
import com.sivalabs.jcart.entities.Payment;
import com.sivalabs.jcart.entities.Product;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Siva
 *
 */
@Getter
@Setter
public class Cart implements Serializable
{
    private static final long serialVersionUID = 1L;

    private List<LineItem> items;
    private Customer customer;
    private Address deliveryAddress;
    private Payment payment;

    public Cart()
    {
        items = new ArrayList<>();
        customer = new Customer();
        deliveryAddress = new Address();
        payment = new Payment();
    }

    public void addItem(Product product)
    {
        for (LineItem lineItem : items)
        {
            if (lineItem.getProduct().getSku().equals(product.getSku()))
            {
                lineItem.setQuantity(lineItem.getQuantity() + 1);
                return;
            }
        }
        LineItem item = new LineItem(product, 1);
        this.items.add(item);
    }

    public void updateItemQuantity(Product product, int quantity)
    {
        for (LineItem lineItem : items)
        {
            if (lineItem.getProduct().getSku().equals(product.getSku()))
            {
                lineItem.setQuantity(quantity);
            }
        }

    }

    public void removeItem(String sku)
    {
        items.removeIf(lineItem -> lineItem.getProduct().getSku().equals(sku));
    }

    public void clearItems()
    {
        items = new ArrayList<>();
    }

    public int getItemCount()
    {
        return items.stream().mapToInt(LineItem::getQuantity).sum();
    }

    public BigDecimal getTotalAmount()
    {
        BigDecimal amount = new BigDecimal("0.0");
        for (LineItem lineItem : items)
        {
            amount = amount.add(lineItem.getSubTotal());
        }
        return amount;
    }

}
