package together.capstone2together;


import com.oracle.bmc.ConfigFileReader;


import com.oracle.bmc.ConfigFileReader.ConfigFile;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;

import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadRequest;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadResponse;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Map;

//@SpringBootApplication
public class OciUnitTest {

    //버킷에 객체 업로드 테스트 안됨 jersey 랑 oci 버전, 호환성 등등 문제인듯.. 일단 도메인에서 버킷으로 저장되는 필드는 없으니까
    //지금은 별로 상관없는데, 나중에 버킷 연결 어디에 시킬 지에 따라 다시 의존성 수정해보던가
    //amazon s3으로 사용해보던가 해야할듯.....
    public static void main(String[] args) throws Exception {

        ConfigFile config = ConfigFileReader.parse("C:/Users/admin/ocikey/config", "DEFAULT");

        AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);

        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(Region.AP_CHUNCHEON_1);


        //upload object
        UploadConfiguration uploadConfiguration =
                UploadConfiguration.builder()
                        .allowMultipartUploads(true)
                        .allowParallelUploads(true)
                        .build();

        UploadManager uploadManager = new UploadManager(client, uploadConfiguration);

        String namespaceName = "axmqagbbe8bt";
        String bucketName = "bucket-demo";
        String objectName = "test_object.jpg";
        Map<String, String> metadata = null;
        String contentType = "image/jpg";
        String contentEncoding = null;
        String contentLanguage = null;

        File body = new File("C:/Users/admin/Desktop/캡스톤 예상 구조.png");

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucketName(bucketName)
                        .namespaceName(namespaceName)
                        .objectName(objectName)
                        .contentType(contentType)
                        .contentLanguage(contentLanguage)
                        .contentEncoding(contentEncoding)
                        .opcMeta(metadata)
                        .build();


        UploadRequest uploadDetails =
                UploadRequest.builder(body).allowOverwrite(true).build(request);

        UploadResponse response = uploadManager.upload(uploadDetails);
        System.out.println(response);

        client.close();
    }

}
