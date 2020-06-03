package servicesequest.hctx.net.DAL;

import android.content.Context;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpsTransportSE;

import java.lang.reflect.Parameter;

import servicesequest.hctx.net.Model.Profile;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.Utility.AllCertificatesAndHostsTruster;


public class PushDataKsoap {

	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String SOAP_ACTION = "http://tempuri.org/uploadInformation";
	private static final String METHOD_NAME = "uploadInformation";

	private ServiceRequestDbHelper dbHelper;
	private Profile _profile;

	public Boolean passing(Context _Context, Request R, String UniqueId) {

		dbHelper = new ServiceRequestDbHelper(_Context);
		ProfileDataManager datamanager = new ProfileDataManager();
		_profile = datamanager.getProfileById(dbHelper, "1");
		dbHelper.close();

		MarshalBase64 marshal = new MarshalBase64();
		try {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


			request.addProperty("f", R.Image);
			request.addProperty("UniqueId", UniqueId);

			if (R.Image != null)
				request.addProperty("ImageName", R.Image_Name);
			else
				request.addProperty("ImageName", "");

			request.addProperty("Latitude", String.valueOf(R.Latitude));
			request.addProperty("Longitude", String.valueOf(R.Longitude));
			request.addProperty("FullAddress", R.FullAddress);
			request.addProperty("Last_Name", _profile.lastname);
			request.addProperty("First_Name", _profile.firstname);
			request.addProperty("Address",	_profile.streetaddress);
			request.addProperty("City", _profile.city);
			request.addProperty("State", _profile.state);
			request.addProperty("Zip_Code", _profile.zipcode);
			request.addProperty("Home_Phone", _profile.primaryphone);
			request.addProperty("Business_Phone", "");
			request.addProperty("Cell_Phone", "");
			request.addProperty("Alternate_Phone", _profile.secondaryphone);
			request.addProperty("Email", _profile.email);
			request.addProperty("Precinct", R.Precinct);
			request.addProperty("RequestType", R.RequestType);
			request.addProperty("RequestTypeValue", R.RequestTypeValue);
			request.addProperty("Area", R.Area);
			request.addProperty("Subdivision", R.Subdivision);
			request.addProperty("CrossStreet", R.CrossStreet);
			request.addProperty("Description", R.Description);
			request.addProperty("ReceivedType", "8");
			request.addProperty("CompanyName", "");


			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.dotNet = true;
			soapEnvelope.setOutputSoapObject(request);
			marshal.register(soapEnvelope);

			//System.out.println("######################  request " + request);

			HttpsTransportSE aht = new HttpsTransportSE("appsqa.harriscountytx.gov", 443, "/ServicesRequestMobile/MobileService.asmx", 30000); //QA
			//HttpsTransportSE aht = new HttpsTransportSE("apps.harriscountytx.gov", 443, "/ServicesRequestMobile/MobileService.asmx", 30000); //Prod
			AllCertificatesAndHostsTruster.apply();
			aht.call(SOAP_ACTION, soapEnvelope);


			Object response = soapEnvelope.getResponse();
			//System.out.println("######################  uploadInformation 1: " + response);

			if (response.toString().contains("not valid"))
				return false;

			return true;
		} catch (Exception e) {
			//System.out.println("######################  uploadInformation 2: " + e.toString());
			return false;
		}
	}

}

