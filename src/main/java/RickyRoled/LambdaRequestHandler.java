package RickyRoled;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LambdaRequestHandler implements RequestHandler<KinesisEvent, Void> {
    public Void handleRequest(KinesisEvent event, Context context) {
        File file = new File("/tmp/text.txt");
        for(KinesisEvent.KinesisEventRecord rec : event.getRecords()) {
            String str = "Calculator will be here eventually: " + new String(rec.getKinesis().getData().array());
            context.getLogger().log(str);
            try {
                FileWriter fw = new FileWriter(file);
                fw.write(str);
                fw.close();
                AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new EnvironmentVariableCredentialsProvider()).build();
                //Upload to S3
                s3Client.putObject(new PutObjectRequest("membershipstate", "text.txt", file));
            } catch (IOException iox) {
                //do stuff with exception
                iox.printStackTrace();
            }


        }
        return null;
    }
}
