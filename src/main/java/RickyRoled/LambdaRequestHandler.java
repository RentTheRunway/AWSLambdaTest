package RickyRoled;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

public class LambdaRequestHandler
        implements RequestHandler<KinesisEvent, Void> {
    public Void handleRequest(KinesisEvent event, Context context) {
        for(KinesisEvent.KinesisEventRecord rec : event.getRecords()) {
            context.getLogger().log("Calculator will be here eventually: " + new String(rec.getKinesis().getData().array()));
        }
        return null;
    }
}
