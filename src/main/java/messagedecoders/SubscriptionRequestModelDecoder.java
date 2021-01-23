package messagedecoders;

import com.google.gson.Gson;
import model.SubscriptionRequestModel;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class SubscriptionRequestModelDecoder implements Decoder.Text<SubscriptionRequestModel> {

    private Gson gson = new Gson();

    @Override
    public SubscriptionRequestModel decode(String s) throws DecodeException {
        return gson.fromJson(s, SubscriptionRequestModel.class);
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
