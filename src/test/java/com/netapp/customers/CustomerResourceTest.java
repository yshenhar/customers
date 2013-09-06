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
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatter;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

import static org.fest.assertions.api.Assertions.assertThat;


@RunWith(Arquillian.class)
public class CustomerResourceTest {
  @Deployment
  public static Archive<?> getDeployment() throws FileNotFoundException {

    final FileOutputStream stream = new FileOutputStream(new File("/Users/yehoram/Desktop", "archive.out"));

    Archive<?> archive = new BaseDeployment() {
      {
        war.addClasses(Number.class);
        //webXml.createServlet().servletClass(Number.class.getName());
        war.addPackage(Customer.class.getPackage());
        war.addClasses(RestCustomer.class,
                CustomerResource.class,
                CustomerRepositoryBean.class,
                CustomerRepositoryCommonBusiness.class,
                CustomerRepositoryLocalBusiness.class,
                CustomerRepositoryRemoteBusiness.class
        );
        war.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
        war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        war.addAsWebInfResource("jbossas-ds.xml");

        war.writeTo(stream, Formatters.VERBOSE);
      }
    }.build();



    return archive;
  }

  @Inject
  CustomerRepositoryCommonBusiness customerRepo;


  @Test
  @RunAsClient
  public void postWillAddCustomer()
          throws IOException, URISyntaxException, JAXBException {

    RestCustomer customer = new RestCustomer(null, "Dave", "Smith");
    StringWriter writer = new StringWriter();
    JAXBContext jaxbContext = JAXBContext.newInstance(RestCustomer.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    jaxbMarshaller.marshal(customer, writer);

    HttpClient client = new DefaultHttpClient();

    try {
      URL url = new URL("http://127.0.0.1:8080/customers");
      HttpPost postRequest = new HttpPost(url.toURI());
      postRequest.addHeader("content-type", "application/xml");
      StringEntity custEntity = new StringEntity(writer.getBuffer().toString());
      postRequest.setEntity(custEntity);

      System.out.println("\n\ncustomer" + writer.getBuffer().toString());

      HttpResponse response = client.execute(postRequest);

      assertThat(response.getStatusLine().getStatusCode())
              .as("request failed with " + response.getStatusLine()).isEqualTo(200);
    } finally {
      client.getConnectionManager().shutdown();
    }
  }
}
