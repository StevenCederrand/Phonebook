import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class JSONCompare implements Comparator<JSONObject> {
    private String nameKey;

    public JSONCompare(String nameKey) {
        this.nameKey = nameKey;
    }

    //We only compare names, considering that most phone contact systems don't allow you to add contacts with the same name
    //This may be expanded, to make sure that duplication also depends on number as well
    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        int nameComparison = 0;

        try {
            //Compare to see if the names are the same
            nameComparison = (o1.getString(nameKey).toUpperCase().compareTo(o2.getString(nameKey).toUpperCase()));

        }catch (JSONException e) {
            e.printStackTrace();
        }

        return nameComparison;
    }
}