import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mock.action.ExpectationForwardAndResponseCallback;
import org.mockserver.mock.action.ExpectationForwardCallback;
import org.mockserver.mock.action.ExpectationResponseCallback;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.notFoundResponse;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.MediaType.APPLICATION_JSON;

public class Example {
    private ClientAndServer mockServer;
    @Before
    public void before(){
        ConfigurationProperties.maxSocketTimeout(120000L);
        ConfigurationProperties.dynamicallyCreateCertificateAuthorityCertificate(true);
        ConfigurationProperties.directoryToSaveDynamicSSLCertificate("/Users/lalith.kumar/MockserverCert");
        ConfigurationProperties.sslCertificateDomainName("localhost");
        ConfigurationProperties.addSslSubjectAlternativeNameDomains(new String[]{"www.example.com", "www.another.com"});
        ConfigurationProperties.addSslSubjectAlternativeNameIps(new String[]{"127.0.0.1"});
        ConfigurationProperties.enableCORSForAPI(true);
        ConfigurationProperties.enableCORSForAllResponses(true);
        ConfigurationProperties.disableSystemOut(true);
        mockServer = startClientAndServer(1080);
    }
    @Test
    public void test1() throws InterruptedException {
        responseClassCallback2();
        for(;;)
        {

        }
    }
    //sample JSon Response {[{'name':'test1'},{'name':'test2'},{'name':'test3'}]}
    // After modification {[{'name':'test1 Automation Test'},{'name':'test2 Automation Test'},{'name':'test3 Automation Test'}]}
    public void responseClassCallback2() {
        new MockServerClient("localhost",1080)
                .when(
                        request()
                                .withPath("/Endpoint2*")
                ).forward(
                new ExpectationForwardCallback() {
                    @Override
                    public HttpRequest handle(HttpRequest httpRequest) throws Exception {
                        return httpRequest;
                    }
                },
                new ExpectationForwardAndResponseCallback() {
                    @Override
                    public HttpResponse handle(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
                        String val = httpResponse.getBodyAsString();
                        JSONArray jsonObject = (JSONArray) new JSONParser().parse(val);
                        for(int i=0;i<jsonObject.size();i++)
                        {
                            JSONObject obj = (JSONObject) jsonObject.get(i);
                            obj.put("name",obj.get("name")+" Automation Test");
                        }
                        return httpResponse
                                .removeHeader(CONTENT_LENGTH.toString())
                                .withBody(jsonObject.toJSONString());
                    }
                }
        );
    }
    @After
    public void stopSe()
    {
        mockServer.stop();
    }

}


