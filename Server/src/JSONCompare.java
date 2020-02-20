import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class JSONCompare implements Comparator<JSONObject> {
    String compareField;

    public JSONCompare(String compareField) {
        this.compareField = compareField;
    }

    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        String o1Name = "";
        String o2Name = "";
        try {
            o1Name = o1.getString(compareField);
            o2Name = o2.getString(compareField);
        }catch (JSONException jsonException) {
            System.out.println("Failed Comparison");
        }

        return o1Name.compareTo(o2Name);
    }
}