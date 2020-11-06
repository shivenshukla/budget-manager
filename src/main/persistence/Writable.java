package persistence;

import org.json.JSONObject;

// Represents the conversion between an object to a JSON object
// This code is modeled on the Writable class from https://github.com/stleary/JSON-java
public interface Writable {

    // EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
