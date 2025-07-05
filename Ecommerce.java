import java.util.*;

abstract class Product {
    String name;
    double price;
    int quantity;

    Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    boolean isShippable() {
        return this instanceof Shippable;
    }

    boolean isExpirable() {
        return this instanceof Expirable;
    }
}

interface Expirable {}

interface Shippable {
    double getWeight();
}

class Cheese extends Product implements Expirable, Shippable {
    double weight;

    Cheese(String name, double price, int quantity, double weight) {
        super(name, price, quantity);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}

class Biscuits extends Product implements Expirable, Shippable {
    double weight;

    Biscuits(String name, double price, int quantity, double weight) {
        super(name, price, quantity);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}

class TV extends Product implements Shippable {
    double weight;

    TV(String name, double price, int quantity, double weight) {
        super(name, price, quantity);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}

class Mobile extends Product {
    Mobile(String name, double price, int quantity) {
        super(name, price, quantity);
    }
}

class ScratchCard extends Product {
    ScratchCard(String name, double price, int quantity) {
        super(name, price, quantity);
    }
}

class CartItem {
    Product product;
    int quantity;

    CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    double getTotalPrice() {
        return product.price * quantity;
    }

    double getShippingWeight() {
        if (product instanceof Shippable) {
            return ((Shippable) product).getWeight() * quantity;
        }
        return 0;
    }
}

class Cart {
    List<CartItem> items = new ArrayList<>();

    void add(Product product, int quantity) {
        if (quantity > product.quantity) {
            System.out.println("Error: Not enough stock for " + product.name);
            return;
        }
        items.add(new CartItem(product, quantity));
        product.quantity -= quantity;
    }

    boolean isEmpty() {
        return items.isEmpty();
    }

    double getSubtotal() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    double getTotalWeight() {
        return items.stream().mapToDouble(CartItem::getShippingWeight).sum();
    }

    double getShippingCost() {
        return getTotalWeight() * 10;
    }

    void clear() {
        items.clear();
    }

    List<CartItem> getItems() {
        return items;
    }
}

class Customer {
    String name;
    double balance;

    Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    boolean canAfford(double amount) {
        return balance >= amount;
    }

    void deduct(double amount) {
        balance -= amount;
    }
}

class ECommerceSystem {
    static void checkout(Customer customer, Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("Error: Cart is empty.");
            return;
        }

        double subtotal = cart.getSubtotal();
        double shipping = cart.getShippingCost();
        double total = subtotal + shipping;

        if (!customer.canAfford(total)) {
            System.out.println("Error: Insufficient balance.");
            return;
        }

        System.out.println("** Shipment notice **");
        for (CartItem item : cart.getItems()) {
            if (item.product instanceof Shippable) {
                System.out.println(item.quantity + "x " + item.product.name + " " +
                        ((Shippable) item.product).getWeight() * 1000 + "g");
            }
        }
        System.out.printf("Total package weight %.1fkg\n", cart.getTotalWeight());

        System.out.println("** Checkout receipt **");
        for (CartItem item : cart.getItems()) {
            System.out.println(item.quantity + "x " + item.product.name + " " + (int)item.getTotalPrice());
        }
        System.out.println("----------------------");
        System.out.println("Subtotal " + (int)subtotal);
        System.out.println("Shipping " + (int)shipping);
        System.out.println("Amount " + (int)total);
        customer.deduct(total);
        System.out.println("Remaining Balance: " + (int)customer.balance);
        System.out.println("END.");

        cart.clear();
    }
}

public class Main {
    public static void main(String[] args) {
        Cheese cheese = new Cheese("Cheese", 100, 10, 0.2);
        Biscuits biscuits = new Biscuits("Biscuits", 150, 5, 0.7);
        TV tv = new TV("TV", 300, 3, 5);
        Mobile mobile = new Mobile("Mobile", 500, 4);
        ScratchCard scratchCard = new ScratchCard("ScratchCard", 50, 10);

        Customer customer = new Customer("Malak", 1000);

        Cart cart = new Cart();
        cart.add(cheese, 2);
        cart.add(biscuits, 1);
        cart.add(scratchCard, 1);

        ECommerceSystem.checkout(customer, cart);
    }
                            }
