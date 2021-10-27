package servicesequest.hctx.net.Model;

import java.io.Serializable;
import java.util.ArrayList;


public class CategoryTypeSet implements Serializable {
    private static final long serialVersionUID = -6099312954099962806L;

    public String HC_Order;
    public String Category;
    public String Code;

    public CategoryTypeSet(String HC_Order, String Category, String Code) {
        this.HC_Order = HC_Order;
        this.Category = Category;
        this.Code = Code;
    }

    public static ArrayList<CategoryTypeSet> categoryTypeSet = new ArrayList<>();
}
