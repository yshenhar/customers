package com.netapp.customers;

import com.netapp.customers.rest.CustomerResource;
import com.netapp.customers.rest.RestCustomer;
import com.netapp.customers.server.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;

@RunWith(Arquillian.class)
public class CustomerResourceTest {

  @Deployment
  public static WebArchive createDeployment() throws FileNotFoundException {
    WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "full-stack.war");
    webArchive.addPackage(Customer.class.getPackage());
    webArchive.addClasses(RestCustomer.class,
                    CustomerResource.class,
                    CustomerRepositoryBean.class,
                    CustomerRepositoryCommonBusiness.class,
                    CustomerRepositoryLocalBusiness.class,
                    CustomerRepositoryRemoteBusiness.class
            );
    webArchive.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
    webArchive.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    webArchive.addAsWebInfResource("jbossas-ds.xml");
    return webArchive;
  }

  @Inject
  CustomerRepositoryCommonBusiness customerRepo;

  //@Test
  //@RunAsClient
  public void putWillUpdateCustomer()
          throws IOException, URISyntaxException, JAXBException {
    // given
    Customer customer = new Customer("Dave", "Smith");
    customerRepo.add(customer);

    RestCustomer restCust = new RestCustomer(1l, "David", "Smith");
    StringWriter writer = new StringWriter();
    JAXBContext jaxbContext = JAXBContext.newInstance(RestCustomer.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    jaxbMarshaller.marshal(customer, writer);

    HttpClient client = new DefaultHttpClient();

    try {

    } finally {
      client.getConnectionManager().shutdown();
    }
  }

  @Test
  @RunAsClient
  public void postWillAddCustomer()
          throws IOException, URISyntaxException, JAXBException {
    System.out.println("**** Starting test");

    RestCustomer customer = new RestCustomer(null, "Dave", "Smith");
    StringWriter writer = new StringWriter();
    JAXBContext jaxbContext = JAXBContext.newInstance(RestCustomer.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    jaxbMarshaller.marshal(customer, writer);

    HttpClient client = new DefaultHttpClient();

    try {
      URL url = new URL("http://localhost:8080/customers");
      HttpPost postRequest = new HttpPost(url.toURI());
      postRequest.addHeader("content-type", "application/xml");
      StringEntity custEntity = new StringEntity(writer.getBuffer().toString());
      postRequest.setEntity(custEntity);

      HttpResponse response = client.execute(postRequest);

      // TODO add assertion
      System.out.println("\n\nResponse Code:" + response.getStatusLine()+ "\n\n");
    } finally {
      client.getConnectionManager().shutdown();
    }
  }


}
