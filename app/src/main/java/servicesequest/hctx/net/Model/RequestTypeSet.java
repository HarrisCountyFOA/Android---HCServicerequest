package servicesequest.hctx.net.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jmelendrez on 8/31/2016.
 */
public class RequestTypeSet implements Serializable {
    private static final long serialVersionUID = -6099312954099962806L;

    public String Name;
    public String Category;
    public String Code;

    public RequestTypeSet(String Name, String Category, String Code) {
        this.Name = Name;
        this.Category = Category;
        this.Code = Code;
    }

    public static ArrayList<RequestTypeSet> requestTypeSet = new ArrayList<>();
}
