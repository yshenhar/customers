package com.netapp.customers;

import org.jboss.arquillian.protocol.servlet.arq514hack.descriptors.api.web.WebAppDescriptor;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;

public abstract class BaseDeployment {

  EnterpriseArchive ear;
  WebArchive war;
  JavaArchive jar;

  WebAppDescriptor webXml = Descriptors.create(WebAppDescriptor.class);

  public BaseDeployment() {
    ear = ShrinkWrap.create(EnterpriseArchive.class);
    war = ShrinkWrap.create(WebArchive.class);
    jar = ShrinkWrap.create(JavaArchive.class);

    jar.addClasses(String.class, Integer.class);
    war.addAsLibrary(jar);
    ear.addAsModule(war);

    webXml.version("3.0");
  }


  public Archive<?> build() {
    war.setWebXML(new StringAsset(webXml.exportAsString()));
    return ear;
  }
}
