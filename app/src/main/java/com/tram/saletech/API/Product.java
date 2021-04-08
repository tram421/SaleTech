package com.tram.saletech.API;

public class Product {

        private String id;
        private String name;
        private String image;
        private String price;
        private String sale;
        private String description;
        private String idCategory;
        private String rate;
        private String similar;
        private String isFeature;


        public Product() {
        }

        public Product(String id, String name, String image, String price, String sale, String description, String idCategory, String rate, String similar, String isFeature) {
            super();
            this.id = id;
            this.name = name;
            this.image = image;
            this.price = price;
            this.sale = sale;
            this.description = description;
            this.idCategory = idCategory;
            this.rate = rate;
            this.similar = similar;
            this.isFeature = isFeature;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSale() {
            return sale;
        }

        public void setSale(String sale) {
            this.sale = sale;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIdCategory() {
            return idCategory;
        }

        public void setIdCategory(String idCategory) {
            this.idCategory = idCategory;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getSimilar() {
            return similar;
        }

        public void setSimilar(String similar) {
            this.similar = similar;
        }

        public String getIsFeature() {
            return isFeature;
        }

        public void setIsFeature(String isFeature) {
            this.isFeature = isFeature;
        }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price='" + price + '\'' +
                ", sale='" + sale + '\'' +
                ", description='" + description + '\'' +
                ", idCategory='" + idCategory + '\'' +
                ", rate='" + rate + '\'' +
                ", similar='" + similar + '\'' +
                ", isFeature='" + isFeature + '\'' +
                '}';
    }
}
