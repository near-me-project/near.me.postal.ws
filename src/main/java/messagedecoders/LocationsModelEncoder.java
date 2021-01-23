package messagedecoders;

import com.google.gson.Gson;
import model.LocationsModel;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class LocationsModelEncoder implements Encoder.Text<LocationsModel> {

    private Gson gson = new Gson();

    @Override
    public String encode(LocationsModel model) throws EncodeException {
        return gson.toJson(model);
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
