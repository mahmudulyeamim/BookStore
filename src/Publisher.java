public class Publisher {
    private int publisherId;
    private String name, country, establishedDate;

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEstablishedDate(String establishedDate) {
        this.establishedDate = establishedDate;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getEstablishedDate() {
        return establishedDate;
    }
}
