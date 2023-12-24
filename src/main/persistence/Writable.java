package persistence;

import org.json.JSONObject;

// Represents the conversion between an object to a JSON object
public interface Writable {

    // EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
