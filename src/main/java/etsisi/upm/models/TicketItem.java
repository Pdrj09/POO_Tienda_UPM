package etsisi.upm.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "ticket_items")
public class TicketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sellable_db_id", referencedColumnName = "dbId")
    private Sellable sellable;

    @Column(name = "quantity")
    private int quantity;

    public TicketItem() {}

    public TicketItem(Sellable sellable, int quantity) {
        this.sellable = sellable;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Sellable getSellable() {
        return sellable;
    }

    public void setSellable(Sellable sellable) {
        this.sellable = sellable;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketItem that = (TicketItem) o;
        return Objects.equals(sellable, that.sellable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellable);
    }
}
