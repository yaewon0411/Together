package together.capstone2together;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.CreateBucketDetails;
import com.oracle.bmc.objectstorage.requests.*;
import com.oracle.bmc.objectstorage.responses.GetBucketResponse;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//@SpringBootApplication
public class GetBucketTest {
        public static void main(String[] args) throws Exception {
            String configurationFilePath = "C:/Users/admin/ocikey/config";
            String profile = "DEFAULT";


            final String compartmentId = "yaewon0411";
            final String bucket = "bucket-demo";
            final String object = "test_object.jpg";

            // Configuring the AuthenticationDetailsProvider. It's assuming there is a default OCI
            // config file
            // "~/.oci/config", and a profile in that config with the name "DEFAULT". Make changes to
            // the following
            // line if needed and use ConfigFileReader.parse(CONFIG_LOCATION, CONFIG_PROFILE);

            final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath, profile);

            final AuthenticationDetailsProvider provider =
                    new ConfigFileAuthenticationDetailsProvider(configFile);

            ObjectStorage client =
                    ObjectStorageClient.builder().region(Region.AP_CHUNCHEON_1).build(provider);

            System.out.println("Getting the namespace.");
            GetNamespaceResponse namespaceResponse =
                    client.getNamespace(GetNamespaceRequest.builder().build());
            String namespaceName = namespaceResponse.getValue();

            System.out.println("Creating the source bucket.");
            CreateBucketDetails createSourceBucketDetails =
                    CreateBucketDetails.builder().compartmentId(compartmentId).name(bucket).build();
            CreateBucketRequest createSourceBucketRequest =
                    CreateBucketRequest.builder()
                            .namespaceName(namespaceName)
                            .createBucketDetails(createSourceBucketDetails)
                            .build();
            client.createBucket(createSourceBucketRequest);

            System.out.println("Creating the source object");
            PutObjectRequest putObjectRequest =
                    PutObjectRequest.builder()
                            .namespaceName(namespaceName)
                            .bucketName(bucket)
                            .objectName(object)
                            .contentLength(4L)
                            .putObjectBody(
                                    new ByteArrayInputStream("data".getBytes(StandardCharsets.UTF_8)))
                            .build();
            client.putObject(putObjectRequest);

            System.out.println("Creating Get bucket request");
            List<GetBucketRequest.Fields> fieldsList = new ArrayList<>(2);
            fieldsList.add(GetBucketRequest.Fields.ApproximateCount);
            fieldsList.add(GetBucketRequest.Fields.ApproximateSize);
            GetBucketRequest request =
                    GetBucketRequest.builder()
                            .namespaceName(namespaceName)
                            .bucketName(bucket)
                            .fields(fieldsList)
                            .build();

            System.out.println("Fetching bucket details");
            GetBucketResponse response = client.getBucket(request);

            System.out.println("Bucket Name : " + response.getBucket().getName());
            System.out.println("Bucket Compartment : " + response.getBucket().getCompartmentId());
            System.out.println(
                    "The Approximate total number of objects within this bucket : "
                            + response.getBucket().getApproximateCount());
            System.out.println(
                    "The Approximate total size of objects within this bucket : "
                            + response.getBucket().getApproximateSize());
        }
}
