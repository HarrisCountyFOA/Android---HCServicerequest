package servicesequest.hctx.net.Model;


public class CategoryTypeSelectSet {
    public String hc_order;
    public String key;
    public String value;

    public CategoryTypeSelectSet(String key, String value, String hc_order) {
        this.key = key;
        this.value = value;
        this.hc_order = hc_order;
    }

    @Override
    public String toString() {
        return (key);
    }
}
