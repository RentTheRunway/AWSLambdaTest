package RickyRoled;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LambdaRequestHandler implements RequestHandler<KinesisEvent, Void> {
    public Void handleRequest(KinesisEvent event, Context context) {
        //String clientEndpoint = "a1isiazbq6kf4m.iot.us-east-1.amazonaws.com";
        //AmazonSNS snsClient =  AmazonSNSClientBuilder.standard().withCredentials(new EnvironmentVariableCredentialsProvider()).build();
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new EnvironmentVariableCredentialsProvider()).build();
        for(KinesisEvent.KinesisEventRecord rec : event.getRecords()) {
            String str = "Calculator will be here eventually: " + new String(rec.getKinesis().getData().array());
            context.getLogger().log("Calculator will be here eventually: " + new String(rec.getKinesis().getData().array()));
            File file = new File(System.getProperty("user.dir") + "/membershipactions/text.txt");
            try {
                OutputStream out = new FileOutputStream(file);
                byte[] strToBytes = str.getBytes();
                out.write(strToBytes);

                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            s3Client.putObject(new PutObjectRequest("membershipaction", "text.txt", file));
        }
        return null;
    }
}
