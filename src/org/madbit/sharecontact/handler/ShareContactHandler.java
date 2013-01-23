package org.madbit.sharecontact.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.madbit.sharecontact.addressbook.domain.Contact;
import org.madbit.sharecontact.addressbook.domain.ContactDetail;
import org.madbit.sharecontact.addressbook.domain.IUser;
import org.madbit.sharecontact.common.UserFactory;
import org.madbit.sharecontact.exception.ServiceException;

import android.util.Log;

public class ShareContactHandler {
	
	private static final String TAG = "org.madbit.sharecontact.handler.ShareContactHandler";
	
	public boolean shareContact(Contact toContact, Contact sharedContact) throws ServiceException {
		
		IUser localUser = UserFactory.getUser();
		Document reqDocument = createRequest(toContact, sharedContact, localUser);
		
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());		
		String request = xmlOutput.outputString(reqDocument);
		Log.d(TAG, request);
		
		Document resDocument = postRequest(request);
		
		return isSuccessResponse(resDocument);		
	}

	private Document createRequest(Contact toContact, Contact sharedContact, IUser localUser) {
		
		// 		Example of request
		//		
		//		<ShareContactRequest xmlns="http://www.madbit.org/ShareContactService">
		//	    <user>
		//	        <display_name></display_name>
		//	        <phone_number>123</phone_number>
		//	    </user>
		//	    <to_contact>
		//	        <display_name></display_name>
		//	        <phone_numbers>
		//	            <phone_number>
		//		            <type></type>
		//		            <value>1235</value>
		//	            </phone_number>
		//	        </phone_numbers>
		//	    </to_contact>
		//	    <contact_to_share>
		//	        <display_name></display_name>
		//	        <phone_numbers>
		//	            <phone_number>
		//		            <type></type>
		//		            <value></value>
		//	            </phone_number>
		//	        </phone_numbers>
		//	    </contact_to_share>
		//	</ShareContactRequest>
		//
		
		Element rootElement = new Element("ShareContactRequest");
		Namespace namespace = Namespace.getNamespace("xs", "http://www.madbit.org/ShareContactService");
		rootElement.addNamespaceDeclaration(namespace);
		rootElement.setNamespace(namespace);

		// BOF user
		Element user = new Element("user");
		user.setNamespace(namespace);

		Element userDisplayName =  new Element("display_name");
		userDisplayName.setNamespace(namespace);
		userDisplayName.addContent("pippo");
		user.addContent(userDisplayName);

		Element userPhoneNumber =  new Element("phone_number");
		userPhoneNumber.setNamespace(namespace);
		userPhoneNumber.addContent(localUser.getMsisdn());
		user.addContent(userPhoneNumber);

		rootElement.addContent(user);
		// EOF user
		
		// BOF toContact
		Element contact = new Element("to_contact");
		contact.setNamespace(namespace);

		Element contactDisplayName =  new Element("display_name");
		contactDisplayName.setNamespace(namespace);
		contactDisplayName.addContent("pippo");
		contact.addContent(contactDisplayName);
		
		Element contactPhoneNumbers = new Element("phone_numbers");
		contactPhoneNumbers.setNamespace(namespace);
		
		for(ContactDetail cs: toContact.getPhoneNumbers()) {
			Element phoneNumber = new Element("phone_number");
			phoneNumber.setNamespace(namespace);
			
			Element type = new Element("type");
			type.setNamespace(namespace);
			
			type.addContent(cs.getContactDetailType().toString());
			phoneNumber.addContent(type);
			
			Element value = new Element("value");
			value.setNamespace(namespace);
			
			value.addContent(cs.getValue());
			phoneNumber.addContent(value);
			
			contactPhoneNumbers.addContent(phoneNumber);
		}
		contact.addContent(contactPhoneNumbers);
		
		rootElement.addContent(contact);
		// EOF toContact
		
		// BOF sharedContact
		Element shared = new Element("contact_to_share");
		shared.setNamespace(namespace);

		Element sharedDisplayName =  new Element("display_name");
		sharedDisplayName.setNamespace(namespace);
		
		sharedDisplayName.addContent("toContact");
		shared.addContent(sharedDisplayName);

		Element sharedPhoneNumbers = new Element("phone_numbers");
		sharedPhoneNumbers.setNamespace(namespace);
		
		for(ContactDetail cs: sharedContact.getPhoneNumbers()) {
			Element phoneNumber = new Element("phone_number");
			phoneNumber.setNamespace(namespace);
			
			Element type = new Element("type");
			type.setNamespace(namespace);
			
			type.addContent(cs.getContactDetailType().toString());
			phoneNumber.addContent(type);
			
			Element value = new Element("value");
			value.setNamespace(namespace);
			
			value.addContent(cs.getValue());
			phoneNumber.addContent(value);
			
			sharedPhoneNumbers.addContent(phoneNumber);
		}
		shared.addContent(sharedPhoneNumbers);
		
		rootElement.addContent(shared);
		// EOF sharedContact		
		
		Document document = new Document(rootElement);
		return document;
	}

	private Document postRequest(String request) throws ServiceException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://192.168.137.54:8080/sc/rest/shareContact");
		post.addHeader("Accept", "application/xml");
		post.addHeader("Content-Type", "application/xml");
		
		try {
			StringEntity entity = new StringEntity(request, "UTF-8");
			entity.setContentType("application/xml");
			post.setEntity(entity);
			
			HttpResponse response = httpClient.execute(post);
			return convertResponse(response);
			
//			post.releaseConnection();
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException();
		} catch (ClientProtocolException e) {
			throw new ServiceException();
		} catch (IOException e) {
			throw new ServiceException();
		} catch (JDOMException e) {
			throw new ServiceException();
		}
	}
	
	private Document convertResponse(HttpResponse response) throws IOException, JDOMException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuilder builder = new StringBuilder();
		String aux = "";		
		while ((aux = br.readLine()) != null)
		    builder.append(aux);
		
		br.close();
		String text = builder.toString();
		SAXBuilder saxBuilder = new SAXBuilder();
		Document document = saxBuilder.build( new StringReader(text));
		return document;
	}
	
	private boolean isSuccessResponse(Document document) {
		Element rootElement;
		rootElement = document.getRootElement();
		List<Element> children = rootElement.getChildren();
		for(Element e: children) {
			if(e.getName().equals("status") && e.getValue().equals("0"))
				return true;
		}
//		String result = rootElement.getChild("status").getValue();
//		if(result.equals("0"))
//			return true;
//		else
			return false;
	}

}
