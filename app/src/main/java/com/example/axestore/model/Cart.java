package com.example.axestore.model;


public class Cart {
    private Integer idProduct;
    private Integer countItem;
    private String note;

    public Cart (){
    }

    public Cart (Integer idProduct){
        this.idProduct = idProduct;
    }

    public Integer getCountItem() {
        return countItem;
    }

    public void setCountItem(Integer countItem) {
        this.countItem = countItem;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "idProduct=" + idProduct +
                ", countItem=" + countItem +
                ", note='" + note + '\'' +
                '}';
    }
}
