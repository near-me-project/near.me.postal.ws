package messagedecoders;

import com.google.gson.Gson;
import model.SubscriptionRequestModel;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class SubscriptionRequestModelEncoder implements Encoder.Text<SubscriptionRequestModel> {

    private Gson gson = new Gson();

    @Override
    public String encode(SubscriptionRequestModel subscriptionRequestModel) throws EncodeException {
        return gson.toJson(subscriptionRequestModel);
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
