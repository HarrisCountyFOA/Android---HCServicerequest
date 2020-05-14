package servicesequest.hctx.net.Model;

/**
 * Created by jmelendrez on 8/31/2016.
 */
public class RequestTypeSelectSet {
    public String key;
    public String value;

    public RequestTypeSelectSet(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return (key);
    }
}
