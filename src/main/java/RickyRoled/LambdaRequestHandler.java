package RickyRoled;

import Membership.MembershipState;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.util.json.Jackson;

import java.io.IOException;

public class LambdaRequestHandler
        implements RequestHandler<KinesisEvent, Void> {


    public Void handleRequest(KinesisEvent event, Context context) {
        for(KinesisEvent.KinesisEventRecord rec : event.getRecords()) {
            String str = new String(rec.getKinesis().getData().array());
            MembershipState state = Jackson.fromJsonString(str, MembershipState.class);
            context.getLogger().log("Membership State received: " + state);
            context.getLogger().log("Calculator will be here eventually: " + str);
        }
        return null;
    }
}
