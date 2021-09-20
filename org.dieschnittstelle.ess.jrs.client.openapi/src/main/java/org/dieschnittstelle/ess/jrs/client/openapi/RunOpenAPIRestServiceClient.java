package org.dieschnittstelle.ess.jrs.client.openapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.logging.log4j.Logger;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.dieschnittstelle.jrs.client.openapi.api.DefaultApi;
import org.dieschnittstelle.jrs.client.openapi.model.ApiOpiTouchpointsAddress;
import org.dieschnittstelle.jrs.client.openapi.model.InlineResponseDefault;
import org.dieschnittstelle.jrs.client.openapi.model.InlineResponseDefault1;
import org.dieschnittstelle.jrs.client.openapi.model.OrgDieschnittstelleEssEntitiesCrmStationaryTouchpoint;

import java.util.ArrayList;
import java.util.List;

public class RunOpenAPIRestServiceClient {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(RunOpenAPIRestServiceClient.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// create the service proxy, using a jackson provider instance that is configured not
		// to fail on unknown properties (most importantly, the @class property)
		JacksonJsonProvider provider = new JacksonJsonProvider();
		provider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		List providers = new ArrayList();
		providers.add(provider);

		DefaultApi serviceProxy = JAXRSClientFactory.create("http://localhost:8080", DefaultApi.class, providers);
/*
		org.apache.cxf.jaxrs.client.Client client = WebClient.client(serviceProxy);

		ClientConfiguration config = WebClient.getConfig(client);
 read all
*/

		List<InlineResponseDefault1> existingTouchpoints = serviceProxy.readAllTouchpoints();
		logger.info("read " + existingTouchpoints.size() + " touchpoints: " + toSinglelineString(existingTouchpoints));

		// if we have more than 0 touchpoints, delete the first one
		if (existingTouchpoints.size() > 0) {
			serviceProxy.deleteTouchpoint(existingTouchpoints.get(0).getId());
			logger.info("after deletion, read " + serviceProxy.readAllTouchpoints().size() + " touchpoints");
		}

		// create a new touchpoint - note that all attributes with primitive types need to be set to a default value
		// as the generated code uses the wrapper types, which will be passed as null values otherwise, causing
		// server-side trouble
		OrgDieschnittstelleEssEntitiesCrmStationaryTouchpoint newTouchpoint = new OrgDieschnittstelleEssEntitiesCrmStationaryTouchpoint();
		ApiOpiTouchpointsAddress address = new ApiOpiTouchpointsAddress();
		address.setCity("Berlin");
		address.setStreet("Luxemburger Str.");
		address.setZipCode("13353");
		address.setHouseNr("10");
		address.setGeoLat(0);
		address.setGeoLong(0);
		address.setId(0);
		newTouchpoint.setErpPointOfSaleId(0);
		newTouchpoint.setAddress(address);
		newTouchpoint.setName("BHT OpenAPI Touchpoint");
		newTouchpoint.setId(0);

		// create
		OrgDieschnittstelleEssEntitiesCrmStationaryTouchpoint tpp = serviceProxy.createTouchpoint(newTouchpoint);
		logger.info("created: " + toSinglelineString(tpp));

		// read the created one
		OrgDieschnittstelleEssEntitiesCrmStationaryTouchpoint resp = serviceProxy.readTouchpoint(tpp.getId());
		logger.info("read created: " + toSinglelineString(resp));

	}

	public static String toSinglelineString(Object obj) {
		String trimmed = String.valueOf(obj).replaceAll("\n","");
		while (trimmed.indexOf("  ") != -1) {
			trimmed = trimmed.replaceAll("  "," ");
		}
		return trimmed;
	}

}
