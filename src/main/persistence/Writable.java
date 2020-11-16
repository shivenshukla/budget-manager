package persistence;

import org.json.JSONObject;

// This code is modeled on the Writable class from https://github.com/stleary/JSON-java

// Represents the conversion between an object to a JSON object
public interface Writable {

    // EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
