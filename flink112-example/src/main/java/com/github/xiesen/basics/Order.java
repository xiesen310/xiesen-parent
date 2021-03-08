package com.github.xiesen.basics;

/**
 * Simple POJO.
 *
 * @author 谢森
 * @since 2021/3/4
 */
public class Order {
    public Long user;
    public String product;
    public int amount;

    public Order() {
    }

    public Order(Long user, String product, int amount) {
        this.user = user;
        this.product = product;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "user=" + user +
                ", product='" + product + '\'' +
                ", amount=" + amount +
                '}';
    }
}
